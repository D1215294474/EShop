package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.RequestParam;
import com.feicuiedu.eshop.network.ResponseEntity;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.entity.Session;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 服务器接口: 添加到购物车.
 */
public class ApiCartCreate extends ApiInterface<ApiCartCreate.Rsp> {

    private Req mReq;

    public ApiCartCreate(int goodsId, int number) {
        mReq = new Req();

        Session session = UserManager.getInstance().getSession();
        if (session == null) {
            throw new IllegalStateException("ApiCartCreate need a session.");
        }

        mReq.mSession = session;
        mReq.mId = goodsId;
        mReq.mNumber = number;
    }

    @NonNull @Override public String getPath() {
        return "/cart/create";
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    public static class Req extends RequestParam {

        @SerializedName("session") private Session mSession;
        @SerializedName("goods_id") private int mId;
        @SerializedName("number") private int mNumber;
        @SerializedName("spec") private List<Integer> mSpecs;

    }

    public static class Rsp extends ResponseEntity {
    }
}
