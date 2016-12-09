package com.feicuiedu.eshop.feature.goods.details;


import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseFragment;
import com.feicuiedu.eshop.network.core.ResponseEntity;

public class GoodsDetailsFragment extends BaseFragment {

    public static GoodsDetailsFragment newInstance() {
        return new GoodsDetailsFragment();
    }

    @Override protected int getContentViewLayout() {
        return R.layout.fragment_goods_details;
    }

    @Override protected void initView() {

    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {

    }

}
