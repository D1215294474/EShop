package com.feicuiedu.eshop.network.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Address;
import com.google.gson.annotations.SerializedName;

public class ApiAddressInfo implements ApiInterface {

    private Req mReq;

    public ApiAddressInfo(int addressId) {
        mReq = new Req();
        mReq.mId = addressId;
    }

    @NonNull @Override public String getPath() {
        return ApiPath.ADDRESS_INFO;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {
        @SerializedName("address_id") private int mId;

        @Override protected int sessionUsage() {
            return SESSION_MANDATORY;
        }
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data") private Address mData;

        public Address getData() {
            return mData;
        }
    }
}
