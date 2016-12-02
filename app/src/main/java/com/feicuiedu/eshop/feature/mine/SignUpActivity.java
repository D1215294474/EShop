package com.feicuiedu.eshop.feature.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.utils.Sha256Utils;
import com.feicuiedu.eshop.network.UserManager;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.api.ApiSignUp;
import com.feicuiedu.eshop.network.entity.Session;
import com.feicuiedu.eshop.network.entity.User;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 注册页面
 */
public class SignUpActivity extends BaseActivity {

    @BindView(R.id.text_toolbar_title) TextView tvToolbarTitle;

    @BindView(R.id.edit_name) EditText etName;
    @BindView(R.id.edit_email) EditText etEmail;
    @BindView(R.id.edit_password) EditText etPassword;
    @BindView(R.id.button_signup) Button btnSignUp;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override protected void initView() {
        tvToolbarTitle.setText(R.string.title_sign_up);
    }

    @OnTextChanged({R.id.edit_password, R.id.edit_name, R.id.edit_email})
    public void onTextChanged() {
        String username = etName.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password)) {
            btnSignUp.setEnabled(false);
        } else {
            btnSignUp.setEnabled(true);
        }
    }

    @OnClick(R.id.button_signup)
    public void signUp() {
        String username = etName.getText().toString();
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();


        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password)) {
            throw new IllegalStateException("Username, Password or Email is empty.");
        }

        ApiSignUp apiSignIn = new ApiSignUp(username, email, Sha256Utils.bin2hex(password));

        showProgress();
        enqueue(apiSignIn, new UiCallback<ApiSignUp.Rsp>(this) {
            @Override
            public void onBusinessResponse(boolean success, ApiSignUp.Rsp responseEntity) {
                dismissProgress();
                if (success) {
                    Session session = responseEntity.getData().getSession();
                    User user = responseEntity.getData().getUser();
                    UserManager.getInstance().update(session, user);
                    finish();
                }
            }
        });
    }
}
