package com.bobo.bobo_res.utils;

import com.google.gson.Gson;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: Gson工具类
 */
public class GsonUtil {
    private static Gson sGson = new Gson();

    public static Gson getGson() {
        return sGson;
    }
}
