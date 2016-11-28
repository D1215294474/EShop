package com.feicuiedu.eshop.feature.goods.info;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.network.entity.Picture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品图片的适配器, 用于{@link GoodsInfoFragment}.
 */
public class GoodsPictureAdapter extends PagerAdapter {


    private final List<Picture> mPictureList = new ArrayList<>();
    
    public GoodsPictureAdapter(List<Picture> pictures) {
        mPictureList.addAll(pictures);
    }

    @Override public int getCount() {
        return mPictureList.size();
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setBackgroundResource(R.drawable.image_bg);
        container.addView(imageView);
        Picasso.with(container.getContext())
                .load(mPictureList.get(position).getLarge())
                .into(imageView);
        return imageView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imageView = (ImageView) object;
        container.removeView(imageView);
    }

}

