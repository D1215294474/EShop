package com.feicuiedu.eshop.feature.cart;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseListAdapter;
import com.feicuiedu.eshop.base.glide.GlideUtils;
import com.feicuiedu.eshop.base.widgets.SimpleNumberPicker;
import com.feicuiedu.eshop.network.entity.CartGoods;
import com.feicuiedu.eshop.network.entity.Picture;

import butterknife.BindView;

public abstract class CartGoodsAdapter extends
        BaseListAdapter<CartGoods, CartGoodsAdapter.ViewHolder> {


    @Override protected int getItemViewLayout() {
        return R.layout.item_cart_goods;
    }

    @Override protected ViewHolder getItemViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void bindItem(int position, final CartGoods item, ViewHolder viewHolder) {
        viewHolder.tvName.setText(item.getGoodsName());
        viewHolder.tvPrice.setText(item.getTotalPrice());
        viewHolder.numberPicker.setNumber(item.getGoodsNumber());

        viewHolder.numberPicker
                .setOnNumberChangedListener(new SimpleNumberPicker.OnNumberChangedListener() {
                    @Override public void onNumberChanged(int number) {
                        numberChanged(item, number);
                    }
                });

        Picture picture = item.getImg();

        GlideUtils.loadPicture(picture, viewHolder.ivGoods);
    }

    public static class ViewHolder extends BaseListAdapter.ViewHolder {

        @BindView(R.id.image_goods) ImageView ivGoods;
        @BindView(R.id.text_goods_name) TextView tvName;
        @BindView(R.id.text_goods_price) TextView tvPrice;
        @BindView(R.id.number_picker) SimpleNumberPicker numberPicker;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public abstract void numberChanged(CartGoods goods, int number);
}
