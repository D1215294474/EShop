package com.feicuiedu.eshop.feature.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.widgets.EndlessScrollListener;
import com.feicuiedu.eshop.base.widgets.LoadMoreFooter;
import com.feicuiedu.eshop.base.widgets.SimpleSearchView;
import com.feicuiedu.eshop.base.widgets.ptr.RefreshHeader;
import com.feicuiedu.eshop.feature.goods.GoodsActivity;
import com.feicuiedu.eshop.network.UiCallback;
import com.feicuiedu.eshop.network.api.ApiSearch;
import com.feicuiedu.eshop.network.entity.Filter;
import com.feicuiedu.eshop.network.entity.Pagination;
import com.feicuiedu.eshop.network.entity.SimpleGoods;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import okhttp3.Call;

/**
 * 搜索商品页面.
 */
public class SearchGoodsActivity extends BaseActivity {

    private static final String EXTRA_SEARCH_FILTER = "EXTRA_SEARCH_FILTER";

    public static Intent getStartIntent(Context context, @Nullable Filter filter) {

        if (filter == null) filter = new Filter();

        Intent intent = new Intent(context, SearchGoodsActivity.class);
        intent.putExtra(EXTRA_SEARCH_FILTER, GSON.toJson(filter));
        return intent;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.search_view) SimpleSearchView mSearchView;
    @BindView(R.id.layout_refresh) PtrFrameLayout refreshLayout;
    @BindView(R.id.list_goods) ListView goodsListView;

    @BindViews({R.id.text_is_hot, R.id.text_most_expensive, R.id.text_cheapest})
    List<TextView> tvOrderList;

    private Filter mFilter;
    private final Pagination mPagination = new Pagination();
    private boolean mHasMore;
    private SearchGoodsAdapter mGoodsAdapter;
    private LoadMoreFooter mFooter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String filterStr = getIntent().getStringExtra(EXTRA_SEARCH_FILTER);
        mFilter = GSON.fromJson(filterStr, Filter.class);

        setContentView(R.layout.activity_search_goods);

        refreshLayout.postDelayed(new Runnable() {
            @Override public void run() {
                refreshLayout.autoRefresh();
            }
        }, 50);

    }

    @Override public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        setUpAppBar();

        tvOrderList.get(0).setActivated(true);

        refreshLayout.disableWhenHorizontalMove(true);
        RefreshHeader refreshHeader = new RefreshHeader(this);
        refreshLayout.setHeaderView(refreshHeader);
        refreshLayout.addPtrUIHandler(refreshHeader);
        refreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override public void onRefreshBegin(PtrFrameLayout frame) {
                SearchGoodsActivity.this.onRefreshBegin(frame);
            }
        });

        mSearchView.setOnSearchListener(new SimpleSearchView.OnSearchListener() {
            @Override public void search(String query) {
                mFilter.setKeywords(query);
                refreshLayout.autoRefresh();
            }
        });

        mGoodsAdapter = new SearchGoodsAdapter();
        goodsListView.setAdapter(mGoodsAdapter);
        mFooter = new LoadMoreFooter(this);
        goodsListView.addFooterView(mFooter);
        goodsListView.setOnScrollListener(new EndlessScrollListener(1, 1) {
            @Override public boolean onLoadMore(int page, int totalItemsCount) {

                if (mHasMore) {
                    mFooter.setState(LoadMoreFooter.STATE_LOADING);
                    searchGoods(false);
                    return true;
                }
                return false;
            }
        });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.text_is_hot, R.id.text_most_expensive, R.id.text_cheapest})
    public void chooseQueryOrder(TextView textView) {

        if (textView.isActivated()) return;

        for (TextView tv : tvOrderList) {
            tv.setActivated(false);
        }
        textView.setActivated(true);

        String sortBy;

        switch (textView.getId()) {
            case R.id.text_is_hot:
                sortBy = Filter.SORT_IS_HOT;
                break;
            case R.id.text_most_expensive:
                sortBy = Filter.SORT_PRICE_DESC;
                break;
            case R.id.text_cheapest:
                sortBy = Filter.SORT_PRICE_ASC;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        mFilter.setSortBy(sortBy);
        refreshLayout.autoRefresh();
    }

    @OnItemClick(R.id.list_goods)
    public void onItemClick(int position) {
        SimpleGoods simpleGoods = (SimpleGoods) goodsListView.getItemAtPosition(position);
        if (simpleGoods != null) {
            Intent intent = GoodsActivity.getStartIntent(this, simpleGoods);
            startActivity(intent);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setUpAppBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void searchGoods(final boolean isRefresh) {
        if (isRefresh) {
            mPagination.reset();
            goodsListView.setSelection(0);
        } else {
            mPagination.next();
        }

        ApiSearch apiSearch = new ApiSearch(mFilter, mPagination);
        Call call = CLIENT.enqueue(apiSearch, new UiCallback<ApiSearch.Rsp>(this) {
            @Override
            public void onBusinessResponse(boolean success, ApiSearch.Rsp responseEntity) {

                if (isRefresh) refreshLayout.refreshComplete();

                if (success) {
                    mHasMore = responseEntity.getPaginated().hasMore();
                    List<SimpleGoods> goodsList = responseEntity.getData();

                    if (isRefresh) {
                        mGoodsAdapter.reset(goodsList);
                    } else {
                        mGoodsAdapter.addAll(goodsList);
                    }

                }

                if (mHasMore) {
                    mFooter.setState(LoadMoreFooter.STATE_LOADED);
                } else {
                    mFooter.setState(LoadMoreFooter.STATE_COMPLETE);
                }
            }
        });
        saveCall(call);
    }

    private void onRefreshBegin(PtrFrameLayout frame) {
        searchGoods(true);
    }
}
