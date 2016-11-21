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

    @Override
    protected void bindItem(int position, CategoryBase item, ViewHolder viewHolder) {
        viewHolder.tvCategory.setText(item.getName());
    }

    public static class ViewHolder extends BaseListAdapter.ViewHolder {

        @BindView(R.id.text_category) TextView tvCategory;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
