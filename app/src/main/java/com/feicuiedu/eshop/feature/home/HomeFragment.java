package com.feicuiedu.eshop.feature.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.base.glide.GlideUtils;
import com.feicuiedu.eshop.base.glide.MaskTransformation;
import com.feicuiedu.eshop.base.widgets.banner.BannerAdapter;
import com.feicuiedu.eshop.base.widgets.banner.BannerLayout;
import com.feicuiedu.eshop.base.wrapper.PtrWrapper;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.goods.GoodsActivity;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.api.ApiHomeBanner;
import com.feicuiedu.eshop.network.api.ApiHomeCategory;
import com.feicuiedu.eshop.network.entity.Banner;
import com.feicuiedu.eshop.network.entity.SimpleGoods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

/**
 * 主页面.
 */
public class HomeFragment extends BaseFragment {

    private static final int[] PROMOTE_COLORS = {
            R.color.purple,
            R.color.orange,
            R.color.pink,
            R.color.colorPrimary
    };

    private static final int[] PROMOTE_PLACE_HOLDER = {
            R.drawable.round_purple,
            R.drawable.round_orange,
            R.drawable.round_pink,
            R.drawable.round_yellow
    };

    @BindView(R.id.list_home_goods) ListView goodsListView;

    private HomeGoodsAdapter mGoodsAdapter;
    private BannerAdapter<Banner> mBannerAdapter;
    private PtrWrapper mPtrWrapper;

    private boolean mBannerRefreshed = false;
    private boolean mCategoryRefreshed = false;

    private ImageView[] mIvPromotes = new ImageView[4];
    private TextView mTvPromoteGoods;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoodsAdapter = new HomeGoodsAdapter();

        mBannerAdapter = new BannerAdapter<Banner>() {
            @Override protected void bind(ViewHolder holder, Banner data) {
                GlideUtils.loadBanner(data.getPicture(), holder.ivBannerItem);
            }
        };
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        new ToolbarWrapper(this).setCustomTitle(R.string.title_home);
        goodsListView.setAdapter(mGoodsAdapter);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.partial_home_header, goodsListView, false);

        BannerLayout bannerLayout = ButterKnife.findById(view, R.id.layout_banner);
        bannerLayout.setBannerAdapter(mBannerAdapter);

        mIvPromotes[0] = ButterKnife.findById(view, R.id.image_promote_one);
        mIvPromotes[1] = ButterKnife.findById(view, R.id.image_promote_two);
        mIvPromotes[2] = ButterKnife.findById(view, R.id.image_promote_three);
        mIvPromotes[3] = ButterKnife.findById(view, R.id.image_promote_four);

        mTvPromoteGoods = ButterKnife.findById(view, R.id.text_promote_goods);

        goodsListView.addHeaderView(view);

        mPtrWrapper = new PtrWrapper(this) {
            @Override public void onRefresh() {
                mBannerRefreshed = false;
                mCategoryRefreshed = false;

                enqueue(new ApiHomeBanner(), new BannerCallback(getContext()));

                enqueue(new ApiHomeCategory(), new CategoryCallback(getContext()));
            }
        };
        mPtrWrapper.postRefresh(50);
    }

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }


    private void setPromoteGoods(final List<SimpleGoods> simpleGoodsList) {

        mTvPromoteGoods.setVisibility(View.VISIBLE);

        for (int i = 0; i < mIvPromotes.length; i++) {

            if (i < simpleGoodsList.size()) {
                mIvPromotes[i].setVisibility(View.VISIBLE);
                final SimpleGoods simpleGoods = simpleGoodsList.get(i);

                GlideUtils.loadPicture(simpleGoods.getImg(),
                        mIvPromotes[i],
                        PROMOTE_PLACE_HOLDER[i],
                        new CropCircleTransformation(getContext()),
                        new GrayscaleTransformation(getContext()),
                        new MaskTransformation(getContext(), PROMOTE_COLORS[i]));

                mIvPromotes[i].setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent = GoodsActivity.getStartIntent(
                                getContext(), simpleGoods.getId());
                        getActivity().startActivity(intent);
                    }
                });
            } else {
                mIvPromotes[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private class BannerCallback extends UiCallback<ApiHomeBanner.Rsp> {

        public BannerCallback(Context context) {
            super(context);
        }

        @Override
        public void onBusinessResponse(boolean success, ApiHomeBanner.Rsp responseEntity) {
            if (!isViewBind()) {
                return;
            }

            mBannerRefreshed = true;
            if (mCategoryRefreshed) {
                mPtrWrapper.stopRefresh();
            }

            if (success) {
                mBannerAdapter.reset(responseEntity.getData().getBanners());
                setPromoteGoods(responseEntity.getData().getGoodsList());
            }
        }
    }

    private class CategoryCallback extends UiCallback<ApiHomeCategory.Rsp> {

        public CategoryCallback(Context context) {
            super(context);
        }

        @Override
        public void onBusinessResponse(boolean success, ApiHomeCategory.Rsp responseEntity) {
            if (!isViewBind()) {
                return;
            }

            mCategoryRefreshed = true;
            if (mBannerRefreshed) {
                mPtrWrapper.stopRefresh();
            }

            if (success) {
                mGoodsAdapter.reset(responseEntity.getData());
            }
        }
    }
}
