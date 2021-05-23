package com.bobo.bobo_res.biz;

import com.bobo.bobo_res.bean.User;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: 用户登录 注册业务逻辑处理
 */
public class UserBiz {

    /**
     * 用户登录网络请求逻辑处理
     * @param username
     * @param password
     * @param commonCallback
     */
    public void login(String username, String password, CommonCallback<User> commonCallback) {
        OkHttpUtils
                .post()
                .url(Config.baseURL + "user_login")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);
    }

    public void register(String username, String password, CommonCallback<User> commonCallback) {
        OkHttpUtils
                .post()
                .url(Config.baseURL + "user_register")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);
    }

    /**
     * 用于取消请求
     */
    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }

}
