package com.feicuiedu.eshop.feature.category;


import android.view.View;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseListAdapter;
import com.feicuiedu.eshop.network.entity.CategoryBase;

import butterknife.BindView;

public class ChildrenAdapter extends BaseListAdapter<CategoryBase, ChildrenAdapter.ViewHolder> {


    @Override protected int getItemViewLayout() {
        return R.layout.item_children_category;
    }

    @Override protected ViewHolder getItemViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseListAdapter.ViewHolder {

        @BindView(R.id.text_category) TextView tvCategory;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override protected void bind(int position) {
            tvCategory.setText(getItem(position).getName());
        }
    }
}
