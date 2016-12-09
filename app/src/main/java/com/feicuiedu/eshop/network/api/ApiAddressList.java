package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiConst;
import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.entity.Address;
import com.feicuiedu.eshop.network.entity.Session;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 收货地址列表接口.
 */
public class ApiAddressList implements ApiInterface {

    private final Req mReq;

    public ApiAddressList() {
        mReq = new Req();

        Session session = UserManager.getInstance().getSession();
        if (session == null) {
            throw new IllegalStateException("ApiCartCreate need a session.");
        }

        mReq.mSession = session;
    }

    @NonNull @Override public String getPath() {
        return ApiConst.PATH_ADDRESS_LIST;
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
        @SerializedName("data") private List<Address> mData;

        public List<Address> getData() {
            return mData;
        }
    }
}
