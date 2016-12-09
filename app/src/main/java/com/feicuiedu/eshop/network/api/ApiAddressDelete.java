package com.feicuiedu.eshop.network.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.core.ApiConst;
import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Session;
import com.google.gson.annotations.SerializedName;

public class ApiAddressDelete implements ApiInterface {

    private Req mReq;

    public ApiAddressDelete(int addressId) {
        mReq = new Req();

        Session session = UserManager.getInstance().getSession();
        if (session == null) {
            throw new IllegalStateException("ApiCartCreate need a session.");
        }

        mReq.mSession = session;
        mReq.mId = addressId;
    }

    @NonNull @Override public String getPath() {
        return ApiConst.PATH_ADDRESS_DELETE;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {
        @SerializedName("session") private Session mSession;
        @SerializedName("address_id") private int mId;
    }

    public static class Rsp extends ResponseEntity {
    }
}
