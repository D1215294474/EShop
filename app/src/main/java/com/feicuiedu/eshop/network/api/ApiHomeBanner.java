package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.RequestParam;
import com.feicuiedu.eshop.network.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Banner;
import com.feicuiedu.eshop.network.entity.SimpleGoods;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 服务器接口: 轮播图和促销商品.
 */
public class ApiHomeBanner implements ApiInterface {


    @NonNull @Override public String getPath() {
        return "/home/data";
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return null;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data")
        private Data mData;

        public Data getData() {
            return mData;
        }

        public static class Data {

            @SerializedName("player") private List<Banner> mBanners;

            @SerializedName("promote_goods") private List<SimpleGoods> mGoodsList;

            public List<Banner> getBanners() {
                return mBanners;
            }

            public List<SimpleGoods> getGoodsList() {
                return mGoodsList;
            }
        }
    }
}
