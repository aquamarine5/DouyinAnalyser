package org.aquarngd.douyinanalyser.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.aquarngd.douyinanalyser.UnifiedResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counter")
public class LikeCounterController {

    private final JdbcTemplate jdbcTemplate;

    public LikeCounterController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
}
