package com.feicuiedu.eshop.network;


import com.feicuiedu.eshop.network.api.ApiGoodsInfo;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ApiGoodsInfoTest extends ApiTest {

    @Test public void getGoodsInfo() throws IOException {

        ApiGoodsInfo apiGoodsInfo = new ApiGoodsInfo(-1);

        ApiGoodsInfo.Rsp rsp = client.execute(apiGoodsInfo);
        Assert.assertFalse(rsp.getStatus().isSucceed());
    }
}