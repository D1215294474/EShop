package com.feicuiedu.eshop.network;


import com.feicuiedu.eshop.network.api.ApiOrderPreview;

import org.junit.Test;

import java.io.IOException;

public class ApiOrderPreviewTest extends ApiSignInTest {

    @Test public void previewOrder() throws IOException {
        signIn();
        ApiOrderPreview apiOrderPreview = new ApiOrderPreview();
        client.execute(apiOrderPreview);
    }
}
