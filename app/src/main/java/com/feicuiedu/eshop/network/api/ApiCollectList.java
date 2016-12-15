package com.feicuiedu.eshop.network.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.CollectGoods;
import com.feicuiedu.eshop.network.entity.Paginated;
import com.feicuiedu.eshop.network.entity.Pagination;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 获取收藏列表.
 */
public class ApiCollectList implements ApiInterface {

    private final Req mReq;

    public ApiCollectList(Pagination pagination) {
        mReq = new Req();
        mReq.mPagination = pagination;
    }

    @NonNull @Override public String getPath() {
        return ApiPath.COLLECT_LIST;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {

        @SerializedName("pagination") private Pagination mPagination;

        @Override protected int sessionUsage() {
            return SESSION_MANDATORY;
        }
    }

    public static class Rsp extends ResponseEntity {
        @SerializedName("paginated") private Paginated mPaginated;
        @SerializedName("data") private List<CollectGoods> mGoodsList;

        public Paginated getPaginated() {
            return mPaginated;
        }

        public List<CollectGoods> getGoodsList() {
            return mGoodsList;
        }
    }
}
