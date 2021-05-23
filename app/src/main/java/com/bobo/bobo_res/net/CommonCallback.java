package com.bobo.bobo_res.net;


import com.bobo.bobo_res.utils.GsonUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by 公众号：IT波 on 2021/5/15 Copyright © Leon. All rights reserved.
 * Functions:
 */
public abstract class CommonCallback<T> extends StringCallback {

    private Type mType;

    public CommonCallback() {
        Class<? extends CommonCallback> clazz = getClass();
        Type genericSuperclass = clazz.getGenericSuperclass();

        if (genericSuperclass instanceof  Class) {
            throw new RuntimeException("CommonCallback: Miss Type Params - 你没有写泛型");
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        mType = parameterizedType.getActualTypeArguments()[0];
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onError(e);
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject resp = new JSONObject(response);
            int resultCode = resp.getInt("resultCode");

            if (resultCode == 1) {
                // 请求成功
                String data = resp.getString("data");
                Gson gson = GsonUtil.getGson();
                onSuccess((T) gson.fromJson(data, mType));
            } else {
                // 请求失败
                onError(new RuntimeException(resp.getString("resultMessage")));
            }
        } catch (JSONException e) {
            // e.printStackTrace();
            onError(e);
        }
    }

    public abstract void onError(Exception e);

    public abstract void onSuccess(T response);
}
