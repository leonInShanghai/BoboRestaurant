package com.bobo.bobo_res.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bobo.bobo_res.R;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.ui.activity.ProductDetialActivity;
import com.bobo.bobo_res.ui.vo.ProductItem;
import com.bobo.bobo_res.utils.T;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by 公众号：IT波 on 2021/5/22 Copyright © Leon. All rights reserved.
 * Functions:
 */
public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListItemViewHolder> {

    private Context mContext;
    private List<ProductItem> mProductItems;
    private LayoutInflater mInflater;

    // 点击事件监听
    private OnProductListener mOnProductListener;

    public ProductListAdapter(Context context, List<ProductItem> productItems) {
        mContext = context;
        mProductItems = productItems;
        mInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ProductListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_product_list, parent, false);
        return new ProductListItemViewHolder(itemView, mProductItems, mOnProductListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListItemViewHolder holder, int position) {
        ProductItem productItem = mProductItems.get(position);
        // 设置商品图片
        Picasso.with(mContext)
                .load(Config.baseURL + productItem.getIcon())
                .placeholder(R.drawable.pictures_no)
                .into(holder.mIvImage);
        holder.mTvName.setText(productItem.getName());
        holder.mTvCount.setText(String.valueOf(productItem.count));
        holder.mTvLable.setText(productItem.getLabel());
        holder.mTvPrice.setText(productItem.getPrice() + "元/份");
    }

    @Override
    public int getItemCount() {
        return mProductItems == null ? 0 : mProductItems.size();
    }

    public interface OnProductListener {
        // 用户添加了商品
        void onProductAdd(ProductItem productItem);
        // 用户减少了商品
        void onProductSub(ProductItem productItem);
    }

    /**
     * 供外界使用点击事件监听接口
     * @param productListener
     */
    public void setOnProductListener(OnProductListener productListener) {
        mOnProductListener = productListener;
    }

    static class ProductListItemViewHolder extends RecyclerView.ViewHolder {

        // 餐厅的图片
        private ImageView mIvImage;
        // 餐厅的名字
        private TextView mTvName;
        // 餐厅的标签
        private TextView mTvLable;
        // 餐厅人均消费价格
        private TextView mTvPrice;
        // 购物车加
        private ImageView mIvAdd;
        // 购物车减号
        private ImageView mIvSub;
        // 购物车数量
        private TextView mTvCount;

        public ProductListItemViewHolder(@NonNull final View itemView, final List<ProductItem> productItems,
                                         final OnProductListener productListener) {
            super(itemView);
            mIvImage = itemView.findViewById(R.id.id_iv_image);
            mTvName = itemView.findViewById(R.id.id_tv_name);
            mTvLable = itemView.findViewById(R.id.id_tv_lable);
            mTvPrice = itemView.findViewById(R.id.id_tv_price);
            mIvAdd = itemView.findViewById(R.id.id_iv_add);
            mIvSub = itemView.findViewById(R.id.id_iv_sub);
            mTvCount = itemView.findViewById(R.id.id_tv_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // FIXME:跳转到商品详情页，标准的MVC架构要写在Activity中
                    ProductDetialActivity.launch(itemView.getContext(), productItems.get(getLayoutPosition()));
                }
            });

            mIvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = productItems.get(pos);
                    productItem.count += 1;
                    mTvCount.setText(String.valueOf(productItem.count));
                    // 回调到Activity
                    if (productListener != null) {
                        productListener.onProductAdd(productItem);
                    }
                }
            });

            mIvSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    ProductItem productItem = productItems.get(pos);
                    if (productItem.count <= 0) {
                        Toast.makeText(itemView.getContext(), "不能减了现在没有商品", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    productItem.count -= 1;
                    mTvCount.setText(String.valueOf(productItem.count));
                    // 回调到Activity
                    if (productListener != null) {
                        productListener.onProductSub(productItem);
                    }
                }
            });
        }
    }
}
