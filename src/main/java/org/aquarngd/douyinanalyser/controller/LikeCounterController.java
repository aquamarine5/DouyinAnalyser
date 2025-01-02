/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */

package org.aquarngd.douyinanalyser.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.aquarngd.douyinanalyser.DouyinAnalyserApplication;
import org.aquarngd.douyinanalyser.DouyinLikeAnalyser;
import org.aquarngd.douyinanalyser.UnifiedResponse;
import org.springframework.core.env.Environment;
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
    private final DouyinLikeAnalyser douyinLikeAnalyser;

    public LikeCounterController(JdbcTemplate jdbcTemplate, DouyinAnalyserApplication douyinAnalyserApplication, DouyinLikeAnalyser douyinLikeAnalyser) {
        this.jdbcTemplate = jdbcTemplate;
        this.douyinAnalyserApplication = douyinAnalyserApplication;
        this.douyinLikeAnalyser = douyinLikeAnalyser;
    }

    @GetMapping("/query")
    public JSONObject getLikeList(@RequestParam int id) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT date,likecount FROM counts WHERE userid=? ORDER BY date LIMIT 90", id);
        JSONArray likeList = new JSONArray();
        while (rowSet.next()) {
            JSONObject like = new JSONObject();
            like.put("date", rowSet.getInt("date"));
            like.put("count", rowSet.getInt("likecount"));
            likeList.add(like);
        }
        SqlRowSet userInfo = jdbcTemplate.queryForRowSet("SELECT `name`,isLikePublic FROM userinfo WHERE `id`=?", id);
        JSONObject result = new JSONObject();
        if (userInfo.next()) {
            result.put("permission",userInfo.getBoolean("isLikePublic"));
            result.put("name", userInfo.getString("name"));
        }
        result.put("list", likeList);
        return UnifiedResponse.Success(result);
    }

    @GetMapping("/update")
    public JSONObject forceUpdate() {
        JSONArray result = new JSONArray();
        douyinLikeAnalyser.analyseAllData((id, likeCount) -> {
            String numberDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
            jdbcTemplate.update("INSERT INTO `counts` (date, userid, likecount) VALUES (?, ?, ?) AS newvalue ON DUPLICATE KEY UPDATE likecount = newvalue.likecount",
                    numberDate, id, likeCount);
            result.add(new JSONObject()
                    .fluentPut("userid", id)
                    .fluentPut("likeCount", likeCount)
            );
        });
        return UnifiedResponse.Success(result);
    }

    @GetMapping("/update_signal")
    public JSONObject forceUpdateWithoutResponse() throws IOException, InterruptedException {
        douyinAnalyserApplication.updateLikeCount();
        return UnifiedResponse.SuccessSignal();
    }
}
