package com.bobo.bobo_res.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.UserInfoHolder;
import com.bobo.bobo_res.bean.User;
import com.bobo.bobo_res.biz.UserBiz;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.net.CommonCallback;
import com.bobo.bobo_res.utils.CheckPhoneNumber;
import com.bobo.bobo_res.utils.SPUtils;
import com.bobo.bobo_res.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import okhttp3.CookieJar;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: 登录页
 */
public class LoginActivity extends BaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mTvRegister;

    private UserBiz mUserBiz = new UserBiz();

    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";

    private Handler mHandler = new Handler();

    private Runnable mRunnableToLogin = new Runnable() {
        @Override
        public void run() {
            // -----------FIXME:用户体验优化↓----------------------
            if (!isFinishing()) {
                // 用户上次有登录成功并且没有退出帮用户自动登录
                if (!TextUtils.isEmpty(mEtUsername.getText().toString()) && !TextUtils.isEmpty(mEtPassword.getText()
                        .toString())) {
                    mBtnLogin.performClick();
                }
            }
            // -----------FIXME:用户体验优化↑----------------------
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 清空cookie
        CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();

        // -----------FIXME:用户体验优化↓----------------------
        // 用户上次有登录成功并且没有退出帮用户自动登录
        String username = SPUtils.getString(Config.LOGIN_SUCCESS_USER_NAME);
        String password = SPUtils.getString(Config.LOGIN_SUCCESS_USER_PASSWORD);
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            mEtUsername.setText(username);
            mEtPassword.setText(password);
            T.showToast("登录失效将自动为您登录~~");
        }
        // -----------FIXME:用户体验优化↑----------------------
        mHandler.postDelayed(mRunnableToLogin, 800);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        
        initEvent();

        initIntent(getIntent());
    }

    private void initEvent() {

        /**
         * 登录按钮点击事件处理
         * 测试账号： 16666666666 密码：123456
         */
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = mEtUsername.getText().toString();
                final String password = mEtPassword.getText().toString();

                // 用户名密码格式校验 zhy abc
                if (!CheckPhoneNumber.isCorrectPhoneNumber(username) ||
                        !CheckPhoneNumber.isCorrectPassworld(password)) {
                    // 格式校验工具中有提示此处不用再提示
                    // T.showToast("账号或密码不能为空");
                    return;
                }

                // 网络请求时页面上显示转圈圈(Progress)进度
                startLoadingProgress();

                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        // 网络请求有结果（成功和失败都算）时页面上停止显示转圈圈(Progress)进度
                        stopLoadingProgress();
                        // 请求失败了告诉用户为什么失败
                        T.showToast(e.getMessage());
                        Log.e("LoginActivity", e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        // 网络请求有结果（成功和失败都算）时页面上停止显示转圈圈(Progress)进度
                        stopLoadingProgress();

                        // 保存用户信息
                        UserInfoHolder.getInstance().setUser(response);

                        // 登录成功-进入到购物（Order:订单）页
                        toOrderActivity();

                        // -----------FIXME:用户体验优化↓----------------------
                        // 用户登录成功后账号密码做持久化保存帮助用户下次自动登录（最好自己加密解密一下）
                        SPUtils.putString(Config.LOGIN_SUCCESS_USER_NAME, username);
                        SPUtils.putString(Config.LOGIN_SUCCESS_USER_PASSWORD, password);
                        // -----------FIXME:用户体验优化↑----------------------
                    }
                });
            }
        });
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterActivity();
            }
        });
    }

    /**
     * 如果ActivityA在栈顶,且现在要再启动ActivityA，这时会调用onNewIntent()方法 ，生命周期顺序为：
     * onCreate--->onStart--->onResume---onPause--->onNewIntent--->onResume
     *
     * 当ActivityA的LaunchMode为SingleInstance,SingleTask：
     * 如果ActivityA已经在任务栈中，再次启动ActivityA，那么此时会调用onNewIntent()方法，生命周期调用顺序为：
     * onPause--->跳转其它页面--->onCreate--->onStart--->onResume---onPause--->跳转A--->onNewIntent--->onRestart--->
     * onStart--->onResume
     *
     * 更准确的说法是，只对SingleTop(且位于栈顶)，SingleTask和SingleInstance(且已经在任务栈中存在实例)的情况下，
     * 再次启动它们时才会调用，即只对startActivity有效，对仅仅从后台切换到前台而不再次启动的情形，不会触发onNewIntent
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    /**
     * （用户注册成功）从注册页跳转过来 携带的(手机号)账号，密码 直接帮用户显示上
     * @param intent
     */
    private void initIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        String username = intent.getStringExtra(KEY_USERNAME);
        String password = intent.getStringExtra(KEY_PASSWORD);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            // TODO: 本地持久化保存用户账号密码自动登录
            return;
        }

        mEtUsername.setText(username);
        // 将光标移至文字末尾
        mEtUsername.setSelection(username.length());
        mEtPassword.setText(password);
        // 将光标移至文字末尾
        mEtPassword.setSelection(password.length());
    }

    /**
     * 跳转到注册页面
     */
    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到订单页
     */
    private void toOrderActivity() {
        Intent intent = new Intent(this, OrderActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
        mEtUsername = findViewById(R.id.id_et_username);
        mEtPassword = findViewById(R.id.id_et_password);
        mBtnLogin = findViewById(R.id.id_btn_login);
        mTvRegister = findViewById(R.id.id_tv_register);
    }

    /**
     * RegisterActivity 跳转到 （当前页）LoginActivity的方法，写的有些怪 直接finish()不就好了
     * @param context
     * @param username
     * @param password
     */
    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(KEY_USERNAME, username);
        // 避免有两个LoginActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (mUserBiz != null) {
            mUserBiz. onDestory();
            mUserBiz = null;
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }
}