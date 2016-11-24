package com.feicuiedu.eshop.network;


import com.feicuiedu.eshop.network.api.ApiHomeCategory;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ApiHomeCategoryTest {

    @Test public void getHomeCategories() throws IOException {

        EShopClient client = EShopClient.getInstance();

        ApiHomeCategory apiHomeCategory = new ApiHomeCategory();
        ApiHomeCategory.Rsp rsp = client.execute(apiHomeCategory);
        Assert.assertTrue(rsp.getStatus().isSucceed());
    }
}
