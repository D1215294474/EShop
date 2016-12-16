package com.feicuiedu.eshop.feature.help;


import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.utils.LogUtils;
import com.feicuiedu.eshop.base.wrapper.PhotoWrapper;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.network.core.ResponseEntity;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;

public class HelpActivity extends BaseActivity {

    @BindView(R.id.web_view) WebView webView;

    private PhotoWrapper mPhotoWrapper;

    @Override protected int getContentViewLayout() {
        return R.layout.activity_help;
    }

    @Override protected void initView() {
        new ToolbarWrapper(this).setCustomTitle(R.string.mine_help);
        mPhotoWrapper = new PhotoWrapper();
        webView.loadUrl("file:///android_asset/help.html");
        webView.setWebViewClient(new WebViewClient() {

            @SuppressWarnings("deprecation")
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {

                try {
                    URL target = new URL(url);
                    String host = target.getHost();

                    if ("eshop.feicuiedu.com".equals(host)) {
                        String query = target.getQuery().substring(4);
                        LogUtils.debug(query);
                        mPhotoWrapper.showPhoto(HelpActivity.this, query);
                    } else {
                        webView.loadUrl(url);
                    }
                    return true;
                } catch (MalformedURLException e) {
                    return false;
                }
            }
        });
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {

    }

    @Override public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }

        super.onBackPressed();
    }
}
