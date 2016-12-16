package com.feicuiedu.eshop.feature.settings;


import android.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.event.UserEvent;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.button_sign_out) Button btnSignOut;

    @Override protected int getContentViewLayout() {
        return R.layout.activity_settings;
    }

    @Override protected void initView() {
        new ToolbarWrapper(this).setCustomTitle(R.string.settings_title);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.container, new SettingsFragment());
        transaction.commit();
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {

    }

    @Override public void onEvent(UserEvent event) {
        super.onEvent(event);

        if (UserManager.getInstance().hasUser()) {
            btnSignOut.setVisibility(View.VISIBLE);
        } else {
            btnSignOut.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.button_sign_out) void signOut() {
        UserManager.getInstance().clear();
        finish();
    }
}
