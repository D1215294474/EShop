package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.RequestParam;
import com.feicuiedu.eshop.network.ResponseEntity;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.entity.GoodsInfo;
import com.feicuiedu.eshop.network.entity.Session;
import com.google.gson.annotations.SerializedName;

/**
 * 获取商品详情.
 */
public class ApiGoodsInfo implements ApiInterface {

    private final Req mReq;

    public ApiGoodsInfo(int goodsId) {
        mReq = new Req();
        mReq.goodsId = goodsId;
    }


    @NonNull @Override public String getPath() {
        return "/goods";
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    private static class Req extends RequestParam {

        @SerializedName("goods_id")
        int goodsId;

        @SerializedName("session")
        Session session = UserManager.getInstance().getSession();
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data")
        private GoodsInfo mData;

        public GoodsInfo getData() {
            return mData;
        }
    }
}
