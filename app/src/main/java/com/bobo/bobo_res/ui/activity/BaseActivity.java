package com.bobo.bobo_res.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.ui.view.AlertContentLoadingView;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions: 用于抽取冗余代码的父类activity
 */
public class BaseActivity extends AppCompatActivity {

    // 网络加载时显示的加载转圈圈对话框
    // private ProgressDialog mLoadingDialog;

    // loading弹窗，上面注释掉的太难看了
    private AlertDialog mAlterDiaglog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为黑色状态栏
        // if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //     getWindow().setStatusBarColor(0xff000000);
        // }

        // 注释原因这个loading加载框能用就是太难看了
        // mLoadingDialog = new ProgressDialog(this);
        // mLoadingDialog.setMessage("加载中...");
    }

    /**
     * 网络请求有结果（成功和失败都算）时页面上停止显示转圈圈(Progress)进度
     */
    protected void stopLoadingProgress() {
        // 注释原因这个loading加载框能用就是太难看了
        // if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
        //     mLoadingDialog.dismiss();
        // }
        if (mAlterDiaglog != null && mAlterDiaglog.isShowing()) {
            mAlterDiaglog.dismiss();
        }
    }

    /**
     * 网络请求时页面上显示转圈圈(Progress)进度
     */
    protected void startLoadingProgress() {
        // 注释原因这个loading加载框能用就是太难看了
        // if (!mLoadingDialog.isShowing()) {
        //     mLoadingDialog.show();
        // }

        // 2021-5-23logding弹窗 美化 ✌
        if (mAlterDiaglog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog)
                    .setCancelable(false)
                    .setView(new AlertContentLoadingView(this));
            mAlterDiaglog = builder.show();
        } else {
            if (!mAlterDiaglog.isShowing()) {
                mAlterDiaglog.show();
            }
        }
    }

    /**
     * 设置标题
     */
    @SuppressLint("NewApi")
    protected void setUpToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.id_toolbar);
        toolbar.setTitle(title);
        // 用户点击了左上角的返回按钮
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 返回上一页 类似 finish();
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        // 注释原因这个loading加载框能用就是太难看了
        // if (mLoadingDialog != null) {
        //     stopLoadingProgress();
        //     mLoadingDialog = null;
        // }
        if (mAlterDiaglog != null) {
            stopLoadingProgress();
            mAlterDiaglog = null;
        }
        super.onDestroy();
    }

    protected void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        // 避免有两个LoginActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
