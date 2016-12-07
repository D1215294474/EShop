package com.feicuiedu.eshop.feature.home;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseListAdapter;
import com.feicuiedu.eshop.base.glide.GlideUtils;
import com.feicuiedu.eshop.base.widgets.ImageGrid;
import com.feicuiedu.eshop.feature.goods.GoodsActivity;
import com.feicuiedu.eshop.network.entity.CategoryHome;
import com.feicuiedu.eshop.network.entity.Picture;
import com.feicuiedu.eshop.network.entity.SimpleGoods;

import java.util.List;

import butterknife.BindView;

public class HomeGoodsAdapter extends BaseListAdapter<CategoryHome, HomeGoodsAdapter.ViewHolder> {


    @Override protected int getItemViewLayout() {
        return R.layout.item_home_goods;
    }

    @Override protected ViewHolder getItemViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindItem(int position, CategoryHome item, ViewHolder viewHolder) {

        final Context context = viewHolder.tvCategory.getContext();

        viewHolder.tvCategory.setText(item.getName());

        ImageView[] imageViews = viewHolder.imageGrid.getImageViews();
        List<SimpleGoods> goodsList = item.getHotGoodsList();

        int goodsCount = goodsList.size();
        for (int i = 0; i < imageViews.length; i++) {
            if (i < goodsCount) {
                final SimpleGoods simpleGoods = goodsList.get(i);

                Picture picture = goodsList.get(i).getImg();
                GlideUtils.loadPicture(picture, imageViews[i]);

                imageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent = GoodsActivity.getStartIntent(context, simpleGoods.getId());
                        context.startActivity(intent);
                    }
                });
            } else {
                Glide.clear(imageViews[i]);
                imageViews[i].setImageDrawable(null);
                imageViews[i].setOnClickListener(null);
            }
        }
    }


    public static class ViewHolder extends BaseListAdapter.ViewHolder {

        @BindView(R.id.text_category) TextView tvCategory;
        @BindView(R.id.grid_image) ImageGrid imageGrid;

        public ViewHolder(View itemView) {
            super(itemView);
            imageGrid.shuffle();
        }
    }
}
