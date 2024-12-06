package org.aquarngd.douyinanalyser;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.scheduling.annotation.Scheduled;

import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class DouyinAnalyserApplication {

    private final JdbcTemplate jdbcTemplate;

    public DouyinAnalyserApplication(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(DouyinAnalyserApplication.class, args);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void updateLikeCount() throws IOException, InterruptedException {
        HttpClient httpClient=HttpClient.newHttpClient();
        SqlRowSet userlist = jdbcTemplate.queryForRowSet("SELECT `key`, `id` FROM userinfo");
        while (userlist.next()) {
            String key = userlist.getString("key");
            HttpRequest request=HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:1125/get?key="+key))
                    .GET()
                    .build();
            HttpResponse<String> response= httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse=JSONObject.parseObject(response.body());
            String numberDate= LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            jdbcTemplate.update("INSERT INTO `counts` (date, userid, likecount) VALUES (?, ?, ?)",
                    numberDate,String.valueOf(userlist.getInt("id")),String.valueOf(jsonResponse.getIntValue("likeCount")));
        }
        httpClient.close();
    }
}
