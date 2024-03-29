package com.feicuiedu.eshop.network.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Order;
import com.feicuiedu.eshop.network.entity.Paginated;
import com.feicuiedu.eshop.network.entity.Pagination;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public class ApiOrderList implements ApiInterface {

    public static final String ORDER_AWAIT_PAY = "await_pay"; // 待支付
    public static final String ORDER_UNCONFIRMED = "unconfirmed"; // 待确认
    public static final String ORDER_AWAIT_SHIP = "await_ship"; // 待发货
    public static final String ORDER_SHIPPED = "shipped"; // 已发货
    public static final String ORDER_FINISHED = "finished"; // 已完成

    @StringDef({
            ORDER_AWAIT_PAY, ORDER_AWAIT_SHIP, ORDER_FINISHED, ORDER_SHIPPED, ORDER_UNCONFIRMED
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrderType {
    }

    private Req mReq;

    public ApiOrderList(@OrderType String type, Pagination pagination) {
        mReq = new Req();
        mReq.mType = type;
        mReq.mPagination = pagination;
    }

    @NonNull @Override public String getPath() {
        return ApiPath.ORDER_LIST;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {

        @SerializedName("type") private String mType;
        @SerializedName("pagination") private Pagination mPagination;

        @Override protected int sessionUsage() {
            return SESSION_MANDATORY;
        }
    }

    public static class Rsp extends ResponseEntity {
        @SerializedName("paginated") private Paginated mPaginated;
        @SerializedName("data") private List<Order> mOrderList;

        public Paginated getPaginated() {
            return mPaginated;
        }

        public List<Order> getOrderList() {
            return mOrderList;
        }
    }


}
