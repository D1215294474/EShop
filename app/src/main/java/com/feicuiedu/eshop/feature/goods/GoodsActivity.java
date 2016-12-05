package com.feicuiedu.eshop.feature.goods;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.feature.goods.comments.GoodsCommentsFragment;
import com.feicuiedu.eshop.feature.goods.details.GoodsDetailsFragment;
import com.feicuiedu.eshop.feature.goods.info.GoodsInfoFragment;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.api.ApiGoodsInfo;
import com.feicuiedu.eshop.network.entity.GoodsInfo;
import com.feicuiedu.eshop.network.entity.SimpleGoods;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * 商品页, 包含三个子页面: <br/>
 * <li>商品信息 {@link GoodsInfoFragment}
 * <li>商品详情 {@link GoodsDetailsFragment}
 * <li>商品评价 {@link GoodsCommentsFragment}.
 */
public class GoodsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private static final String EXTRA_SIMPLE_GOODS = "EXTRA_SIMPLE_GOODS";

    /**
     * @param context     上下文对象
     * @param simpleGoods 简单商品实体
     * @return 用于启动此Activity的Intent对象
     */
    public static Intent getStartIntent(Context context, SimpleGoods simpleGoods) {
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtra(EXTRA_SIMPLE_GOODS, new Gson().toJson(simpleGoods));
        return intent;
    }

    @BindViews({R.id.text_tab_goods, R.id.text_tab_details, R.id.text_tab_comments})
    List<TextView> tvTabList;

    @BindView(R.id.pager_goods) ViewPager goodsPager;

    private SimpleGoods mSimpleGoods;
    private GoodsInfo mGoodsInfo;

    private GoodsSpecPopupWindow mGoodsSpecPopupWindow;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String extra = getIntent().getStringExtra(EXTRA_SIMPLE_GOODS);
        mSimpleGoods = new Gson().fromJson(extra, SimpleGoods.class);

        setContentView(R.layout.activity_goods);
        enqueue(new ApiGoodsInfo(mSimpleGoods.getId()),
                new GoodsInfoCallback(this));
    }

    @Override public void initView() {
        goodsPager.addOnPageChangeListener(this);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_goods, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_share) {
            Toast.makeText(this, R.string.share, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.text_tab_goods, R.id.text_tab_details, R.id.text_tab_comments})
    public void onClickTab(TextView textView) {
        int position = tvTabList.indexOf(textView);
        goodsPager.setCurrentItem(position, false);
        chooseTab(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override public void onPageSelected(int position) {
        chooseTab(position);
    }

    @Override public void onPageScrollStateChanged(int state) {
    }

    @OnClick({R.id.button_show_cart, R.id.button_add_cart, R.id.button_buy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_show_cart:
                break;
            case R.id.button_add_cart:

                if (mGoodsInfo == null) return;

                if (mGoodsSpecPopupWindow == null) {
                    mGoodsSpecPopupWindow = new GoodsSpecPopupWindow(this, mGoodsInfo);
                }
                mGoodsSpecPopupWindow.show();

                break;
            case R.id.button_buy:
                break;
            default:
                throw new UnsupportedOperationException("Unsupported View Id");
        }
    }

    public void selectPage(int position) {
        goodsPager.setCurrentItem(position, false);
        chooseTab(position);
    }


    // 选择Tab标签, 注意此方法只改变Tab标签的UI效果, 不会改变ViewPager的位置.
    private void chooseTab(int position) {
        Resources res = getResources();
        for (int i = 0; i < tvTabList.size(); i++) {
            tvTabList.get(i).setSelected(i == position);
            float textSize = i == position ? res.getDimension(R.dimen.font_large) :
                    res.getDimension(R.dimen.font_normal);
            tvTabList.get(i).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    private class GoodsInfoCallback extends UiCallback<ApiGoodsInfo.Rsp> {

        public GoodsInfoCallback(Context context) {
            super(context);
        }

        @Override public void onBusinessResponse(boolean success, ApiGoodsInfo.Rsp responseEntity) {
            if (success) {
                mGoodsInfo = responseEntity.getData();
                goodsPager.setAdapter(new GoodsPagerAdapter(getSupportFragmentManager(),
                        mGoodsInfo));
                chooseTab(0);
            }
        }
    }

}
