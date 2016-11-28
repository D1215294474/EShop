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
import com.feicuiedu.eshop.base.MaskTransformation;
import com.feicuiedu.eshop.base.widgets.banner.BannerAdapter;
import com.feicuiedu.eshop.base.widgets.banner.BannerLayout;
import com.feicuiedu.eshop.feature.goods.GoodsActivity;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.api.ApiHomeBanner;
import com.feicuiedu.eshop.network.api.ApiHomeCategory;
import com.feicuiedu.eshop.network.entity.Banner;
import com.feicuiedu.eshop.network.entity.SimpleGoods;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import okhttp3.Call;

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

    @BindView(R.id.list_home_goods) ListView goodsListView;

    private HomeGoodsAdapter mGoodsAdapter;
    private BannerAdapter<Banner> mBannerAdapter;

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
                Picasso.with(getContext())
                        .load(data.getPicture().getLarge())
                        .into(holder.ivBannerItem);
            }
        };
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        autoRefresh();
    }


    @Override protected int getContentViewLayout() {
        return R.layout.fragment_home;
    }

    @Override protected int getTitleId() {
        return R.string.title_home;
    }

    @Override protected void onRefreshBegin(final PtrFrameLayout frame) {

        mBannerRefreshed = false;
        mCategoryRefreshed = false;

        Call bannerCall = client.enqueue(new ApiHomeBanner(), new BannerCallback(getContext()));
        saveCall(bannerCall);

        Call categoryCall = client.enqueue(new ApiHomeCategory(),
                new CategoryCallback(getContext()));
        saveCall(categoryCall);
    }

    private void setPromoteGoods(final List<SimpleGoods> simpleGoodsList) {

        mTvPromoteGoods.setVisibility(View.VISIBLE);

        for (int i = 0; i < mIvPromotes.length; i++) {

            if (i < simpleGoodsList.size()) {
                mIvPromotes[i].setVisibility(View.VISIBLE);
                final SimpleGoods simpleGoods = simpleGoodsList.get(i);
                Picasso.with(getContext())
                        .load(simpleGoods.getImg().getLarge())
                        .transform(new CropCircleTransformation())
                        .transform(new GrayscaleTransformation())
                        .transform(new MaskTransformation(getContext(), PROMOTE_COLORS[i]))
                        .into(mIvPromotes[i]);
                mIvPromotes[i].setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent = GoodsActivity.getStartIntent(
                                getContext(), simpleGoods);
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
                assert refreshLayout != null;
                refreshLayout.refreshComplete();
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
                assert refreshLayout != null;
                refreshLayout.refreshComplete();
            }

            if (success) {
                mGoodsAdapter.reset(responseEntity.getData());
            }
        }
    }
}
