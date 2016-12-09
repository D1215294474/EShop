package com.feicuiedu.eshop.feature.order;

import android.content.Intent;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.address.edit.EditAddressActivity;
import com.feicuiedu.eshop.network.core.ResponseEntity;

import butterknife.OnClick;

/**
 * 订单预览界面.
 */
public class OrderPreviewActivity extends BaseActivity {

    @Override protected int getContentViewLayout() {
        return R.layout.activity_order_preview;
    }

    @Override protected void initView() {
        new ToolbarWrapper(this).setCustomTitle(R.string.title_order_preview);
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {

    }

    @OnClick(R.id.text_empty_consignee) void navigateToAddAddress() {
        // 添加新收件地址.
        Intent intent = EditAddressActivity.getStartIntent(this, null);
        startActivity(intent);
    }
}
