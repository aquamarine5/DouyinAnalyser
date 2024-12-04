package org.aquarngd.douyinanalyser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;

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
    public void updateLikeCount() {

    }
}
