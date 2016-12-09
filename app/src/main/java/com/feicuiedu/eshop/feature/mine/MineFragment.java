package com.feicuiedu.eshop.feature.mine;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.feature.address.manage.ManageAddressActivity;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.User;
import com.feicuiedu.eshop.network.event.UserEvent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的页面.
 */
public class MineFragment extends BaseFragment {

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @BindView(R.id.text_username) TextView tvName;

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_mine;
    }

    @Override protected void initView() {

    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {

    }

    @Override public void onEvent(UserEvent event) {
        super.onEvent(event);
        User user = UserManager.getInstance().getUser();

        if (user == null) {
            tvName.setText(R.string.sign_in_or_sign_up);
        } else {
            tvName.setText(user.getName());
        }
    }

    @OnClick({R.id.text_username, R.id.text_manage_address}) void onClick(View view) {

        if (!UserManager.getInstance().hasUser()) {
            Intent intent = new Intent(getContext(), SignInActivity.class);
            getActivity().startActivity(intent);
            return;
        }

        switch (view.getId()) {
            case R.id.text_username:
                break;
            case R.id.text_manage_address:
                Intent intent = new Intent(getContext(), ManageAddressActivity.class);
                startActivity(intent);
                break;
            default:
                throw new UnsupportedOperationException();
        }

    }

    @OnClick(R.id.button_setting) void navigateToSettings() {
        UserManager.getInstance().clear();
    }

}
