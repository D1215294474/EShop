package com.feicuiedu.eshop.feature.cart;


import android.support.v4.app.FragmentTransaction;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.network.core.ResponseEntity;

public class CartActivity extends BaseActivity {

    private static final String CART_FRAGMENT_TAG = CartFragment.class.getSimpleName();

    @Override protected int getContentViewLayout() {
        return R.layout.activity_cart;
    }

    @Override protected void initView() {
        if (getSupportFragmentManager().findFragmentByTag(CART_FRAGMENT_TAG) == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.container, CartFragment.newInstance(), CART_FRAGMENT_TAG);
            transaction.commit();
        }
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {
    }
}
