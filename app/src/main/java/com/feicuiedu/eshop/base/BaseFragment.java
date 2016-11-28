package com.feicuiedu.eshop.base;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.widgets.ptr.RefreshHeader;
import com.feicuiedu.eshop.network.EShopClient;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

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


    @Nullable @BindView(R.id.toolbar) Toolbar toolbar; // ActionBar
    @Nullable @BindView(R.id.text_toolbar_title) TextView tvToolbarTitle; // ActionBar标题

    @Nullable @BindView(R.id.layout_refresh) protected PtrFrameLayout refreshLayout;

    protected final EShopClient client = EShopClient.getInstance(); // 用于服务器Api请求

    // 使用弱引用缓存所有Call对象, 在Fragment销毁时统一取消, 避免内存溢出.
    private final List<WeakReference<Call>> mCallList = new ArrayList<>();
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater,
                                   @Nullable ViewGroup container,
                                   @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    /**
     * <p>在此方法中做父类统一的视图初始化工作.
     * <p>子类的视图初始化在{@link #onActivityCreated(Bundle)}中执行.
     */
    @Override public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (toolbar != null && tvToolbarTitle != null) {
            ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setHasOptionsMenu(true); // 设置Fragment有选项菜单.
            tvToolbarTitle.setText(getTitleId());
        }

        if (refreshLayout != null) {
            refreshLayout.disableWhenHorizontalMove(true);
            RefreshHeader refreshHeader = new RefreshHeader(getContext());
            refreshLayout.setHeaderView(refreshHeader);
            refreshLayout.addPtrUIHandler(refreshHeader);
            refreshLayout.setPtrHandler(new PtrDefaultHandler() {
                @Override public void onRefreshBegin(PtrFrameLayout frame) {
                    BaseFragment.this.onRefreshBegin(frame);
                }
            });
        }
    }

    @Override public final void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        mUnbinder = null;
    }

    @Override public void onDestroy() {
        super.onDestroy();
        // 取消所有网络请求, 避免内存溢出
        for (WeakReference<Call> reference : mCallList) {
            Call call = reference.get();
            if (call != null) call.cancel();
        }
    }

    public ActionBar getSupportActionBar() {
        return ((BaseActivity) getActivity()).getSupportActionBar();
    }

    protected void saveCall(Call call) {
        mCallList.add(new WeakReference<>(call));
    }

    protected boolean isViewBind() {
        return mUnbinder != null;
    }

    @LayoutRes protected abstract int getContentViewLayout();

    @StringRes protected abstract int getTitleId();

    protected void onRefreshBegin(PtrFrameLayout frame) {
    }

    protected void autoRefresh() {
        if (refreshLayout == null) {
            throw new UnsupportedOperationException("No PtrFrameLayout in this Fragment");
        }

        refreshLayout.autoRefresh();
    }
}
