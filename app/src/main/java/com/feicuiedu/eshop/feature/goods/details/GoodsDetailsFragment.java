package com.feicuiedu.eshop.feature.goods.details;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.GoodsInfo;
import com.google.gson.Gson;

import butterknife.BindView;

public class GoodsDetailsFragment extends BaseFragment {

    private static final String ARGUMENT_GOODS_INFO = "ARGUMENT_GOODS_INFO";

    public static GoodsDetailsFragment newInstance(GoodsInfo goodsInfo) {

        Bundle args = new Bundle();
        args.putString(ARGUMENT_GOODS_INFO, new Gson().toJson(goodsInfo));

        GoodsDetailsFragment fragment = new GoodsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.web_view) WebView webView;

    private GoodsInfo mGoodsInfo;

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_goods_details;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Override protected void initView() {

        // 获取传入的商品信息实体
        mGoodsInfo = new Gson().fromJson(getArguments().getString(ARGUMENT_GOODS_INFO),
                GoodsInfo.class);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/goodsDetails.html");
        webView.addJavascriptInterface(new WebViewInterface(), "GoodsDetails");
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {

    }


    public class WebViewInterface {

        @JavascriptInterface
        public String getGoodsName() {
            return mGoodsInfo.getName();
        }

        @JavascriptInterface
        public void makePhoneCall(String tel) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + tel);
            intent.setData(data);
            startActivity(intent);
        }
    }

}
