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

@RestController
@RequestMapping("/counter")
public class LikeCounterController {

    private final JdbcTemplate jdbcTemplate;
    private final DouyinAnalyserApplication douyinAnalyserApplication;

    public LikeCounterController(JdbcTemplate jdbcTemplate, DouyinAnalyserApplication douyinAnalyserApplication) {
        this.jdbcTemplate = jdbcTemplate;
        this.douyinAnalyserApplication = douyinAnalyserApplication;
    }

    @GetMapping("/get")
    public JSONObject getLikeList(@RequestParam int id){
        SqlRowSet rowSet=jdbcTemplate.queryForRowSet("SELECT (date,likecount) FROM counts WHERE userid=? ORDER BY date DESC LIMIT 14",id);
        JSONArray likeList=new JSONArray();
        while(rowSet.next()){
            JSONObject like=new JSONObject();
            like.put("date",rowSet.getString("date"));
            like.put("count",rowSet.getInt("likecount"));
            likeList.add(like);
        }
        JSONObject result=new JSONObject();
        result.put("list",likeList);
        return UnifiedResponse.Success(result);
    }

    @GetMapping("/update")
    public JSONObject forceUpdate() throws IOException, InterruptedException {
        douyinAnalyserApplication.updateLikeCount();
        return UnifiedResponse.SuccessSignal();
    }
}
