package com.feicuiedu.eshop.feature.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.base.widgets.SimpleSearchView;
import com.feicuiedu.eshop.base.widgets.loadmore.EndlessScrollListener;
import com.feicuiedu.eshop.base.widgets.loadmore.LoadMoreFooter;
import com.feicuiedu.eshop.base.wrapper.PtrWrapper;
import com.feicuiedu.eshop.base.wrapper.ToolbarWrapper;
import com.feicuiedu.eshop.feature.goods.GoodsActivity;
import com.feicuiedu.eshop.network.api.ApiSearch;
import com.feicuiedu.eshop.network.core.ApiPath;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Filter;
import com.feicuiedu.eshop.network.entity.Paginated;
import com.feicuiedu.eshop.network.entity.Pagination;
import com.feicuiedu.eshop.network.entity.SimpleGoods;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import butterknife.OnItemClick;
import okhttp3.Call;

/**
 * 搜索商品页面.
 */
public class SearchGoodsActivity extends BaseActivity {

    private static final String EXTRA_SEARCH_FILTER = "EXTRA_SEARCH_FILTER";

    public static Intent getStartIntent(Context context, @Nullable Filter filter) {

        if (filter == null) filter = new Filter();

        Intent intent = new Intent(context, SearchGoodsActivity.class);
        intent.putExtra(EXTRA_SEARCH_FILTER, new Gson().toJson(filter));
        return intent;
    }

    @BindView(R.id.search_view) SimpleSearchView mSearchView;
    @BindView(R.id.list_goods) ListView goodsListView;
    @BindViews({R.id.text_is_hot, R.id.text_most_expensive, R.id.text_cheapest})
    List<TextView> tvOrderList;

    private Filter mFilter;
    private final Pagination mPagination = new Pagination();
    private boolean mHasMore;
    private SearchGoodsAdapter mGoodsAdapter;
    private LoadMoreFooter mFooter;
    private PtrWrapper mPtrWrapper;
    private Call mSearchCall;

    @Override protected int getContentViewLayout() {
        return R.layout.activity_search_goods;
    }

    @Override protected void initView() {
        new ToolbarWrapper(this);
        tvOrderList.get(0).setActivated(true);

        String filterStr = getIntent().getStringExtra(EXTRA_SEARCH_FILTER);
        mFilter = new Gson().fromJson(filterStr, Filter.class);

        mPtrWrapper = new PtrWrapper(this) {
            @Override public void onRefresh() {
                searchGoods(true);
            }
        };

        mSearchView.setOnSearchListener(new SimpleSearchView.OnSearchListener() {
            @Override public void search(String query) {
                mFilter.setKeywords(query);
                mPtrWrapper.autoRefresh();
            }
        });

        mGoodsAdapter = new SearchGoodsAdapter();
        goodsListView.setAdapter(mGoodsAdapter);
        mFooter = new LoadMoreFooter(this);
        goodsListView.addFooterView(mFooter);
        goodsListView.setOnScrollListener(new EndlessScrollListener(0, 1) {
            @Override public boolean onLoadMore(int page, int totalItemsCount) {

                if (mHasMore && mSearchCall == null) {
                    mFooter.setState(LoadMoreFooter.STATE_LOADING);
                    searchGoods(false);
                    return true;
                }
                return false;
            }
        });

        mPtrWrapper.postRefresh(50);
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {
        if (!ApiPath.SEARCH.equals(apiPath)) {
            throw new UnsupportedOperationException(apiPath);
        }

        mPtrWrapper.stopRefresh();

        if (success) {

            ApiSearch.Rsp searchRsp = (ApiSearch.Rsp) rsp;

            Paginated paginated = searchRsp.getPaginated();

            if (mPagination.getPage() * paginated.getCount() > paginated.getTotal()) {
                throw new IllegalStateException("Load more data than needed!");
            }

            mHasMore = paginated.hasMore();
            List<SimpleGoods> goodsList = searchRsp.getData();

            if (mHasMore) {
                mFooter.setState(LoadMoreFooter.STATE_LOADED);
            } else {
                mFooter.setState(LoadMoreFooter.STATE_COMPLETE);
            }

            if (mPagination.isFirst()) {
                mGoodsAdapter.reset(goodsList);
            } else {
                mGoodsAdapter.addAll(goodsList);
            }

        }

        mSearchCall = null;

    }


    @OnClick({R.id.text_is_hot, R.id.text_most_expensive, R.id.text_cheapest})
    void chooseQueryOrder(TextView textView) {

        if (mSearchCall != null) return;

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
        mPtrWrapper.autoRefresh();
    }

    @OnItemClick(R.id.list_goods) void onItemClick(int position) {
        SimpleGoods simpleGoods = (SimpleGoods) goodsListView.getItemAtPosition(position);
        if (simpleGoods != null) {
            Intent intent = GoodsActivity.getStartIntent(this, simpleGoods.getId());
            startActivity(intent);
        }
    }

    private void searchGoods(final boolean isRefresh) {

        if (mSearchCall != null) {
            throw new UnsupportedOperationException();
        }

        if (isRefresh) {
            mPagination.reset();
            goodsListView.setSelection(0);
        } else {
            mPagination.next();
        }

        ApiSearch apiSearch = new ApiSearch(mFilter, mPagination);
        mSearchCall = enqueue(apiSearch);
    }


}
