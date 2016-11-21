package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.RequestParam;
import com.feicuiedu.eshop.network.ResponseEntity;
import com.feicuiedu.eshop.network.entity.CategoryPrimary;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 服务器接口: 获取商品分类.
 */
public class ApiCategory implements ApiInterface<ApiCategory.Rsp> {

    @NonNull
    @Override public String getPath() {
        return "/category";
    }

    @Nullable
    @Override public RequestParam getRequestParam() {
        return null;
    }

    @NonNull
    @Override public Class<Rsp> getResponseType() {
        return Rsp.class;
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data")
        private List<CategoryPrimary> mData;

        public List<CategoryPrimary> getData() {
            return mData;
        }
    }
}
