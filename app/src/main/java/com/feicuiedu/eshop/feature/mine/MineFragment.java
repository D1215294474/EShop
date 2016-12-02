package com.feicuiedu.eshop.feature.mine;


import android.content.Intent;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.entity.User;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的页面.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.text_username) TextView tvName;

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_mine;
    }

    @Override protected void initView() {
        setUser();
    }

    @Override public void onEvent(UserManager.UpdateUserEvent event) {
        super.onEvent(event);
        setUser();
    }

    @OnClick(R.id.text_username)
    public void onClick() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        getActivity().startActivity(intent);
    }

    private void setUser() {
        User user = UserManager.getInstance().getUser();

        if (user == null) {
            tvName.setText(R.string.sign_in_or_sign_up);
        } else {
            tvName.setText(user.getName());
        }
    }

}
