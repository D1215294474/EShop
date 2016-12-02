package com.feicuiedu.eshop.base;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.widgets.ptr.RefreshHeader;
import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.EShopClient;
import com.feicuiedu.eshop.network.ResponseEntity;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.UserManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

/**
 * <p>Fragment的统一基类. 实现了{@link ButterKnife}绑定和{@link Call}的取消.
 * <p>注意如果此Fragment有标题栏, 需参考{@link R.layout#partial_action_bar}实现. 确保风格的统一.
 * <p>此Fragment必须在{@link BaseActivity}中使用.
 */
public abstract class BaseFragment extends Fragment {


    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;

    @Nullable @BindView(R.id.layout_refresh) PtrFrameLayout refreshLayout;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (toolbar != null) {
            initAppBar();
        }

        if (refreshLayout != null) {
            initPtr();
        }

        initView();
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

    protected ActionBar getSupportActionBar() {
        return ((BaseActivity) getActivity()).getSupportActionBar();
    }

    protected <T extends ResponseEntity> Call enqueue(ApiInterface<T> apiInterface,
                                                      UiCallback<T> uiCallback) {
        return EShopClient.getInstance()
                .enqueue(apiInterface, uiCallback, getClass().getSimpleName());
    }

    protected boolean isViewBind() {
        return mUnbinder != null;
    }

    @LayoutRes protected abstract int getContentViewLayout();

    /**
     * 视图初始化工作, 例如设置监听器和适配器.
     */
    protected abstract void initView();

    protected void onRefresh() {
    }

    protected void autoRefresh() {
        assert refreshLayout != null;
        refreshLayout.autoRefresh();
    }

    protected void stopRefresh() {
        assert refreshLayout != null;
        refreshLayout.refreshComplete();
    }

    private void initAppBar() {
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true); // 设置Fragment有选项菜单.
    }

    private void initPtr() {
        assert refreshLayout != null;
        refreshLayout.disableWhenHorizontalMove(true);
        RefreshHeader refreshHeader = new RefreshHeader(getContext());
        refreshLayout.setHeaderView(refreshHeader);
        refreshLayout.addPtrUIHandler(refreshHeader);
        refreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh();
            }
        });

        refreshLayout.postDelayed(new Runnable() {
            @Override public void run() {
                refreshLayout.autoRefresh();
            }
        }, 50);
    }
}
