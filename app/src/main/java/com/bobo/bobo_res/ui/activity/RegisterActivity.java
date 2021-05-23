package com.bobo.bobo_res.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.UserInfoHolder;
import com.bobo.bobo_res.bean.User;
import com.bobo.bobo_res.biz.UserBiz;
import com.bobo.bobo_res.net.CommonCallback;
import com.bobo.bobo_res.utils.CheckPhoneNumber;
import com.bobo.bobo_res.utils.T;

public class RegisterActivity extends BaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtRePassword;
    private Button mBtnRegister;

    private UserBiz mUserBiz = new UserBiz();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpToolbar("注册");
        initView();
        initEvent();
    }


    private void initEvent() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                String rePassword = mEtRePassword.getText().toString();

                // 用户名密码格式校验
                if (!CheckPhoneNumber.isCorrectPhoneNumber(username) ||
                        !CheckPhoneNumber.isCorrectPassworld(password)) {
                    // 格式校验工具中有提示此处不用再提示
                    // T.showToast("账号或密码不能为空");
                    return;
                }

                // 校验两次密码是否相同
                if (!password.equals(rePassword)) {
                    T.showToast("两次输入的密码不一致！");
                    return;
                }

                // 网络请求时页面上显示转圈圈(Progress)进度
                startLoadingProgress();

                mUserBiz.register(username, password, new CommonCallback<User>() {
                    @Override
                    public void onError(Exception e) {
                        // 网络请求有结果（成功和失败都算）时页面上停止显示转圈圈(Progress)进度
                        stopLoadingProgress();
                        // 请求失败了告诉用户为什么失败
                        T.showToast(e.getMessage());
                        Log.e("RegisterActivity", e.getMessage());
                    }

                    @Override
                    public void onSuccess(User response) {
                        // 网络请求有结果（成功和失败都算）时页面上停止显示转圈圈(Progress)进度
                        stopLoadingProgress();

                        // 保存用户信息
                        UserInfoHolder.getInstance().setUser(response);

                        // 注册成功-jump到登录页，并带回登录账号（手机号）和密码
                        LoginActivity.launch(RegisterActivity.this, response.getUsername(), response.getPassword());
                        finish();
                    }
                });
            }
        });
    }

    private void initView() {
        mEtUsername = findViewById(R.id.id_et_username);
        mEtPassword = findViewById(R.id.id_et_password);
        mEtRePassword = findViewById(R.id.id_et_repassword);
        mBtnRegister = findViewById(R.id.id_btn_register);
    }

    @Override
    protected void onDestroy() {
        if (mUserBiz != null) {
            mUserBiz. onDestory();
            mUserBiz = null;
        }
        super.onDestroy();
    }
}