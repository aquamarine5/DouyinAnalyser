/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */

package org.aquarngd.douyinanalyser;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@EnableScheduling
@SpringBootApplication
public class DouyinAnalyserApplication {

    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(DouyinAnalyserApplication.class);
    private final Environment environment;

    public DouyinAnalyserApplication(JdbcTemplate jdbcTemplate, Environment environment) {
        this.jdbcTemplate = jdbcTemplate;
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(DouyinAnalyserApplication.class, args);
    }

    @Scheduled(cron = "0 10 0 * * *")
    public void updateLikeCount() throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();
        SqlRowSet userlist = jdbcTemplate.queryForRowSet("SELECT `key`, `id` FROM userinfo");
        while (userlist.next()) {
            String key = userlist.getString("key");
            logger.info("Updating like count for user {}", key);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tikhub.io/api/v1/douyin/app/v3/handler_user_profile?sec_user_id=" + key))
                    .GET()
                    .header("Authorization","Bearer " + environment.getProperty("TIKHUB_TOKEN"))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = JSONObject.parseObject(response.body());
            String numberDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            int likecount = jsonResponse.getJSONObject("data").getJSONObject("user").getIntValue("favoriting_count",0);
            if (likecount == 0) {
                logger.warn("No like count found for user {}", key);
                continue;
            }
            jdbcTemplate.update("INSERT INTO `counts` (date, userid, likecount) VALUES (?, ?, ?) AS newvalue ON DUPLICATE KEY UPDATE likecount = newvalue.likecount",
                    numberDate, userlist.getInt("id"), likecount);

            logger.info("Updating like date {} likecount {}", key,likecount);
        }
        httpClient.close();
    }
}
