package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiConst;
import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.entity.CartBill;
import com.feicuiedu.eshop.network.entity.CartGoods;
import com.feicuiedu.eshop.network.entity.Session;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 服务器接口: 购物车列表.
 */
public class ApiCartList implements ApiInterface {

    private Req mReq;

    public ApiCartList() {
        Session session = UserManager.getInstance().getSession();

        if (session == null) {
            throw new IllegalStateException("ApiCartList need a session.");
        }

        mReq = new Req();
        mReq.mSession = session;
    }

    @NonNull @Override public String getPath() {
        return ApiConst.PATH_CART_LIST;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {

        @SerializedName("session") private Session mSession;

    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data") Data mData;

        public Data getData() {
            return mData;
        }

        public static class Data {
            @SerializedName("goods_list") private List<CartGoods> mGoodsList;

            @SerializedName("total") private CartBill mCartBill;

            public List<CartGoods> getGoodsList() {
                return mGoodsList;
            }

            public CartBill getCartBill() {
                return mCartBill;
            }
        }
    }
}
