package com.feicuiedu.eshop.network;


import com.feicuiedu.eshop.network.api.ApiSearch;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ApiSearchTest {

    @Test public void searchGoods() throws IOException {

        EShopClient client = EShopClient.getInstance();

        ApiSearch apiSearch = new ApiSearch(null, null);
        ApiSearch.Rsp rsp = client.execute(apiSearch);
        Assert.assertTrue(rsp.getStatus().isSucceed());
    }
}
