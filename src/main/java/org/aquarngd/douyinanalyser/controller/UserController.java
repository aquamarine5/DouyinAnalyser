package org.aquarngd.douyinanalyser.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {
    @CrossOrigin(origins = "*")
    @PostMapping("/add")
    public JSONObject AddUser(@RequestParam String username, @RequestParam String key){
        JSONObject response = new JSONObject();
        response.put("status", "success");
        return response;
    }
}
