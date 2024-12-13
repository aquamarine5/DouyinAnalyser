/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */

package org.aquarngd.douyinanalyser.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.aquarngd.douyinanalyser.DouyinAnalyserApplication;
import org.aquarngd.douyinanalyser.UnifiedResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/counter")
public class LikeCounterController {

    private final JdbcTemplate jdbcTemplate;
    private final DouyinAnalyserApplication douyinAnalyserApplication;

    public LikeCounterController(JdbcTemplate jdbcTemplate, DouyinAnalyserApplication douyinAnalyserApplication) {
        this.jdbcTemplate = jdbcTemplate;
        this.douyinAnalyserApplication = douyinAnalyserApplication;
    }

    @GetMapping("/query")
    public JSONObject getLikeList(@RequestParam int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT date,likecount FROM counts WHERE userid=? ORDER BY date LIMIT 30", id);
        JSONArray likeList = new JSONArray();
        while (rowSet.next()) {
            JSONObject like = new JSONObject();
            like.put("date", rowSet.getInt("date"));
            like.put("count", rowSet.getInt("likecount"));
            likeList.add(like);
        }
        SqlRowSet userInfo = jdbcTemplate.queryForRowSet("SELECT `name` FROM userinfo WHERE `id`=?", id);
        JSONObject result = new JSONObject();
        if (userInfo.next()) {
            result.put("name", userInfo.getString("name"));
        }
        result.put("list", likeList);
        return UnifiedResponse.Success(result);
    }

    @GetMapping("/update")
    public JSONObject forceUpdate() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        SqlRowSet userlist = jdbcTemplate.queryForRowSet("SELECT `key`, `id` FROM userinfo");
        JSONArray result = new JSONArray();
        while (userlist.next()) {
            String key = userlist.getString("key");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:1132/api/douyin/web/handler_user_profile?sec_user_id=" + key))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonResponse = JSONObject.parseObject(response.body());
            String numberDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            int likecount=jsonResponse.getJSONObject("data").getJSONObject("user").getIntValue("favoriting_count");
            if (likecount == 0) {
                result.add(new JSONObject().fluentPut("id", userlist.getInt("id")).fluentPut("status", "failed with 0 like count"));
            } else if (likecount == -1) {
                result.add(new JSONObject().fluentPut("id", userlist.getInt("id")).fluentPut("status", "like list is private"));
            } else {
                jdbcTemplate.update("INSERT INTO `counts` (date, userid, likecount) VALUES (?, ?, ?) AS newvalue ON DUPLICATE KEY UPDATE likecount = newvalue.likecount",
                        numberDate, userlist.getInt("id"), likecount);
                result.add(new JSONObject().fluentPut("id", userlist.getInt("id")).fluentPut("status", "success"));
            }
        }
        httpClient.close();
        return UnifiedResponse.Success(result);
    }
}
