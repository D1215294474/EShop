package com.feicuiedu.eshop.feature.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.wrapper.AlertWrapper;
import com.feicuiedu.eshop.base.wrapper.ProgressWrapper;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.address.manage.ManageAddressActivity;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.api.ApiOrderDone;
import com.feicuiedu.eshop.network.api.ApiOrderPreview;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Address;
import com.feicuiedu.eshop.network.entity.CartGoods;
import com.feicuiedu.eshop.network.entity.Payment;
import com.feicuiedu.eshop.network.entity.Shipping;
import com.feicuiedu.eshop.network.event.UserEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单预览界面.
 */
public class OrderPreviewActivity extends BaseActivity {

    @BindView(R.id.text_consignee) TextView tvConsignee;
    @BindView(R.id.text_address) TextView tvAddress;
    @BindView(R.id.layout_goods) LinearLayout goodsLayout;
    @BindView(R.id.text_payment) TextView tvPayment;
    @BindView(R.id.text_shipping) TextView tvShipping;
    @BindView(R.id.button_summit) Button btnSummit;
    @BindView(R.id.text_shipping_price) TextView tvShippingPrice;
    @BindView(R.id.text_goods_price) TextView tvGoodsPrice;


    private List<Payment> mPaymentList;
    private List<Shipping> mShippingList;
    private AlertWrapper mAlertWrapper;
    private ProgressWrapper mProgressWrapper;

    private int mPaymentId;
    private int mShippingId;

    @Override protected int getContentViewLayout() {
        return R.layout.activity_order_preview;
    }

    @Override protected void initView() {
        new ToolbarWrapper(this).setCustomTitle(R.string.title_order_preview);
        mAlertWrapper = new AlertWrapper();
        mProgressWrapper = new ProgressWrapper();
        mProgressWrapper.showProgress(this);
        enqueue(new ApiOrderPreview());
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {
        mProgressWrapper.dismissProgress();

        if (!success) return;

        switch (apiPath) {
            case ApiPath.ORDER_PREVIEW:
                ApiOrderPreview.Rsp previewRsp = (ApiOrderPreview.Rsp) rsp;
                mPaymentList = previewRsp.getData().getPaymentList();
                mShippingList = previewRsp.getData().getShippingList();
                showAddress(previewRsp.getData().getAddress());
                showGoods(previewRsp.getData().getGoodsList());

                String price = UserManager.getInstance().getCartBill().getGoodsPrice();
                tvGoodsPrice.setText(getString(R.string.order_goods_price, price));
                tvShippingPrice.setText(getString(R.string.order_shipping_price, ""));
                break;
            case ApiPath.ORDER_DONE:
                mAlertWrapper.setAlertText(R.string.alert_purchase_order).showAlert(this);
                break;
            default:
                throw new UnsupportedOperationException(apiPath);
        }
    }

    @Override public void onEvent(UserEvent event) {
        super.onEvent(event);
        if (!UserManager.getInstance().hasUser()) {
            finish();
        }
    }

    @OnClick(R.id.layout_consignee) void navigateToManageAddress(View view) {
        Intent manage = new Intent(this, ManageAddressActivity.class);
        startActivity(manage);
    }

    @OnClick(R.id.text_payment) void showSelectPayment() {

        if (mPaymentList == null) return;

        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_payment)
                .setItems(paymentsToStrings(mPaymentList), new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        Payment payment = mPaymentList.get(which);
                        mPaymentId = payment.getId();
                        tvPayment.setText(
                                getString(R.string.payment_name, payment.getName())
                        );
                        checkSummitEnabled();
                    }
                })
                .show();
    }

    @OnClick(R.id.text_shipping) void showSelectShipping() {

        if (mShippingList == null) return;

        new AlertDialog.Builder(this)
                .setTitle(R.string.choose_shipping)
                .setItems(shippingsToStrings(mShippingList), new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        Shipping shipping = mShippingList.get(which);
                        mShippingId = shipping.getId();
                        tvShipping.setText(
                                getString(R.string.shipping_name, shipping.getName())
                        );
                        tvShippingPrice.setText(
                                getString(R.string.order_shipping_price, shipping.getPrice())
                        );
                        checkSummitEnabled();
                    }
                })
                .show();
    }

    @OnClick(R.id.button_summit) void summitOrder() {
        mProgressWrapper.showProgress(this);
        ApiOrderDone apiOrderDone = new ApiOrderDone(mPaymentId, mShippingId);
        enqueue(apiOrderDone);
    }


    private void showAddress(Address address) {
        tvConsignee.setText(
                getString(R.string.consignee_name, address.getConsignee())
        );
        tvAddress.setText(
                String.format("(%s)%s%s - %s",
                        address.getProvinceName(),
                        address.getCityName(),
                        address.getDistrictName(),
                        address.getAddress()
                )
        );
    }

    private void showGoods(List<CartGoods> goodsList) {
        for (CartGoods goods : goodsList) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.item_order_goods, goodsLayout, false);
            GoodsItemHolder holder = new GoodsItemHolder(view);
            holder.bind(goods);
            goodsLayout.addView(view);
        }
    }

    private String[] paymentsToStrings(List<Payment> paymentList) {
        ArrayList<String> list = new ArrayList<>();

        for (Payment payment : paymentList) {
            list.add(payment.getName());
        }
        return list.toArray(new String[list.size()]);
    }

    private String[] shippingsToStrings(List<Shipping> shippingList) {
        ArrayList<String> list = new ArrayList<>();

        for (Shipping shipping : shippingList) {
            list.add(shipping.getName() + "  " + shipping.getPrice());
        }
        return list.toArray(new String[list.size()]);
    }

    private void checkSummitEnabled() {
        if (mPaymentId != 0 && mShippingId != 0) {
            btnSummit.setEnabled(true);
        } else {
            btnSummit.setEnabled(false);
        }
    }

    final class GoodsItemHolder {
        @BindView(R.id.text_goods_name) TextView tvName;
        @BindView(R.id.text_amount) TextView tvAmount;

        public GoodsItemHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(CartGoods cartGoods) {
            tvName.setText(cartGoods.getGoodsName());
            tvAmount.setText(
                    getString(R.string.order_goods_amount, cartGoods.getGoodsNumber())
            );
        }
    }

}
