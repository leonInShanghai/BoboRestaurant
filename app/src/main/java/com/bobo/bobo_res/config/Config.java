package com.bobo.bobo_res.config;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: 全局常量类  Config:配置的意识
 */
public class Config {

    // 这是测试的url
    // public static String baseURL = "http://115.29.246.231/basePro/";

    // 这是正式的url目前能用的
    public static String baseURL = "http://restaurant.t.imooc.com/basePro/";

    /** 订单列表页用户支付成功后返回的resultCode */
    public static final int PRODUCT_LIST_RESULT_CODE = 1001;

    /** 用户退出成功后返回的resultCode */
    public static final int LOGUT_RESULT_CODE = 1002;

    /** 登录成功保存用户账号的key */
    public static final String LOGIN_SUCCESS_USER_NAME = "LOGIN_SUCCESS_USER_NAME";
    /** 登录成功保存用户密码的key */
    public static final String LOGIN_SUCCESS_USER_PASSWORD = "LOGIN_SUCCESS_USER_PASSWORD";
}
