package com.feicuiedu.eshop.network.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 商品详情实体.
 */
public class GoodsInfo {

    @SerializedName("id") private int mId;

    @SerializedName("goods_name") private String mName;

    @SerializedName("pictures") private List<Picture> mPictures;

    @SerializedName("shop_price") private String mShopPrice;

    public String getMarketPrice() {
        return mMarketPrice;
    }

    @SerializedName("market_price") private String mMarketPrice;

    public List<Picture> getPictures() {
        return mPictures;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getShopPrice() {
        return mShopPrice;
    }
}
