package com.feicuiedu.eshop.feature.home;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseListAdapter;
import com.feicuiedu.eshop.base.widgets.ImageGrid;
import com.feicuiedu.eshop.network.entity.CategoryHome;
import com.feicuiedu.eshop.network.entity.SimpleGoods;
import com.squareup.picasso.Picasso;

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
        viewHolder.tvCategory.setText(item.getName());

        ImageView[] imageViews = viewHolder.imageGrid.getImageViews();
        List<SimpleGoods> goodsList = item.getHotGoodsList();

        int goodsCount = goodsList.size();
        for (int i = 0; i < imageViews.length; i++) {
            if (i < goodsCount) {
                Picasso.with(viewHolder.imageGrid.getContext())
                        .load(goodsList.get(i).getImg().getLarge())
                        .into(imageViews[i]);
            } else {
                imageViews[i].setImageDrawable(null);
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
