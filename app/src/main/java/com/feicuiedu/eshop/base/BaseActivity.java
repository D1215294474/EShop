package com.feicuiedu.eshop.base;


import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.widgets.ProgressDialogFragment;
import com.feicuiedu.eshop.base.widgets.ptr.RefreshHeader;
import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.EShopClient;
import com.feicuiedu.eshop.network.ResponseEntity;
import com.feicuiedu.eshop.network.UiCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

/**
 * 通用Activity基类.
 */
public abstract class BaseActivity extends TransitionActivity {

    @Nullable @BindView(R.id.toolbar) Toolbar toolbar;

    @Nullable @BindView(R.id.layout_refresh) PtrFrameLayout refreshLayout;

    private ProgressDialogFragment mProgressDialogFragment;

    @Override public final void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        if (toolbar != null) {
            initAppBar();
        }

        if (refreshLayout != null) {
            initPtr();
        }

        initView();
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
    }

    /**
     * 视图初始化工作, 例如设置监听器和适配器.
     */
    protected abstract void initView();

    protected <T extends ResponseEntity> Call enqueue(ApiInterface<T> apiInterface,
                                                      UiCallback<T> uiCallback) {
        return EShopClient.getInstance()
                .enqueue(apiInterface, uiCallback, getClass().getSimpleName());
    }

    protected void showProgress() {
        if (mProgressDialogFragment == null) mProgressDialogFragment = new ProgressDialogFragment();

        if (mProgressDialogFragment.isAdded()) return;

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        mProgressDialogFragment.show(getSupportFragmentManager(), "ProgressDialogFragment");
    }

    protected void dismissProgress() {
        mProgressDialogFragment.dismiss();
    }

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
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initPtr() {
        assert refreshLayout != null;
        refreshLayout.disableWhenHorizontalMove(true);
        RefreshHeader refreshHeader = new RefreshHeader(this);
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
