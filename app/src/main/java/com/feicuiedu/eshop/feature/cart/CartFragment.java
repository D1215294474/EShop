package com.feicuiedu.eshop.feature.cart;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.base.wrapper.AlertWrapper;
import com.feicuiedu.eshop.base.wrapper.ProgressWrapper;
import com.feicuiedu.eshop.base.wrapper.PtrWrapper;
import com.feicuiedu.eshop.base.wrapper.ToastWrapper;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.goods.GoodsActivity;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.api.ApiCartDelete;
import com.feicuiedu.eshop.network.api.ApiCartUpdate;
import com.feicuiedu.eshop.network.entity.CartGoods;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;

/**
 * 购物车页面.
 */
public class CartFragment extends BaseFragment {

    @BindView(R.id.list_cart_goods) ListView cartListView;

    private CartGoodsAdapter mGoodsAdapter;
    private PtrWrapper mPtrWrapper;
    private AlertWrapper mAlertWrapper = new AlertWrapper();
    private ProgressWrapper mProgressWrapper = new ProgressWrapper();

    public CartFragment() {
        // Required empty public constructor
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoodsAdapter = new CartGoodsAdapter() {
            @Override public void numberChanged(CartGoods goods, int number) {
                updateCart(goods.getRecId(), number);
            }
        };
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new ToolbarWrapper(this).setCustomTitle(R.string.title_shopping_cart);

        cartListView.setAdapter(mGoodsAdapter);

        mGoodsAdapter.reset(UserManager.getInstance().getCartGoodsList());

        mPtrWrapper = new PtrWrapper(this) {
            @Override public void onRefresh() {
                if (UserManager.getInstance().isSignIn()) {
                    UserManager.getInstance().updateCart(getContext());
                } else {
                    ToastWrapper.show(R.string.please_sign_in_first);
                    stopRefresh();
                }
            }
        };
    }

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_cart;
    }

    @OnItemLongClick(R.id.list_cart_goods)
    public boolean onItemLongClick(int position) {
        final CartGoods cartGoods = mGoodsAdapter.getItem(position);
        mAlertWrapper.setAlertText(R.string.confirm_delete_goods)
                .setConfirmListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        mAlertWrapper.dismiss();
                        deleteFromCart(cartGoods.getRecId());
                    }
                })
                .showAlert(this);

        return true;
    }

    @OnItemClick(R.id.list_cart_goods)
    public void onItemClick(int position) {
        CartGoods cartGoods = mGoodsAdapter.getItem(position);
        Intent intent = GoodsActivity.getStartIntent(getContext(), cartGoods.getGoodsId());
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserManager.UpdateCartEvent event) {
        mPtrWrapper.stopRefresh();
        mProgressWrapper.dismissProgress();
        if (event.success) {
            mGoodsAdapter.reset(UserManager.getInstance().getCartGoodsList());
        }
    }

    private void deleteFromCart(int recId) {
        mProgressWrapper.showProgress(this);

        ApiCartDelete apiCartDelete = new ApiCartDelete(recId);
        enqueue(apiCartDelete, new UiCallback<ApiCartDelete.Rsp>(getContext()) {
            @Override
            public void onBusinessResponse(boolean success, ApiCartDelete.Rsp responseEntity) {
                if (success) {
                    UserManager.getInstance().updateCart(getContext());
                } else {
                    mProgressWrapper.dismissProgress();
                }
            }
        });
    }

    private void updateCart(int recId, int newNumber) {
        mProgressWrapper.showProgress(this);

        ApiCartUpdate apiCartUpdate = new ApiCartUpdate(recId, newNumber);
        enqueue(apiCartUpdate, new UiCallback<ApiCartUpdate.Rsp>(getContext()) {
            @Override
            public void onBusinessResponse(boolean success, ApiCartUpdate.Rsp responseEntity) {
                if (success) {
                    UserManager.getInstance().updateCart(getContext());
                } else {
                    mProgressWrapper.dismissProgress();
                    mGoodsAdapter.notifyDataSetChanged();
                }
            }
        });
    }


}
