/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */

package org.aquarngd.douyinanalyser.controller;

import com.alibaba.fastjson2.JSONObject;
import org.aquarngd.douyinanalyser.NoRedirectSimpleClientHttpRequestFactory;
import org.aquarngd.douyinanalyser.UnifiedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.sql.PreparedStatement;

@RestController
@RequestMapping("/user")
public class UserController {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    public UserController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/add")
    public JSONObject AddUser(@RequestParam String username, @RequestParam String key) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO userinfo (`name`, `key`) VALUES (?, ?) AS newvalues ON DUPLICATE KEY UPDATE `name` = newvalues.name",
                    new String[] { "userid" }
            );
            ps.setString(1, username);
            ps.setString(2, key);
            return ps;
        }, keyHolder);

        Number userId = keyHolder.getKey();
        if (userId != null) {
            return UnifiedResponse.Success(new JSONObject().fluentPut("userid", userId.intValue()));
        } else {
            return UnifiedResponse.Failed("Failed to retrieve user ID");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/parse")
    public JSONObject ParseUrlToKey(@RequestParam String url){
        RestTemplate restTemplate = new RestTemplate(new NoRedirectSimpleClientHttpRequestFactory());
        logger.info("URL: {}", url);
        ResponseEntity<String> response=restTemplate.getForEntity(url,String.class);
        if(response.getStatusCode()== HttpStatus.FOUND){
            HttpHeaders headers=response.getHeaders();
            URI location=headers.getLocation();
            if(location!=null){
                String[] aftersplit=location.toString().split("\\?")[0].split("/");
                return UnifiedResponse.Success(new JSONObject().fluentPut("key",aftersplit[aftersplit.length-1]));
            }
            else{
                return UnifiedResponse.Failed("Invalid URL LOCATION iS NULL");
            }
        }else{
            return UnifiedResponse.Failed("Invalid URL STATUS CODE IS NOT 302");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public JSONObject ListAllUsers(){
        return UnifiedResponse.Success(new JSONObject().fluentPut("list",jdbcTemplate.queryForList("SELECT id,name FROM userinfo")));
    }
}
