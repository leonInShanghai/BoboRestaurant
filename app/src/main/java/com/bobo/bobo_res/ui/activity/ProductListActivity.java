package com.bobo.bobo_res.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.bean.Order;
import com.bobo.bobo_res.bean.Product;
import com.bobo.bobo_res.biz.OrderBiz;
import com.bobo.bobo_res.biz.ProductBiz;
import com.bobo.bobo_res.net.CommonCallback;
import com.bobo.bobo_res.ui.adapter.ProductListAdapter;
import com.bobo.bobo_res.ui.view.refresh.SwipeRefresh;
import com.bobo.bobo_res.ui.view.refresh.SwipeRefreshLayout;
import com.bobo.bobo_res.ui.vo.ProductItem;
import com.bobo.bobo_res.utils.PreciseAdditionAndSubtraction;
import com.bobo.bobo_res.utils.T;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends BaseActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mTvCount;
    private Button mBtnPay;
    private RecyclerView mRecyclerView;
    private ProductListAdapter mAdapter;
    private List<ProductItem> mDatas = new ArrayList<>();

    // 订餐网络请求逻辑处理类
    private ProductBiz mProductBiz = new ProductBiz();
    // 订单(用户选择好商品了点击要支付)网络请求逻辑处理类
    private OrderBiz mOrderBiz = new OrderBiz();

    private int mCurrentPage;

    // 总价格
    private double mTotalPrice;
    // 购物车总件数
    private int mTotalCount;

    private Order mOrder = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setUpToolbar("订餐");

        initView();
        initEvent();

        // 一进来要加载一次数据
        loadDatas();
    }

    private void initEvent() {
        // 用户上拉加载更多
        mSwipeRefreshLayout.setOnPullUpRefreshListener(new SwipeRefreshLayout.OnPullUpRefreshListener() {
            @Override
            public void onPullUpRefresh() {
                loadMore();
            }
        });

        // 用户下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefresh.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 加载数据（第一页）
                loadDatas();
            }
        });

        mAdapter.setOnProductListener(new ProductListAdapter.OnProductListener() {
            @Override
            public void onProductAdd(ProductItem productItem) {
                mTotalCount++;
                mTvCount.setText("数量: " + mTotalCount);
                double price = new BigDecimal(String.valueOf(productItem.getPrice())).doubleValue();
                mTotalPrice = PreciseAdditionAndSubtraction.add(mTotalPrice, price , 2);
                mBtnPay.setText(mTotalPrice + "元 立即支付");
                mBtnPay.setEnabled(mTotalPrice> 0.0D);
                // 添加商品到订单对象
                mOrder.addProduct(productItem);
            }

            @Override
            public void onProductSub(ProductItem productItem) {
                mTotalCount--;
                mTvCount.setText("数量: " + mTotalCount);
                double price = new BigDecimal(String.valueOf(productItem.getPrice())).doubleValue();
                mTotalPrice = PreciseAdditionAndSubtraction.sub(mTotalPrice, price , 2);
                mBtnPay.setText(mTotalPrice + "元 立即支付");
                mBtnPay.setEnabled(mTotalPrice > 0.0D);
                // 从订单对象移除商品
                mOrder.removeProduct(productItem);
            }
        });

        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTotalCount <= 0) {
                    T.showToast("您还没有选择菜品~~~");
                    return;
                }

                mOrder.setCount(mTotalCount);
                mOrder.setPrice(mTotalPrice);
                mOrder.setRestaurant(mDatas.get(0).getRestaurant());


                // 开始网络请求loadding搞起来
                startLoadingProgress();

                // 用户选好商品了要支付
                mOrderBiz.add(mOrder, new CommonCallback<String>() {
                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast("请求失败");
                        Log.e("ProductListActivity", e.getMessage());
                    }

                    @Override
                    public void onSuccess(String response) {
                        stopLoadingProgress();
                        T.showToast("您已支付成功~~~");
                        finish();
                        setResult(RESULT_OK);
                    }
                });
            }
        });
    }

    // 加载数据（第一页）
    private void loadDatas() {
        // 加载动画搞起来
        startLoadingProgress();
        mProductBiz.listByPage(0, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                // 加载失败或成功后都要停止loading
                stopLoadingProgress();
                Log.e("ProductListActivity", e.getMessage());
                // 请求失败了
                T.showToast("请求失败");
                // 下拉刷新关闭
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onSuccess(List<Product> response) {
                // 加载失败或成功后都要停止loading
                stopLoadingProgress();
                // 下拉刷新关闭
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                // 只有下拉刷新有了数据 才能允许用户上来加载更多
                if (response != null && response.size() > 0) {
                    mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.BOTH);
                } else {
                    // 请求成功了但是没有数据
                    T.showToast("服务异常~~~");
                    return;
                }
                // 下拉成功后当前在第0页
                mCurrentPage = 0;
                mDatas.clear();
                for (Product product : response) {
                    mDatas.add(new ProductItem(product));
                }
                // 刷新数据
                mAdapter.notifyDataSetChanged();
                // 清空选择的数据，数量，价格
                mTotalPrice = 0.0D;
                mTotalCount = 0;
                mTvCount.setText("数量: " + mTotalCount);
                mBtnPay.setEnabled(mTotalPrice > 0.0D);
                mBtnPay.setText("0元 不可支付");
            }
        });
    }

    // 加载更多数据
    private void loadMore() {
        // 加载动画搞起来
        startLoadingProgress();
        mProductBiz.listByPage(++mCurrentPage, new CommonCallback<List<Product>>() {
            @Override
            public void onError(Exception e) {
                // 加载失败或成功后都要停止loading
                stopLoadingProgress();
                T.showToast("请求失败");
                Log.e("ProductListActivity", e.toString());
                // 请求失败了mCurrentPage要减回来
                mCurrentPage--;
                // 下拉刷新关闭
                mSwipeRefreshLayout.setPullUpRefreshing(false);
            }

            @Override
            public void onSuccess(List<Product> response) {
                // 加载失败或成功后都要停止loading
                stopLoadingProgress();
                // 下拉刷新关闭
                mSwipeRefreshLayout.setPullUpRefreshing(false);

                // 只有下拉刷新有了数据 才能允许用户上来加载更多
                if (response == null || response.size() == 0) {
                    // 请求成功了但是没有数据
                    Toast.makeText(ProductListActivity.this,"没有更多数据了~~~", Toast.LENGTH_SHORT).show();
                    // 请求成功了但是没有数据mCurrentPage要减回来
                    mCurrentPage--;
                    return;
                }
                Log.e("ProductActivity","又加载了" + response.size() + " 道菜 ");
                // 上拉加载更多不用clear
                // mDatas.clear();
                for (Product product : response) {
                    mDatas.add(new ProductItem(product));
                }
                // 刷新数据
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        mSwipeRefreshLayout = findViewById(R.id.id_swiperefresh);
        mRecyclerView = findViewById(R.id.id_recyclerview);
        mBtnPay = findViewById(R.id.id_btn_pay);
        mTvCount = findViewById(R.id.id_tv_count);

        // 设置开关刚开始只能下拉刷新，下拉刷新有数据才能上拉加载更多
        mSwipeRefreshLayout.setMode(SwipeRefresh.Mode.PULL_FROM_START);
        // 设置颜色
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.BLUE, Color.GRAY);

        mAdapter = new ProductListAdapter(this, mDatas);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        if (mProductBiz != null) {
            mProductBiz.onDestory();
            mProductBiz = null;
        }
        super.onDestroy();
    }
}