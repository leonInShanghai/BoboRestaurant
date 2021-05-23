package com.bobo.bobo_res.biz;

import com.bobo.bobo_res.bean.Order;
import com.bobo.bobo_res.bean.Product;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by 公众号：IT波 on 2021/5/16 Copyright © Leon. All rights reserved.
 * Functions: 订单页网络请求业务处理
 */
public class OrderBiz {

    public void listByPage(int currentPage, CommonCallback<List<Order>> callback) {
        OkHttpUtils
                .post()
                .url(Config.baseURL + "order_find")
                .tag(this)
                .addParams("currentPage", currentPage + "")
                .build()
                .execute(callback);
    }

    public void add(Order order, CommonCallback callback) {

        Map<Product, Integer> productsMap = order.productsMap;
        StringBuilder sb = new StringBuilder();
        for (Product p : productsMap.keySet()) {
            sb.append(p.getId() + "_" + productsMap.get(p));
            sb.append("|");
        }
        sb = sb.deleteCharAt(sb.length() - 1);

        OkHttpUtils
                .post()
                .url(Config.baseURL + "order_add")
                .addParams("res_id", order.getRestaurant().getId() + "")
                .addParams("product_str", sb.toString())
                .addParams("count", order.getCount() + "")
                .addParams("price", order.getPrice() + "")
                .tag(this)
                .build()
                .execute(callback);
    }

    /**
     * 用于取消请求
     */
    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
