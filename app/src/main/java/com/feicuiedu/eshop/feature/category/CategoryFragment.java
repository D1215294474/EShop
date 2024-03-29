package com.feicuiedu.eshop.feature.category;


import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.search.SearchGoodsActivity;
import com.feicuiedu.eshop.network.api.ApiCategory;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.CategoryPrimary;
import com.feicuiedu.eshop.network.entity.Filter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * 分类页面.
 */
public class CategoryFragment extends BaseFragment {

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @BindView(R.id.list_category) ListView categoryListView;
    @BindView(R.id.list_children) ListView childrenListView;

    private CategoryAdapter mCategoryAdapter;
    private ChildrenAdapter mChildrenAdapter;

    private List<CategoryPrimary> mData;

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_category;
    }

    @Override protected void initView() {

        new ToolbarWrapper(this)
                .setShowBack(false)
                .setShowTitle(false)
                .setCustomTitle(R.string.category_title);

        mCategoryAdapter = new CategoryAdapter();
        categoryListView.setAdapter(mCategoryAdapter);

        mChildrenAdapter = new ChildrenAdapter();
        childrenListView.setAdapter(mChildrenAdapter);

        if (mData != null) {
            updateCategory();
        } else {
            enqueue(new ApiCategory());
        }
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {
        if (!ApiPath.CATEGORY.equals(apiPath)) {
            throw new UnsupportedOperationException(apiPath);
        }

        if (success) {
            ApiCategory.Rsp categoryRsp = (ApiCategory.Rsp) rsp;
            mData = categoryRsp.getData();
            updateCategory();
        }
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_search) {
            int position = categoryListView.getCheckedItemPosition();
            int categoryId = mCategoryAdapter.getItem(position).getId();
            navigateToSearch(categoryId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        // 请求失败的简化处理方案: 界面切换时触发重新请求.
        if (!hidden && mData == null) {
            enqueue(new ApiCategory());
        }
    }


    @OnItemClick(R.id.list_category) void onItemClick(int position) {
        chooseCategory(position);
    }

    @OnItemClick(R.id.list_children) void onChildrenItemClick(int position) {
        int categoryId = mChildrenAdapter.getItem(position).getId();
        navigateToSearch(categoryId);
    }

    private void updateCategory() {
        mCategoryAdapter.reset(mData);
        chooseCategory(0);
    }

    private void chooseCategory(int position) {
        // http://stackoverflow.com/questions/27335355/setselected-works-buggy-with-listview
        categoryListView.setItemChecked(position, true);

        mChildrenAdapter.reset(mCategoryAdapter.getItem(position).getChildren());
    }

    private void navigateToSearch(int categoryId) {
        Filter filter = new Filter();
        filter.setCategoryId(categoryId);
        Intent intent = SearchGoodsActivity.getStartIntent(getContext(), filter);
        getActivity().startActivity(intent);
    }
}
