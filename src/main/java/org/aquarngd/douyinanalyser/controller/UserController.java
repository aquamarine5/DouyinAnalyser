package org.aquarngd.douyinanalyser.controller;

import com.alibaba.fastjson2.JSONObject;
import org.aquarngd.douyinanalyser.UnifiedResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final JdbcTemplate jdbcTemplate;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/add")
    public JSONObject AddUser(@RequestParam String username, @RequestParam String key){
        jdbcTemplate.update("INSERT INTO userinfo (`name`, `key`) VALUES (?, ?)", username, key);
        return UnifiedResponse.SuccessSignal();
    }
}
