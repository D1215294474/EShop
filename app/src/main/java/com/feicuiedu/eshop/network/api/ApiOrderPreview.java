package com.feicuiedu.eshop.network.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.RequestParam;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Address;
import com.feicuiedu.eshop.network.entity.CartGoods;
import com.feicuiedu.eshop.network.entity.Payment;
import com.feicuiedu.eshop.network.entity.Shipping;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiOrderPreview implements ApiInterface {


    @NonNull @Override public String getPath() {
        return ApiPath.ORDER_PREVIEW;
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return new Req();
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {

        @Override protected int sessionUsage() {
            return SESSION_MANDATORY;
        }
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data") private Data mData;

        public Data getData() {
            return mData;
        }

        public static class Data {
            @SerializedName("goods_list") private List<CartGoods> mGoodsList;

            @SerializedName("consignee") private Address mAddress;

            @SerializedName("shipping_list") private List<Shipping> mShippingList; // 派送方式

            @SerializedName("payment_list") private List<Payment> mPaymentList; // 支付方式

            public List<CartGoods> getGoodsList() {
                return mGoodsList;
            }

            public Address getAddress() {
                return mAddress;
            }

            public List<Shipping> getShippingList() {
                return mShippingList;
            }

            public List<Payment> getPaymentList() {
                return mPaymentList;
            }
        }
    }

}
