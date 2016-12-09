package com.feicuiedu.eshop.feature.goods;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.glide.GlideUtils;
import com.feicuiedu.eshop.base.widgets.SimpleNumberPicker;
import com.feicuiedu.eshop.base.wrapper.ToastWrapper;
import com.feicuiedu.eshop.feature.mine.SignInActivity;
import com.feicuiedu.eshop.network.EShopClient;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.api.ApiCartCreate;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.core.UiCallback;
import com.feicuiedu.eshop.network.entity.GoodsInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoodsSpecPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    @BindView(R.id.image_goods) ImageView ivGoods;
    @BindView(R.id.text_goods_price) TextView tvPrice;
    @BindView(R.id.number_picker) SimpleNumberPicker numberPicker;
    @BindView(R.id.text_number_value) TextView tvNumber;
    @BindView(R.id.text_inventory_value) TextView tvInventory;

    private ViewGroup mParent;
    private GoodsInfo mGoodsInfo;
    private BaseActivity mActivity;

    public GoodsSpecPopupWindow(BaseActivity activity, @NonNull GoodsInfo goodsInfo) {
        mActivity = activity;
        mGoodsInfo = goodsInfo;
        mParent = (ViewGroup) activity.getWindow().getDecorView();
        Context context = mParent.getContext();
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.popup_goods_spec, mParent, false);

        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(context.getResources().getDimensionPixelSize(R.dimen.size_400));

        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOnDismissListener(this);

        ButterKnife.bind(this, view);
        initView();
    }

    public void show() {
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.6f);
    }


    @Override public void onDismiss() {
        backgroundAlpha(1.0f);
    }

    @OnClick(R.id.button_ok) void addToCart() {
        int number = numberPicker.getNumber();

        if (number == 0) {
            ToastWrapper.show(R.string.please_choose_number);
            return;
        }

        if (!UserManager.getInstance().hasUser()) {
            Intent intent = new Intent(mActivity, SignInActivity.class);
            mActivity.startActivity(intent);
            return;
        }

        ApiCartCreate apiCartCreate = new ApiCartCreate(mGoodsInfo.getId(), number);
        EShopClient.getInstance().enqueue(apiCartCreate, new UiCallback() {
            @Override
            public void onBusinessResponse(boolean success, ResponseEntity responseEntity) {

                if (success) {
                    UserManager.getInstance().retrieveCartList();
                    ToastWrapper.show(R.string.add_to_cart_succeed);
                    dismiss();
                }
            }
        }, null);
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    private void initView() {
        GlideUtils.loadPicture(mGoodsInfo.getImg(), ivGoods);
        tvPrice.setText(mGoodsInfo.getShopPrice());
        tvInventory.setText(String.valueOf(mGoodsInfo.getNumber()));

        numberPicker.setOnNumberChangedListener(new SimpleNumberPicker.OnNumberChangedListener() {
            @Override public void onNumberChanged(int number) {
                tvNumber.setText(String.valueOf(number));
            }
        });
    }
}
