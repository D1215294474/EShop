package com.feicuiedu.eshop.network.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Region;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiRegion implements ApiInterface {

    public static final int ID_CHINA = 1;

    private Req mReq;

    public ApiRegion(int parentId) {
        mReq = new Req();
        mReq.mParentId = parentId;
    }

    @NonNull @Override public String getPath() {
        return ApiPath.REGION;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {
        @SerializedName("parent_id") private int mParentId;

        @Override protected int sessionUsage() {
            return SESSION_NO_NEED;
        }
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data") Data mData;

        public Data getData() {
            return mData;
        }

        public static class Data {

            @SerializedName("more") private int mMore;

            @SerializedName("regions") private List<Region> mRegions;

            public boolean hasMore() {
                return mMore == 1;
            }

            public List<Region> getRegions() {
                return mRegions;
            }
        }
    }
}
