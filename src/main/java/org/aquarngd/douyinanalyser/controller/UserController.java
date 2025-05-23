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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.util.Objects;

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
                    new String[]{"userid"});
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
    public JSONObject ParseUrlToKey(@RequestParam String url) {
        if (!isValidUrl(url)) {
            return UnifiedResponse.Failed("Invalid URL");
        }
        RestTemplate restTemplate = new RestTemplate(new NoRedirectSimpleClientHttpRequestFactory());
        logger.info("URL: {}", url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.FOUND) {
            HttpHeaders headers = response.getHeaders();
            URI location = headers.getLocation();
            if (location != null) {
                String[] aftersplit = location.toString().split("\\?")[0].split("/");
                return UnifiedResponse.Success(new JSONObject().fluentPut("key", aftersplit[aftersplit.length - 1]));
            } else {
                return UnifiedResponse.Failed("Invalid URL LOCATION iS NULL");
            }
        } else {
            return UnifiedResponse.Failed("Invalid URL STATUS CODE IS NOT 302");
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/delete")
    public JSONObject DeleteUser(@RequestParam int id) {
        jdbcTemplate.update("DELETE FROM userinfo WHERE id=?", id);
        return UnifiedResponse.SuccessSignal();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/list")
    public JSONObject ListAllUsers() {
        return UnifiedResponse.Success(
                new JSONObject()
                        .fluentPut("list", jdbcTemplate.queryForList("SELECT id,name FROM userinfo")));
    }

    private boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            String path = uri.getPath();
            return Objects.equals(host, "v.douyin.com") && path.matches("[a-zA-Z0-9]*");
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
