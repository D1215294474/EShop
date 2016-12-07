package com.feicuiedu.eshop.feature.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.utils.Sha256Utils;
import com.feicuiedu.eshop.base.wrapper.ProgressWrapper;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.api.ApiSignIn;
import com.feicuiedu.eshop.network.entity.Session;
import com.feicuiedu.eshop.network.entity.User;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 登录页面
 */
public class SignInActivity extends BaseActivity {

    @BindView(R.id.edit_name) EditText etName;
    @BindView(R.id.edit_password) EditText etPassword;

    @BindView(R.id.button_signin) Button btnSignIn;

    private ProgressWrapper mProgressWrapper = new ProgressWrapper();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        new ToolbarWrapper(this).setCustomTitle(R.string.title_sign_in);
    }

    @OnTextChanged({R.id.edit_password, R.id.edit_name})
    public void onTextChanged() {
        String username = etName.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            btnSignIn.setEnabled(false);
        } else {
            btnSignIn.setEnabled(true);
        }
    }

    @OnClick(R.id.button_signin)
    public void signIn() {
        String username = etName.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            throw new IllegalStateException("Username or Password is empty.");
        }

        ApiSignIn apiSignIn = new ApiSignIn(username, Sha256Utils.bin2hex(password));

        mProgressWrapper.showProgress(this);
        enqueue(apiSignIn, new UiCallback<ApiSignIn.Rsp>(this) {
            @Override
            public void onBusinessResponse(boolean success, ApiSignIn.Rsp responseEntity) {
                mProgressWrapper.dismiss();
                if (success) {
                    Session session = responseEntity.getData().getSession();
                    User user = responseEntity.getData().getUser();
                    UserManager.getInstance().signIn(session, user);
                    UserManager.getInstance().updateCart(SignInActivity.this);
                    finish();
                }
            }
        });
    }

    @OnClick(R.id.text_signup)
    public void navigateToSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finishWithDefaultTransition();
    }
}
