package com.bobo.bobo_res.bean;

import java.io.Serializable;

/**
 * Created by 公众号：IT波 on 2021/5/16 Copyright © Leon. All rights reserved.
 * Functions: 餐厅对象 Restaurant：约思特阮特
 */
public class Restaurant implements Serializable {

    private int id;
    private String icon;
    private String name;
    private String desc;
    private float price; // 人均价

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
