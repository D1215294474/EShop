package com.feicuiedu.eshop.feature.address.manage;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.wrapper.ProgressWrapper;
import com.feicuiedu.eshop.base.wrapper.ToastWrapper;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.address.edit.EditAddressActivity;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.api.ApiAddressDefault;
import com.feicuiedu.eshop.network.api.ApiAddressDelete;
import com.feicuiedu.eshop.network.api.ApiAddressInfo;
import com.feicuiedu.eshop.network.core.ApiConst;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Address;
import com.feicuiedu.eshop.network.event.AddressEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 管理收货地址.
 */
public class ManageAddressActivity extends BaseActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private ProgressWrapper mProgressWrapper;
    private AddressAdapter mAddressAdapter;

    @Override protected int getContentViewLayout() {
        return R.layout.activity_manage_address;
    }

    @Override protected void initView() {
        new ToolbarWrapper(this).setCustomTitle(R.string.title_manage_address);
        mProgressWrapper = new ProgressWrapper();

        mAddressAdapter = new AddressAdapterImpl();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAddressAdapter);
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {


        switch (apiPath) {
            case ApiConst.PATH_ADDRESS_DEFAULT:
            case ApiConst.PATH_ADDRESS_DELETE:
                if (success) {
                    UserManager.getInstance().retrieveAddressList();
                } else {
                    mProgressWrapper.dismissProgress();
                }
                break;
            case ApiConst.PATH_ADDRESS_INFO:
                mProgressWrapper.dismissProgress();

                if (success) {
                    ApiAddressInfo.Rsp infoRsp = (ApiAddressInfo.Rsp) rsp;
                    Intent intent = EditAddressActivity.getStartIntent(this, infoRsp.getData());
                    startActivity(intent);
                }
                break;
            default:
                throw new UnsupportedOperationException(apiPath);
        }

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(AddressEvent event) {
        mProgressWrapper.dismissProgress();
        mAddressAdapter.reset(UserManager.getInstance().getAddressList());
    }

    @OnClick(R.id.button_create) void navigateToAddAddress() {
        Intent intent = EditAddressActivity.getStartIntent(this, null);
        startActivity(intent);
    }

    private class AddressAdapterImpl extends AddressAdapter {
        @Override protected void onSetDefault(Address address) {
            mProgressWrapper.showProgress(ManageAddressActivity.this);
            ApiAddressDefault apiAddressDefault = new ApiAddressDefault(address.getId());
            enqueue(apiAddressDefault);
        }

        @Override protected void onDelete(Address address) {
            if (address.isDefault()) {
                ToastWrapper.show(R.string.can_not_delete_default_address);
                return;
            }

            mProgressWrapper.showProgress(ManageAddressActivity.this);
            ApiAddressDelete apiAddressDelete = new ApiAddressDelete(address.getId());
            enqueue(apiAddressDelete);
        }

        @Override protected void onEdit(Address address) {
            mProgressWrapper.showProgress(ManageAddressActivity.this);
            ApiAddressInfo apiAddressInfo = new ApiAddressInfo(address.getId());
            enqueue(apiAddressInfo);
        }
    }
}
