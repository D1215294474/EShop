package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiConst;
import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Filter;
import com.feicuiedu.eshop.network.entity.Paginated;
import com.feicuiedu.eshop.network.entity.Pagination;
import com.feicuiedu.eshop.network.entity.SimpleGoods;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 服务器接口: 搜索商品.
 */
public class ApiSearch implements ApiInterface {

    private Req mReq;

    public ApiSearch(Filter filter, Pagination pagination) {
        mReq = new Req();
        mReq.mFilter = filter;
        mReq.mPagination = pagination;
    }

    @NonNull
    @Override public String getPath() {
        return ApiConst.PATH_SEARCH;
    }

    @Nullable
    @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }


    public static class Req extends RequestParam {
        @SerializedName("filter") private Filter mFilter;

        @SerializedName("pagination") private Pagination mPagination;
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data") private List<SimpleGoods> mData;

        @SerializedName("paginated") private Paginated mPaginated;

        public List<SimpleGoods> getData() {
            return mData;
        }

        public Paginated getPaginated() {
            return mPaginated;
        }
    }
}
