package com.feicuiedu.eshop.network;

import com.feicuiedu.eshop.network.api.ApiCategory;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * 商品分类接口的单元测试.
 */
public class ApiCategoryTest {

    @Test public void getCategories() throws IOException {

        EShopClient client = EShopClient.getInstance();

        ApiCategory apiCategory = new ApiCategory();
        ApiCategory.Rsp rsp = client.execute(apiCategory);
        Assert.assertTrue(rsp.getStatus().isSucceed());
    }

}
