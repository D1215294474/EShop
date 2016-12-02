package com.feicuiedu.eshop.feature.search;


import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseListAdapter;
import com.feicuiedu.eshop.base.glide.GlideUtils;
import com.feicuiedu.eshop.network.entity.Picture;
import com.feicuiedu.eshop.network.entity.SimpleGoods;

import butterknife.BindView;

public class SearchGoodsAdapter extends
        BaseListAdapter<SimpleGoods, SearchGoodsAdapter.ViewHolder> {


    @Override protected int getItemViewLayout() {
        return R.layout.item_search_goods;
    }

    @Override protected ViewHolder getItemViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindItem(int position, SimpleGoods item, ViewHolder viewHolder) {
        viewHolder.tvName.setText(item.getName());
        viewHolder.tvPrice.setText(item.getShopPrice());

        // 设置商场价格, 并添加删除线
        String marketPrice = item.getMarketPrice();
        SpannableString spannableString = new SpannableString(marketPrice);
        spannableString.setSpan(
                new StrikethroughSpan(), 0, marketPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.tvMarketPrice.setText(spannableString);

        Context context = viewHolder.itemView.getContext();
        Picture picture = item.getImg();

        GlideUtils.loadPicture(picture, viewHolder.ivGoods);
    }

    public static class ViewHolder extends BaseListAdapter.ViewHolder {

        @BindView(R.id.image_goods) ImageView ivGoods;
        @BindView(R.id.text_goods_name) TextView tvName;
        @BindView(R.id.text_goods_price) TextView tvPrice;
        @BindView(R.id.text_market_price) TextView tvMarketPrice;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
