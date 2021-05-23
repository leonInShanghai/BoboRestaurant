package com.bobo.bobo_res.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.UserInfoHolder;
import com.bobo.bobo_res.bean.User;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.utils.SPUtils;

public class LoginOutActivity extends BaseActivity {

    private Button mBtnLogout;
    private TextView mTvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_out);

        initView();
        initEvent();
    }

    private void initView() {
        mBtnLogout = findViewById(R.id.id_btn_logout);
        mTvUsername = findViewById(R.id.id_tv_name);

        User user = UserInfoHolder.getInstance().getUser();
        if (user != null) {
            mTvUsername.setText(user.getUsername());
        }
    }

    private void initEvent() {
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 清空本地保存的账号密码然后 finish();
                SPUtils.putString(Config.LOGIN_SUCCESS_USER_NAME, "");
                SPUtils.putString(Config.LOGIN_SUCCESS_USER_PASSWORD, "");
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}