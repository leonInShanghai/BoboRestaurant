package com.bobo.bobo_res.utils;

import android.content.Context;
import android.text.TextUtils;

import com.bobo.bobo_res.ResApplication;


/**
 * Created by 公众号：IT波 on 2021/4/25 Copyright © Leon. All rights reserved.
 * Functions: 判断是否是小米、华为、魅族系统
 * 当华为操作系统时AlertDialog的内边距要加上状态栏的高度 不然它（AlertDialog）不居中
 */
public class SystemUtil {

    private static final String TAG = "SystemUtil";

    // 荣耀厂商
    public static final String HONOR_SYS_ANDROID = "HONOR";

    /**
     * 获取手机厂商
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        if (!TextUtils.isEmpty(SPUtils.getString(TAG))) {
            return SPUtils.getString(TAG);
        }
        SPUtils.putString(TAG, android.os.Build.BRAND);
        return android.os.Build.BRAND;
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
