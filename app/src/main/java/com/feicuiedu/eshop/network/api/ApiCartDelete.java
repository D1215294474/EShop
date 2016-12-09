package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiConst;
import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.entity.CartBill;
import com.feicuiedu.eshop.network.entity.Session;
import com.google.gson.annotations.SerializedName;

/**
 * 服务器接口: 从购物车删除商品.
 */
public class ApiCartDelete implements ApiInterface {

    private Req mReq;

    public ApiCartDelete(int recId) {

        Session session = UserManager.getInstance().getSession();
        if (session == null) {
            throw new IllegalStateException("ApiCartDelete need a session.");
        }

        mReq = new Req();
        mReq.mRecId = recId;
        mReq.mSession = session;
    }

    @NonNull @Override public String getPath() {
        return ApiConst.PATH_CART_DELETE;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {

        @SerializedName("rec_id") int mRecId;

        @SerializedName("session") Session mSession;
    }

    public static class Rsp extends ResponseEntity {
        @SerializedName("total") private CartBill mCartBill;

        public CartBill getCartBill() {
            return mCartBill;
        }
    }
}
