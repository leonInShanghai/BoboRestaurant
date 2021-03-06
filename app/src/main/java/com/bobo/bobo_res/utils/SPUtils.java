package com.bobo.bobo_res.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.bobo.bobo_res.ResApplication;

/**
 * Created by 公众号：IT波 on 2021/4/25 Copyright © Leon. All rights reserved.
 * Functions: 數據持久化保存工具類
 */
public class SPUtils {
    /**SharedPreferences（共享類） 的名稱*/
    private static final String SP_NAME = "bobo_res";

    /**本地持久化存儲Boolean類型*/
    public static void setBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key,value).commit();
    }

    /**獲取本地持久化存儲的Boolean類型*/
    public static boolean getBoolean(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        boolean result = sp.getBoolean(key,false);
        return result;
    }

    /**本地持久化存儲String類型*/
    public static void putString(String key, String value){
        SharedPreferences sp = ResApplication.getAppContext().getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value).commit();
    }

    /**
     * 獲取本地持久化存儲的String類型
     */
    public static String getString(String key){
        SharedPreferences sp = ResApplication.getAppContext().getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        String result = sp.getString(key,"");
        return result;
    }

    /**本地持久化存儲int類型*/
    public static void setInt(Context context,String key,int value){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key,value).commit();
    }

    /**獲取本地持久化存儲的Int類型*/
    public static int getInt(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        int result = sp.getInt(key,0);
        return result;
    }

    /**本地持久化存儲Long類型*/
    public static void setLong(Context context,String key,long value){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        //谷歌建議使用apply() 和 commit() 效果一樣 apply是異步的方法
        edit.putLong(key,value).apply();
    }

    /**獲取本地持久化存儲的Long類型*/
    public static long getLong(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        long result = sp.getLong(key,0);
        return result;
    }
}
