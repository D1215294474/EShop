package com.feicuiedu.eshop.base;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.feicuiedu.eshop.R;

import butterknife.ButterKnife;

/**
 * 通用Activity基类.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        setTransitionAnimation(true);
    }

    @Override public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        setTransitionAnimation(true);
    }

    @Override
    public void startActivityFromFragment(@NonNull Fragment fragment,
                                          Intent intent,
                                          int requestCode) {
        super.startActivityFromFragment(fragment, intent, requestCode);
        setTransitionAnimation(true);
    }

    @Override public void finish() {
        super.finish();
        setTransitionAnimation(false);
    }

    public void finishWithDefaultTransition() {
        super.finish();
    }

    private void setTransitionAnimation(boolean newActivityIn) {
        if (newActivityIn) {
            // 新页面从右边进入
            overridePendingTransition(R.anim.push_right_in,
                    R.anim.push_right_out);
        } else {
            // 上一个页面从左边进入
            overridePendingTransition(R.anim.push_left_in,
                    R.anim.push_left_out);
        }

    }

}
