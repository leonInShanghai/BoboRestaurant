package com.bobo.bobo_res.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.utils.SystemUtil;


/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: app 入口 第一个activity
 */
public class SplashActivity extends AppCompatActivity {

    private Button mBtnSkip;

    // ----------------------FIXME:用户体验优化 ↓——————————————————————————————————
    // 解决用户按返回键推出后 app到首页或登录页又出现的 用户体验优化
    private boolean isBackPressed = false;
    // 解决有时候跳转到登录页两次的bug
    private boolean isToLogin = false;
    // ----------------------FIXME:用户体验优化 ↑——————————————————————————————————

    private Handler mHandler = new Handler();
    private Runnable mRunnableToLogin = new Runnable() {
        @Override
        public void run() {
            // 跳转到登录页
            toLoginActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ----------------------FIXME:用户体验优化 ↓——————————————————————————————————
        // 荣耀操作系统时即使Theme设置了FullScreen但是状态栏是那种黑色（透明）的样子难看
        // 采取了如果是荣耀手机就不要FullScreen而是显示的和其它（All off）页面风格一样红色的状态栏
        String deviceBrand = SystemUtil.getDeviceBrand();
        if (deviceBrand != null && deviceBrand.equals(SystemUtil.HONOR_SYS_ANDROID)) {
            // 设置主题一定要放在setContentView之前
            setTheme(R.style.AppTheme);
        }
        // ----------------------FIXME:用户体验优化 ↑——————————————————————————————————
        setContentView(R.layout.activity_splash);

        initView();
        initEvent();

        mHandler.postDelayed(mRunnableToLogin, 2000);
    }

    private void initEvent() {
        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFinishing()) {
                    mBtnSkip.removeCallbacks(mRunnableToLogin);
                    toLoginActivity();
                }
            }
        });
    }

    private void initView() {
        mBtnSkip = findViewById(R.id.id_btn_skip);
    }

    private void toLoginActivity() {
        // ----------------------FIXME:用户体验优化 ↓——————————————————————————————————
        // 如果用户按下返回键 不要再继续跳转,当已经跳转过了也不要再跳转。
        if (isBackPressed || isToLogin) {
            return;
        }
        // 解决有时候跳转到登录页两次的bug
        isToLogin = true;
        // ----------------------FIXME:用户体验优化 ↑——————————————————————————————————

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        super.onDestroy();
    }

    // ----------------------FIXME:用户体验优化 ↓——————————————————————————————————
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 用户按下了返回键此时返回用户的桌面不再继续其他页面跳转
        isBackPressed = true;
    }
    // ----------------------FIXME:用户体验优化 ↑——————————————————————————————————
}