package com.feicuiedu.eshop.feature.category;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.api.ApiCategory;

import butterknife.BindView;
import butterknife.OnItemClick;
import okhttp3.Call;

/**
 * 分类页面.
 */
public class CategoryFragment extends BaseFragment {


    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list_category) ListView categoryListView;
    @BindView(R.id.list_children) ListView childrenListView;

    private CategoryAdapter mCategoryAdapter;
    private ChildrenAdapter mChildrenAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategoryAdapter = new CategoryAdapter();
        mChildrenAdapter = new ChildrenAdapter();

        ApiCategory api = new ApiCategory();
        Call call = client.enqueue(api, new UiCallback<ApiCategory.Rsp>(getContext()) {
            @Override
            public void onBusinessResponse(boolean success, ApiCategory.Rsp responseEntity) {
                if (success) {
                    mCategoryAdapter.reset(responseEntity.getData());
                    chooseCategory(0);
                }
            }
        });
        saveCall(call);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_category, menu);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        categoryListView.setAdapter(mCategoryAdapter);
        childrenListView.setAdapter(mChildrenAdapter);
    }

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_category;
    }

    @Override protected int getTitleId() {
        return R.string.title_category;
    }

    @OnItemClick(R.id.list_category)
    public void onItemClick(int position) {
        chooseCategory(position);
    }

    private void chooseCategory(int position) {
        // http://stackoverflow.com/questions/27335355/setselected-works-buggy-with-listview
        categoryListView.setItemChecked(position, true);

        mChildrenAdapter.reset(mCategoryAdapter.getItem(position).getChildren());
    }
}
