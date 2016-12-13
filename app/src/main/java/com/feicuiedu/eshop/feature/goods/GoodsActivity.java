package com.feicuiedu.eshop.feature.goods;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.wrapper.ToastWrapper;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.goods.comments.GoodsCommentsFragment;
import com.feicuiedu.eshop.feature.goods.details.GoodsDetailsFragment;
import com.feicuiedu.eshop.feature.goods.info.GoodsInfoFragment;
import com.feicuiedu.eshop.network.api.ApiGoodsInfo;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.GoodsInfo;

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

    private static final String EXTRA_GOODS_ID = "EXTRA_GOODS_ID";

    /**
     * @param context 上下文对象
     * @param goodsId 商品Id
     * @return 用于启动此Activity的Intent对象
     */
    public static Intent getStartIntent(Context context, int goodsId) {
        Intent intent = new Intent(context, GoodsActivity.class);
        intent.putExtra(EXTRA_GOODS_ID, goodsId);
        return intent;
    }

    @BindViews({R.id.text_tab_goods, R.id.text_tab_details, R.id.text_tab_comments})
    List<TextView> tvTabList;
    @BindView(R.id.pager_goods) ViewPager goodsPager;

    private GoodsInfo mGoodsInfo;
    private GoodsSpecPopupWindow mGoodsSpecPopupWindow;

    @Override protected int getContentViewLayout() {
        return R.layout.activity_goods;
    }

    @Override protected void initView() {
        new ToolbarWrapper(this);
        goodsPager.addOnPageChangeListener(this);

        // 获取商品信息.
        int goodsId = getIntent().getIntExtra(EXTRA_GOODS_ID, 0);
        enqueue(new ApiGoodsInfo(goodsId));
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {
        if (!ApiPath.GOODS.equals(apiPath)) {
            throw new UnsupportedOperationException(apiPath);
        }

        if (success) {
            ApiGoodsInfo.Rsp goodsRsp = (ApiGoodsInfo.Rsp) rsp;
            mGoodsInfo = goodsRsp.getData();
            goodsPager.setAdapter(new GoodsPagerAdapter(getSupportFragmentManager(), mGoodsInfo));
            chooseTab(0);
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_goods, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_share) {
            ToastWrapper.show(R.string.share);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override public void onPageSelected(int position) {
        chooseTab(position);
    }

    @Override public void onPageScrollStateChanged(int state) {
    }

    public void selectPage(int position) {
        goodsPager.setCurrentItem(position, false);
        chooseTab(position);
    }

    @OnClick({R.id.text_tab_goods, R.id.text_tab_details, R.id.text_tab_comments})
    void onClickTab(TextView textView) {
        int position = tvTabList.indexOf(textView);
        goodsPager.setCurrentItem(position, false);
        chooseTab(position);
    }

    @OnClick({R.id.button_show_cart, R.id.button_add_cart, R.id.button_buy})
    void onClick(View view) {
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

}
