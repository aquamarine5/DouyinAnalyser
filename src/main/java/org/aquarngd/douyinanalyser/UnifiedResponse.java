/*
 * @Author: aquamarine5 && aquamarine5_@outlook.com
 * Copyright (c) 2024 by @aquamarine5, RC. All Rights Reversed.
 */

package org.aquarngd.douyinanalyser;

import com.alibaba.fastjson2.JSONObject;

public class UnifiedResponse {
    public static JSONObject Success(JSONObject data) {
        return new JSONObject()
                .fluentPut("status", "success")
                .fluentPut("data", data);
    }
    public static JSONObject Failed(String message) {
        return new JSONObject()
                .fluentPut("status", "err")
                .fluentPut("message", message);
    }
    public static JSONObject SuccessSignal() {
        return new JSONObject()
                .fluentPut("status", "success.Signal");
    }
    public static JSONObject FailedSignal() {
        return new JSONObject()
                .fluentPut("status", "err");
    }
}
