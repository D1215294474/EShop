package com.feicuiedu.eshop.base;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.EShopClient;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.UserManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * <p>Fragment的统一基类. 实现了{@link ButterKnife}绑定和{@link Call}的取消.
 * <p>此Fragment必须在{@link BaseActivity}中使用.
 * <p>视图初始化请在{@link #onActivityCreated(Bundle)}中完成.
 */
public abstract class BaseFragment extends Fragment {


    private Unbinder mUnbinder;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        mUnbinder.unbind();
        mUnbinder = null;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        EShopClient.getInstance().cancelByTag(getClass().getSimpleName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserManager.UpdateUserEvent event) {
    }

    protected Call enqueue(ApiInterface apiInterface,
                           UiCallback uiCallback) {
        return EShopClient.getInstance()
                .enqueue(apiInterface, uiCallback, getClass().getSimpleName());
    }

    protected boolean isViewBind() {
        return mUnbinder != null;
    }

    @LayoutRes protected abstract int getContentViewLayout();

}
