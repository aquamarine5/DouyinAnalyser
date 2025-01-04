/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */

package org.aquarngd.douyinanalyser;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class DouyinLikeAnalyser {
    private final Environment environment;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Logger logger = LoggerFactory.getLogger(DouyinLikeAnalyser.class);
    public static final int LIKE_PERMISSION_NOT_PUBLIC = -100;
    public static final int LIKE_PERMISSION_PUBLIC = 0;
    private final JdbcTemplate jdbcTemplate;

    public DouyinLikeAnalyser(Environment environment, JdbcTemplate jdbcTemplate) {
        this.environment = environment;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void analyseAllData(LikeCountDataCallback callback) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT `key`, `id`, `isLikePublic` FROM userinfo");
        while (sqlRowSet.next()) {
            String key = sqlRowSet.getString("key");
            int id = sqlRowSet.getInt("id");
            boolean isLikePublic = sqlRowSet.getBoolean("isLikePublic");
            try {
                int likeCount = analyseDataBySecUserId(key);
                if (isLikePublic && likeCount == LIKE_PERMISSION_NOT_PUBLIC) {
                    jdbcTemplate.update("UPDATE userinfo SET isLikePublic=0 WHERE id=?", id);
                }
                if ((!isLikePublic) && likeCount != LIKE_PERMISSION_NOT_PUBLIC) {
                    jdbcTemplate.update("UPDATE userinfo SET isLikePublic=1 WHERE id=?", id);
                }
                callback.onLikeCountRetrieved(id, likeCount);
            } catch (IOException | InterruptedException e) {
                logger.error("Failed to retrieve like count for user {}, reason: {}", key, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public int analyseDataBySecUserId(String key) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tikhub.io/api/v1/douyin/app/v3/handler_user_profile?sec_user_id=" + key))
                .GET()
                .header("Authorization", "Bearer " + environment.getProperty("TIKHUB_TOKEN"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject jsonResponse = JSONObject.parseObject(response.body());
        int likePermission = jsonResponse.getJSONObject("data").getJSONObject("user").getIntValue("favorite_permission");
        int likeCount = jsonResponse.getJSONObject("data").getJSONObject("user").getIntValue("favoriting_count", 0);
        if (likePermission != LIKE_PERMISSION_PUBLIC) {
            return LIKE_PERMISSION_NOT_PUBLIC;
        } else {
            return likeCount;
        }
    }
}
