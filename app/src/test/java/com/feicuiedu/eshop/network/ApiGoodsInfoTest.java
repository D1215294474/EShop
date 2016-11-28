package com.feicuiedu.eshop.network;


import com.feicuiedu.eshop.network.api.ApiGoodsInfo;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ApiGoodsInfoTest {

    @Test public void getGoodsInfo() throws IOException {

        EShopClient client = EShopClient.getInstance();

        ApiGoodsInfo apiGoodsInfo = new ApiGoodsInfo(-1);

        ApiGoodsInfo.Rsp rsp = client.execute(apiGoodsInfo);
        Assert.assertFalse(rsp.getStatus().isSucceed());
    }
}