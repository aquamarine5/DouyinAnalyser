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
    private final DouyinLikeAnalyser douyinLikeAnalyser;

    public DouyinAnalyserApplication(JdbcTemplate jdbcTemplate, DouyinLikeAnalyser douyinLikeAnalyser) {
        this.jdbcTemplate = jdbcTemplate;
        this.douyinLikeAnalyser = douyinLikeAnalyser;
    }

    public static void main(String[] args) {
        SpringApplication.run(DouyinAnalyserApplication.class, args);
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void updateLikeCount() throws IOException, InterruptedException {
        douyinLikeAnalyser.analyseAllData((id, likeCount) -> {
            String numberDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            jdbcTemplate.update("INSERT INTO `counts` (date, userid, likecount) VALUES (?, ?, ?) AS newvalue ON DUPLICATE KEY UPDATE likecount = newvalue.likecount",
                    numberDate, id, likeCount);
        });
    }
}
