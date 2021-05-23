package com.bobo.bobo_res.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.bean.Order;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.utils.T;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailActivity extends BaseActivity {

    // （产品）商品对象
    private Order mOrder;

    private ImageView mImageView;
    private TextView mTvTitle;
    private TextView mTvDesc;
    private TextView mTvPrice;
    private static final String KEY_ORDER = "key_order";

    // 自己封装跳转到本页方法
    public static void launch(Context context, Order order) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(KEY_ORDER, order);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        setUpToolbar("订单详情");

        Intent intent = getIntent();
        if (intent != null) {
            // Serializable  [ˈsɪərɪəlaɪzəbl] 序列化
            mOrder = (Order) intent.getSerializableExtra(KEY_ORDER);
        }

        if (mOrder == null) {
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
                .load(Config.baseURL + mOrder.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(mImageView);
        mTvTitle.setText(mOrder.getRestaurant().getName());
        // 用户的这个订单中都买了啥
        List<Order.ProductVo> ps = mOrder.getPs();
        if (ps != null) {
            StringBuilder sb = new StringBuilder();
            for (Order.ProductVo productVo : ps) {
                sb.append(productVo.product.getName()).append(" * ")
                .append(productVo.count)
                .append("\n");
            }
            mTvDesc.setText(sb.toString());
        }
        mTvPrice.setText("共消费" + mOrder.getPrice() + " 元");
    }
}