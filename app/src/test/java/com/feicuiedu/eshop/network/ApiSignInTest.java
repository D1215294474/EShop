package com.feicuiedu.eshop.network;

import com.feicuiedu.eshop.network.api.ApiSignIn;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ApiSignInTest extends ApiTest {

    @Test public void signIn() throws IOException {

        ApiSignIn apiSignIn = new ApiSignIn("ycj", "123456");
        ApiSignIn.Rsp rsp = client.execute(apiSignIn);
        UserManager.getInstance().signIn(rsp.getData().getSession(), rsp.getData().getUser());
        Assert.assertTrue(rsp.getStatus().isSucceed());
    }

}
