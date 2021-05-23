package com.bobo.bobo_res;

import android.app.Application;
import android.content.Context;

import com.bobo.bobo_res.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: 用于获取全局上下文
 */
public class ResApplication extends Application {

    // 全局上下文
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化自定义toast
        T.init(this);

        // 设置cookie
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        appContext = getBaseContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

}
