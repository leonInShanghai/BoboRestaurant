package com.bobo.bobo_res.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.ResApplication;
import com.bobo.bobo_res.utils.SizeUtils;
import com.bobo.bobo_res.utils.SystemUtil;


/**
 * Created by 公众号：IT波 on 2021/1/24 Copyright © Leon. All rights reserved.
 * Functions: 自定义加载view
 */
public class AlertContentLoadingView extends LinearLayout {

    public AlertContentLoadingView(Context context) {
        this(context, null);
    }

    public AlertContentLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlertContentLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // viewGrep加载布局不要有返回值反例：
        // LayoutInflater inflater LayoutInflater.from(context).inflate(R.layout.content_loading_view, this);
        LayoutInflater.from(context).inflate(R.layout.content_loading_view, this);

        // topPadding是为了加上状态栏的高度使得从视觉上和其他地方的loadding在同一位置
        // 荣耀操作系统时AlertDialog的内边距要加上状态栏的高度 不然它（AlertDialog）不居中
        String deviceBrand = SystemUtil.getDeviceBrand();
        if (deviceBrand != null && deviceBrand.equals(SystemUtil.HONOR_SYS_ANDROID)) {
            int statusBarHeight =  SizeUtils.px2dip(ResApplication.getAppContext(),
                    SystemUtil.getStatusBarHeight(context));
            Log.e("AlertContentLoadingView", "StatusBarHeight: " + statusBarHeight);
            this.setPadding(0, statusBarHeight, 0, 0);
        }
    }
}
