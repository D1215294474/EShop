package com.feicuiedu.eshop.feature.goods.comments;


import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.network.core.ResponseEntity;

public class GoodsCommentsFragment extends BaseFragment {

    public static GoodsCommentsFragment newInstance() {
        return new GoodsCommentsFragment();
    }

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_goods_comments;
    }

    @Override protected void initView() {

    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {

    }


}
