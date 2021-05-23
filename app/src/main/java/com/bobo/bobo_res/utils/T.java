package com.bobo.bobo_res.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: 自定义 toast类, 缺点是有时候用户连续操作需要连续提示的时候它做不到的
 */
public class T {
    private static Toast sToast;

    public static void showToast(String content) {
        sToast.setText(content);
        sToast.show();
    }

    public static void init(Context context) {
        sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }
}
