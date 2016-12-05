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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                .bitmapTransform(new TopCropTransformation(context))
                .into(imageView);
    }

    @SafeVarargs
    public static void loadPicture(Picture picture,
                                   ImageView imageView,
                                   int placeholder,
                                   Transformation<Bitmap>... transformations) {

        Context context = imageView.getContext();

        List<Transformation<Bitmap>> transList = new ArrayList<>();
        transList.addAll(Arrays.asList(transformations));
        transList.add(new BlurTransformation(context, 5));


        DrawableRequestBuilder<String> thumbnailRequest = Glide
                .with(context)
                .load(picture.getSmall())
                .bitmapTransform(transList.toArray(transformations));

        transList.clear();
        transList.addAll(Arrays.asList(transformations));
        transList.add(new TopCropTransformation(context));

        DrawableRequestBuilder<String> pictureRequest = Glide
                .with(context)
                .load(picture.getMiddle())
                .thumbnail(thumbnailRequest)
                .placeholder(placeholder)
                .error(R.drawable.ic_loading_failure_big)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(transList.toArray(transformations));

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
