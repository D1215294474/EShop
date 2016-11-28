package com.feicuiedu.eshop.base;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.network.EShopClient;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 通用Activity基类.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected EShopClient client = EShopClient.getInstance();

    // 使用弱引用缓存所有Call对象, 在Fragment销毁时统一取消, 避免内存溢出.
    private final List<WeakReference<Call>> mCallList = new ArrayList<>();

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        // 取消所有网络请求, 避免内存溢出
        for (WeakReference<Call> reference : mCallList) {
            Call call = reference.get();
            if (call != null) call.cancel();
        }
    }

    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        setTransitionAnimation(true);
    }

    @Override public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        setTransitionAnimation(true);
    }

    @Override public void finish() {
        super.finish();
        setTransitionAnimation(false);
    }

    public void finishWithDefaultTransition() {
        super.finish();
    }

    protected void saveCall(Call call) {
        mCallList.add(new WeakReference<>(call));
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
