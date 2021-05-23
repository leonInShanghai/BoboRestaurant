package com.bobo.bobo_res.utils;

import android.text.TextUtils;

/**
 * Created by 公众号：IT波 on 2021/5/16 Copyright © Leon. All rights reserved.
 * Functions: 校验手机号密码格式是否正确
 */
public class CheckPhoneNumber {

    /**
     * 校验手机号格式是否正确
     * @param mobiles
     * @return
     */
    public static boolean isCorrectPhoneNumber(String mobiles) {
        // "[1]"代表第1位为数字1，"[3456789]"代表第二位可以为3、4、5、6、7、8、9中的一个，
        // "\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}" ;
        if (TextUtils.isEmpty(mobiles)) {
            T.showToast("请输入手机号");
            return false ;
        } else {
            if (mobiles.matches(telRegex) == false) {
                T.showToast("请输入格式正确的手机号");
            }
            return mobiles.matches( telRegex );
        }
    }

    /**
     * 校验密码格式是否正确 密码要求6位不包含空格
     * @param password
     * @return
     */
    public static boolean isCorrectPassworld(String password) {
        if (!TextUtils.isEmpty(password) && password.trim().length() == 6) {
            return true;
        }
        T.showToast("密码要求6位不包含空格");
        return false;
    }

}
