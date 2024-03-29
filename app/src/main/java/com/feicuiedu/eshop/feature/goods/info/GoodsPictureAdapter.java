package com.feicuiedu.eshop.feature.goods.info;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.glide.GlideUtils;
import com.feicuiedu.eshop.network.entity.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品图片的适配器, 用于{@link GoodsInfoFragment}.
 */
public abstract class GoodsPictureAdapter extends PagerAdapter {


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
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        ImageView imageView = (ImageView) inflater
                .inflate(R.layout.item_goods_picture, container, false);
        container.addView(imageView);

        final Picture picture = mPictureList.get(position);
        GlideUtils.loadFullPicture(picture, imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onImageClicked(picture);
            }
        });

        return imageView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imageView = (ImageView) object;
        container.removeView(imageView);
    }

    public abstract void onImageClicked(Picture picture);

}

