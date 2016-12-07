package com.feicuiedu.eshop.feature.category;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.search.SearchGoodsActivity;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.api.ApiCategory;
import com.feicuiedu.eshop.network.entity.Filter;

import butterknife.BindView;
import butterknife.OnItemClick;

/**
 * 分类页面.
 */
public class CategoryFragment extends BaseFragment {

    @BindView(R.id.list_category) ListView categoryListView;
    @BindView(R.id.list_children) ListView childrenListView;

    private CategoryAdapter mCategoryAdapter;
    private ChildrenAdapter mChildrenAdapter;

    private boolean mLoadSuccess;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoryAdapter = new CategoryAdapter();
        mChildrenAdapter = new ChildrenAdapter();

        getCategories();
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new ToolbarWrapper(this)
                .setShowBack(false)
                .setShowTitle(false)
                .setCustomTitle(R.string.title_category);
        categoryListView.setAdapter(mCategoryAdapter);
        childrenListView.setAdapter(mChildrenAdapter);
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

        if (!hidden && !mLoadSuccess) {
            getCategories();
        }
    }

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_category;
    }

    @OnItemClick(R.id.list_category)
    public void onItemClick(int position) {
        chooseCategory(position);
    }

    @OnItemClick(R.id.list_children)
    public void onChildrenItemClick(int position) {
        int categoryId = mChildrenAdapter.getItem(position).getId();
        navigateToSearch(categoryId);
    }

    private void getCategories() {
        ApiCategory api = new ApiCategory();
        enqueue(api, new UiCallback<ApiCategory.Rsp>(getContext()) {
            @Override
            public void onBusinessResponse(boolean success, ApiCategory.Rsp responseEntity) {
                if (success) {
                    mLoadSuccess = true;
                    mCategoryAdapter.reset(responseEntity.getData());
                    chooseCategory(0);
                }
            }
        });
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
