package com.feicuiedu.eshop.feature.goods.info;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.feature.goods.GoodsActivity;
import com.feicuiedu.eshop.network.entity.GoodsInfo;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

/**
 * 商品信息Fragment. 此Fragment不可直接实例化, 需使用{@link #newInstance(GoodsInfo)}方法创建新对象.
 * <p/>
 * 此Fragment主要显示{@link GoodsInfo}实体中的信息.
 */
public class GoodsInfoFragment extends BaseFragment {

    private static final String ARGUMENT_GOODS_INFO = "ARGUMENT_GOODS_INFO";

    @BindView(R.id.pager_goods_pictures) ViewPager picturesPager; // 用于显示商品图片
    @BindView(R.id.indicator) CircleIndicator circleIndicator; // ViewPager的圆点指示器
    @BindView(R.id.text_goods_name) TextView tvGoodsName; // 商品名称
    @BindView(R.id.text_goods_price) TextView tvGoodsPrice; // 商品价格
    @BindView(R.id.text_market_price) TextView tvMarketPrice; // 商场价格

    private GoodsInfo mGoodsInfo;

    /**
     * @param goodsInfo 待显示的商品信息实体
     * @return 新的GoodsInfoFragment对象
     */
    public static GoodsInfoFragment newInstance(GoodsInfo goodsInfo) {

        Bundle args = new Bundle();
        args.putString(ARGUMENT_GOODS_INFO, new Gson().toJson(goodsInfo));

        GoodsInfoFragment fragment = new GoodsInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取传入的商品信息实体
        mGoodsInfo = new Gson().fromJson(getArguments().getString(ARGUMENT_GOODS_INFO),
                GoodsInfo.class);
    }

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_goods_info;
    }

    @Override protected void initView() {
        // 设置显示商品图片的ViewPager
        GoodsPictureAdapter adapter = new GoodsPictureAdapter(mGoodsInfo.getPictures());
        picturesPager.setAdapter(adapter);
        circleIndicator.setViewPager(picturesPager);


        // 设置商品名称, 价格等信息
        tvGoodsName.setText(mGoodsInfo.getName());
        tvGoodsPrice.setText(mGoodsInfo.getShopPrice());

        // 设置商场价格, 并添加删除线
        String marketPrice = mGoodsInfo.getMarketPrice();
        SpannableString spannableString = new SpannableString(marketPrice);
        spannableString.setSpan(
                new StrikethroughSpan(), 0, marketPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMarketPrice.setText(spannableString);
    }

    @OnClick(R.id.text_goods_comments)
    public void changeToComments() {
        // 切换到商品评价Fragment
        GoodsActivity activity = (GoodsActivity) getActivity();
        activity.selectPage(2);
    }
}
