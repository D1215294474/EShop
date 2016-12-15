package com.feicuiedu.eshop.network;


import com.feicuiedu.eshop.network.api.ApiCollectCreate;

import org.junit.Test;

import java.io.IOException;

public class ApiCollectCreateTest extends ApiSignInTest {

    @Test public void createCollect() throws IOException {
        signIn();
        ApiCollectCreate apiCollectCreate = new ApiCollectCreate(80);
        client.execute(apiCollectCreate);
    }
}
