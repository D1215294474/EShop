package com.feicuiedu.eshop.base.widgets.image;


import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TopCropImageView extends ImageView {
    public TopCropImageView(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public TopCropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    public TopCropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        recomputeImgMatrix(r - l, b - t);
        return super.setFrame(l, t, r, b);
    }

    private void recomputeImgMatrix(int viewWidth, int viewHeight) {

        if (getDrawable() == null) {
            return;
        }
        Matrix matrix = getImageMatrix();

        float scale;

        final int drawableWidth = getDrawable().getIntrinsicWidth();
        final int drawableHeight = getDrawable().getIntrinsicHeight();

        if (viewWidth == 0 || viewHeight == 0 || drawableWidth == 0 || drawableHeight == 0) {
            return;
        }

        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            scale = (float) viewHeight / (float) drawableHeight;
            matrix.setScale(scale, scale);

            float translateX = (drawableWidth * scale - viewWidth) / 2;
            matrix.postTranslate(-translateX, 0);
        } else {
            scale = (float) viewWidth / (float) drawableWidth;
            matrix.setScale(scale, scale);
        }
        setImageMatrix(matrix);
    }
}
