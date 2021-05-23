package com.bobo.bobo_res.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.bean.Product;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.utils.T;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by 公众号：IT波 on 2021/5/23 Copyright © Leon. All rights reserved.
 * Functions: 商品详情页
 */
public class ProductDetialActivity extends BaseActivity {

    // （产品）商品对象
    private Product mProduct;

    private ImageView mImageView;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvPrice;
    private static final String KEY_PRODUCT = "key_product";

    // 自己封装跳转到本页方法
    public static void launch(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetialActivity.class);
        intent.putExtra(KEY_PRODUCT, product);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detial);

        setUpToolbar("商品详情");

        Intent intent = getIntent();
        if (intent != null) {
            // Serializable  [ˈsɪərɪəlaɪzəbl] 序列化
            mProduct = (Product) intent.getSerializableExtra(KEY_PRODUCT);
        }

        if (mProduct == null) {
            T.showToast("参数传递错误!");
            finish();
            return;
        }

        initView();
    }

    private void initView() {
        mImageView = findViewById(R.id.id_iv_image);
        mTvTitle = findViewById(R.id.id_tv_title);
        mTvDesc = findViewById(R.id.id_tv_desc);
        mTvPrice = findViewById(R.id.id_tv_price);

        Picasso.with(this)
                .load(Config.baseURL + mProduct.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mImageView);
        mTvTitle.setText(mProduct.getName());
        mTvDesc.setText(mProduct.getDescription());
        mTvPrice.setText("￥" + mProduct.getPrice() + " 元/份");
    }
}