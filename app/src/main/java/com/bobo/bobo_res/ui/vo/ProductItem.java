package com.bobo.bobo_res.ui.vo;

import com.bobo.bobo_res.bean.Product;

/**
 * Created by 公众号：IT波 on 2021/5/22 Copyright © Leon. All rights reserved.
 * Functions: 订单页为用户选择的购物车商品增加的bean类
 */
public class ProductItem extends Product {

    public int count;

    public ProductItem(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.label = product.getLabel();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.icon = product.getIcon();
        this.restaurant = product.getRestaurant();
    }

}
