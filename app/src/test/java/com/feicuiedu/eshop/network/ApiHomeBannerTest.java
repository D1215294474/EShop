package com.feicuiedu.eshop.network;


import com.feicuiedu.eshop.network.api.ApiHomeBanner;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ApiHomeBannerTest {

    @Test public void getBanners() throws IOException {
        EShopClient client = EShopClient.getInstance();

        ApiHomeBanner apiHomeBanner = new ApiHomeBanner();
        ApiHomeBanner.Rsp rsp = client.execute(apiHomeBanner);

        Assert.assertTrue(rsp.getStatus().isSucceed());
    }
}
