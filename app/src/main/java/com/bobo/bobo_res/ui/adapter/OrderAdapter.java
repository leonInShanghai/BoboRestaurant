package com.bobo.bobo_res.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.bean.Order;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.ui.activity.OrderDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 公众号：IT波 on 2021/5/16 Copyright © Leon. All rights reserved.
 * Functions: 订单页RecyclerView的适配器
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder> {

    // 数据源集合
    private List<Order> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public OrderAdapter(Context context, List<Order> datas) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_order_list, parent, false);
        return new OrderItemViewHolder(itemView, mDatas);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Order order = mDatas.get(position);

        // 显示图片
        Picasso.with(mContext).load(Config.baseURL + order.getRestaurant().getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);

        if (order.getPs() != null && order.getPs().size() > 0) {
            holder.mTvLable.setText(order.getPs().get(0).product.getName() + "等" + order.getCount() + "件商品");
        } else {
            holder.mTvLable.setText("无消费");
        }

        holder.mTvName.setText(order.getRestaurant().getName());
        holder.mTvPrice.setText("共消费： " + order.getRestaurant().getPrice() + "元");
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    static class OrderItemViewHolder extends RecyclerView.ViewHolder {

        // 餐厅的图片
        private ImageView mIvImage;
        // 餐厅的名字
        private TextView mTvName;
        // 餐厅的标签
        private TextView mTvLable;
        // 餐厅人均消费价格
        private TextView mTvPrice;

        public OrderItemViewHolder(@NonNull final View itemView, final List<Order> datas) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 正规的MVC架构要写在Activity中，跳转到订单详情页
                    OrderDetailActivity.launch(itemView.getContext(), datas.get(getLayoutPosition()));
                }
            });

            mIvImage = itemView.findViewById(R.id.id_iv_image);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLable = itemView.findViewById(R.id.id_tv_lable);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);

        }
    }
}
