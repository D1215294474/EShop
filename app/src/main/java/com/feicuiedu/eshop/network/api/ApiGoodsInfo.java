package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.GoodsInfo;
import com.google.gson.annotations.SerializedName;

/**
 * 获取商品详情.
 */
public class ApiGoodsInfo implements ApiInterface {

    private final Req mReq;

    public ApiGoodsInfo(int goodsId) {
        mReq = new Req();
        mReq.mGoodsId = goodsId;
    }


    @NonNull @Override public String getPath() {
        return ApiPath.GOODS;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    private static class Req extends RequestParam {

        @SerializedName("goods_id")
        private int mGoodsId;

        @Override protected int sessionUsage() {
            return SESSION_OPTIONAL;
        }
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data")
        private GoodsInfo mData;

        public GoodsInfo getData() {
            return mData;
        }
    }
}
