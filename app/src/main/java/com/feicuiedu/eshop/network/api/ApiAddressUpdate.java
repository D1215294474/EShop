package com.feicuiedu.eshop.network.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.core.ApiConst;
import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Address;
import com.feicuiedu.eshop.network.entity.Session;
import com.google.gson.annotations.SerializedName;

public class ApiAddressUpdate implements ApiInterface {

    private final Req mReq;

    public ApiAddressUpdate(Address address, int addressId) {
        mReq = new Req();

        Session session = UserManager.getInstance().getSession();
        if (session == null) {
            throw new IllegalStateException("ApiCartCreate need a session.");
        }

        mReq.mSession = session;
        mReq.mAddress = address;
        mReq.mId = addressId;
    }

    @NonNull @Override public String getPath() {
        return ApiConst.PATH_ADDRESS_UPDATE;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {

        @SerializedName("session") private Session mSession;
        @SerializedName("address") private Address mAddress;
        @SerializedName("address_id") private int mId;
    }

    public static class Rsp extends ResponseEntity {
    }
}
