package com.feicuiedu.eshop.feature;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;

import butterknife.BindView;

/**
 * <p> App入口 - 启动页.
 * <p> 开始2秒的渐变动画, 然后跳转到主页面.
 */
public class SplashActivity extends BaseActivity implements Animator.AnimatorListener {

    @BindView(R.id.image_splash) ImageView ivSplash;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 设置全屏(不显示状态栏)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        startSplashAnimation();
    }

    @Override public void onAnimationStart(Animator animation) {
    }

    @Override public void onAnimationEnd(Animator animation) {
        Intent intent = new Intent(this, EShopHomeActivity.class);
        startActivity(intent);
        finishWithDefaultTransition();
    }

    @Override public void onAnimationCancel(Animator animation) {
    }

    @Override public void onAnimationRepeat(Animator animation) {
    }

    private void startSplashAnimation() {
        // 渐变动画
        ivSplash.setAlpha(0.3f);
        ivSplash.animate()
                .alpha(1.0f)
                .setDuration(2000)
                .setListener(this)
                .start();
    }
}
