package com.bobo.bobo_res.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.UserInfoHolder;
import com.bobo.bobo_res.bean.Order;
import com.bobo.bobo_res.bean.User;
import com.bobo.bobo_res.biz.OrderBiz;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.net.CommonCallback;
import com.bobo.bobo_res.ui.adapter.OrderAdapter;
import com.bobo.bobo_res.ui.view.CircleTransform;
import com.bobo.bobo_res.ui.view.refresh.SwipeRefresh;
import com.bobo.bobo_res.ui.view.refresh.SwipeRefreshLayout;
import com.bobo.bobo_res.utils.T;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 公众号：IT波 on 2021/5/16 Copyright © Leon. All rights reserved.
 * Functions:订单页
 */
public class OrderActivity extends BaseActivity {
    
    private Button mBtnTakeOrder;
    private TextView mTvUsername;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mIvIcon;
    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    
    private List<Order> mDatas = new ArrayList<>();
    private OrderBiz mOrderBiz = new OrderBiz();
    private int mCurrentPage = 0;

    // -----------FIXME:用户体验优化↓----------------------
    // 当用户按下返回键时发送一个广播OrderActivity 收到广播后自己finish()
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };
    // -----------FIXME:用户体验优化↑----------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initView();
        initEvents();

        // -----------FIXME:用户体验优化↓----------------------
        // 当用户按下返回键时发送一个广播OrderActivity 收到广播后自己finish()
        registerReceiver(mBroadcastReceiver, new IntentFilter(Config.FINISH_ORDER_ACTIVITY));
        // -----------FIXME:用户体验优化↑----------------------
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadDatas();
    }

    private void loadDatas() {
        // 加载中的提示搞起来！
        startLoadingProgress();
        mOrderBiz.listByPage(0, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast(e.getMessage());
                // 下拉刷新关闭
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (e.getMessage() != null && e.getMessage().contains("用户未登录")) {
                    toLoginActivity();
                }
            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                // 重置页码从0开始
                mCurrentPage = 0;
                mDatas.clear();
                mDatas.addAll(response);
                // 只有下拉刷新有了数据 才能允许用户上来加载更多
                if (mDatas.size() > 0) {
                    mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
                } else {
                    T.showToast("您没有任何订单~~~");
                }
                // 刷新RecyclerView
                mAdapter.notifyDataSetChanged();
                // 下拉刷新关闭
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void initEvents() {
        // 下拉刷新被用户拉了
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 加载数据
                loadDatas();
            }
        });

        // 上拉加载更多被用户拉了
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                // 加载更多
                loadMore();
            }
        });

        // 右上角的点餐按钮被点击了
        mBtnTakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toProductListActivity();
            }
        });

        // 用户点击了自己的头像跳转到退出页面
        mIvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, LoginOutActivity.class);
                startActivityForResult(intent, Config.LOGUT_RESULT_CODE);
            }
        });
    }

    private void toProductListActivity() {
        Intent intent = new Intent(OrderActivity.this, ProductListActivity.class);
        startActivityForResult(intent, Config.PRODUCT_LIST_RESULT_CODE);
    }

    private void initView() {

        mBtnTakeOrder = findViewById(R.id.id_btn_take_order);
        mIvIcon = findViewById(R.id.id_iv_icon);
        mTvUsername = findViewById(R.id.id_tv_name);
        mRecyclerView = findViewById(R.id.id_recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.id_refresh_layout);

        User user = UserInfoHolder.getInstance().getUser();
        if (user != null) {
            mTvUsername.setText(user.getUsername());
        } else {
            toLoginActivity();
            return;
        }

        // 设置开关刚开始只能下拉刷新，下拉刷新有数据才能上拉加载更多
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.PULL_FROM_START);
        // 设置颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.BLUE, Color.GRAY);

        mAdapter = new OrderAdapter(this, mDatas);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        Picasso.with(this).load(R.drawable.icon).placeholder(R.drawable.pictures_no).transform(new CircleTransform())
                .into(mIvIcon);

    }

    /**
     * 用户按下返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                // 像腾讯QQ微信那样直接显示app的内容页，不先显示Splash → 登录 → 再显示内容页
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                return true;
            } catch (Exception e) {
                // ignore ：代码会自动往下执行的
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 上一页finsh 携带Result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Config.PRODUCT_LIST_RESULT_CODE) {
            // 上一页支付成功返回此时要刷新数据，显示用户刚刚买的订单 onResume里做了这里可以注释掉不用重复加载
            // loadDatas();
        } else if (resultCode == RESULT_OK && requestCode == Config.LOGUT_RESULT_CODE) {
            // 跳转到登录页，并finish掉当前页
            toLoginActivity();
            finish();
        }
    }

    /**
     * 上拉加载更多
     */
    private void loadMore() {
        // 加载中的提示搞起来！
        startLoadingProgress();
        mOrderBiz.listByPage(++mCurrentPage, new CommonCallback<List<Order>>() {
            @Override
            public void onError(Exception e) {
                stopLoadingProgress();
                T.showToast("请求失败");
                Log.e("OrderActivity", e.toString());
                mSwipeRefreshLayout.setPullUpRefreshing(false);
                mCurrentPage--;

                String message = e.getMessage();
                if (message != null && message.contains("用户未登录")) {
                    toLoginActivity();
                }

            }

            @Override
            public void onSuccess(List<Order> response) {
                stopLoadingProgress();
                if (response.size() == 0) {
                    mCurrentPage--;
                    Toast.makeText(OrderActivity.this, "木有更多历史订单啦~~~", Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setPullUpRefreshing(false);
                    return;
                }
                Toast.makeText(OrderActivity.this, "更新订单成功~~~", Toast.LENGTH_SHORT).show();
                mDatas.addAll(response);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mOrderBiz != null) {
            mOrderBiz.onDestory();
            mOrderBiz = null;
        }
        // 广播要解除注册避免内存泄漏
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        super.onDestroy();
    }
}