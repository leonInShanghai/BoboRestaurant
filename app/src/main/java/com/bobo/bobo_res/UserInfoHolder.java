package com.bobo.bobo_res;


import android.text.TextUtils;

import com.bobo.bobo_res.bean.User;
import com.bobo.bobo_res.utils.SPUtils;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: 用户信息类
 */
public class UserInfoHolder {

    private static UserInfoHolder sUserInfoHolder = new UserInfoHolder();
    private User mUser;

    private static final String KEY_USERNAME = "key_username";

    private UserInfoHolder() {

    }

    public static UserInfoHolder getInstance() {
        return sUserInfoHolder;
    }

    public void setUser(User user) {
        mUser = user;

        if (user != null) {
            SPUtils.putString(KEY_USERNAME, user.getUsername());
        }
    }

    public User getUser() {
        User u = mUser;
        if (u == null) {
            String username = SPUtils.getString(KEY_USERNAME);
            if (!TextUtils.isEmpty(username)) {
                u = new User();
                u.setUsername(username);
            }
        }
        mUser = u;
        return u;
    }
}
