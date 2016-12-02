package com.feicuiedu.eshop.base.glide;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.network.entity.Picture;

import java.util.Random;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class GlideUtils {

    private static final int[] PLACE_HOLDERS = {
            R.drawable.bg_1,
            R.drawable.bg_2,
            R.drawable.bg_3,
            R.drawable.bg_4,
            R.drawable.bg_5,
            R.drawable.bg_6,
            R.drawable.bg_7,
    };

    private GlideUtils() {
    }

    public static void loadBanner(Picture picture, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(picture.getLarge())
                .error(R.drawable.ic_loading_failure_big)
                .placeholder(R.drawable.banner_bg)
                .centerCrop()
                .priority(Priority.IMMEDIATE)
                .crossFade(200)
                .into(imageView);
    }

    public static void loadFullPicture(Picture picture, ImageView imageView) {
        Context context = imageView.getContext();
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(context)
                .load(picture.getSmall())
                .bitmapTransform(new BlurTransformation(context, 5));

        Glide.with(context)
                .load(picture.getLarge())
                .thumbnail(thumbnailRequest)
                .error(R.drawable.ic_loading_failure_big)
                .placeholder(getRandomPlaceholder())
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    @SafeVarargs
    public static void loadPicture(Picture picture,
                                   ImageView imageView,
                                   int placeholder,
                                   Transformation<Bitmap>... transformations) {

        Context context = imageView.getContext();
        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(context)
                .load(picture.getSmall())
                .bitmapTransform(new BlurTransformation(context, 5));

        if (transformations.length > 0) {
            thumbnailRequest.bitmapTransform(transformations);
        }


        DrawableRequestBuilder<String> pictureRequest = Glide
                .with(context)
                .load(picture.getMiddle())
                .thumbnail(thumbnailRequest)
                .placeholder(placeholder)
                .error(R.drawable.ic_loading_failure_big)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        if (transformations.length > 0) {
            pictureRequest.bitmapTransform(transformations);
        }

        pictureRequest.into(imageView);

    }

    @SafeVarargs
    public static void loadPicture(Picture picture,
                                   ImageView imageView,
                                   Transformation<Bitmap>... transformations) {

        loadPicture(picture, imageView,
                getRandomPlaceholder(),
                transformations);

    }

    private static int getRandomPlaceholder() {
        return PLACE_HOLDERS[new Random().nextInt(PLACE_HOLDERS.length)];
    }

}
