package com.feicuiedu.eshop.base;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.EShopClient;
import com.feicuiedu.eshop.network.core.ApiInterface;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.core.UiCallback;
import com.feicuiedu.eshop.network.event.UserEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 通用Activity基类.
 */
public abstract class BaseActivity extends TransitionActivity {

    private Unbinder mUnbinder;

    @Override protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());
        mUnbinder = ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override protected final void onDestroy() {
        super.onDestroy();
        EShopClient.getInstance().cancelByTag(getClass().getSimpleName());
        EventBus.getDefault().unregister(this);
        mUnbinder.unbind();
        mUnbinder = null;
    }

    protected Call enqueue(final ApiInterface apiInterface) {
        UiCallback uiCallback = new UiCallback() {
            @Override
            public void onBusinessResponse(boolean success, ResponseEntity responseEntity) {
                BaseActivity.this.onBusinessResponse(
                        apiInterface.getPath(),
                        success,
                        responseEntity);
            }
        };

        return EShopClient.getInstance()
                .enqueue(apiInterface, uiCallback, getClass().getSimpleName());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(UserEvent event) {
    }

    @LayoutRes protected abstract int getContentViewLayout();

    protected abstract void initView();

    protected abstract void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp);
}
