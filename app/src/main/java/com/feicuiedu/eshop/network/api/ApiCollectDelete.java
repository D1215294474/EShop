package com.feicuiedu.eshop.network.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.google.gson.annotations.SerializedName;

public class ApiCollectDelete implements ApiInterface {

    private final Req mReq;

    public ApiCollectDelete(int recId) {
        mReq = new Req();
        mReq.mRecId = recId;
    }

    @NonNull @Override public String getPath() {
        return ApiPath.COLLECT_DELETE;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {

        @SerializedName("rec_id") private int mRecId;

        @Override protected int sessionUsage() {
            return SESSION_MANDATORY;
        }
    }

    public static class Rsp extends ResponseEntity {
    }
}
