package com.feicuiedu.eshop.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.EShopClient;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.UserManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * <p>通用Activity基类.
 * <p>视图初始化请在{@link #onContentChanged()}中完成.
 */
public abstract class BaseActivity extends TransitionActivity {

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        EShopClient.getInstance().cancelByTag(getClass().getSimpleName());
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserManager.UpdateUserEvent event) {
    }

    public Call enqueue(ApiInterface apiInterface,
                        UiCallback uiCallback) {
        return EShopClient.getInstance()
                .enqueue(apiInterface, uiCallback, getClass().getSimpleName());
    }


}
