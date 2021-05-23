package com.bobo.bobo_res.biz;

import com.bobo.bobo_res.bean.Product;
import com.bobo.bobo_res.config.Config;
import com.bobo.bobo_res.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

/**
 * Created by 公众号：IT波 on 2021/5/22 Copyright © Leon. All rights reserved.
 * Functions: 订餐页网络请求逻辑
 */
public class ProductBiz {

    public void listByPage(int currentPage, CommonCallback<List<Product>> commonCallback) {

        OkHttpUtils.post()
                .url(Config.baseURL + "product_find")
                .addParams("currentPage", currentPage+"")
                .tag(this)
                .build()
                .execute(commonCallback);
    }

    /**
     * 用于取消请求
     */
    public void onDestory() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
