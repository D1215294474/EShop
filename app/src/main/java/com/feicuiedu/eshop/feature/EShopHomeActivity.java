package com.feicuiedu.eshop.feature;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.BaseActivity;
import com.feicuiedu.eshop.feature.cart.CartFragment;
import com.feicuiedu.eshop.feature.category.CategoryFragment;
import com.feicuiedu.eshop.feature.home.HomeFragment;
import com.feicuiedu.eshop.feature.mine.MineFragment;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.event.CartEvent;
import com.feicuiedu.eshop.network.UserManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * <p> App主页面Activity, 用来管理四个Fragment, 依次是:
 * <ol>
 * <li/> 主页面: {@link HomeFragment}
 * <li/> 分类页面: {@link CategoryFragment}
 * <li/> 购物车页面: {@link CartFragment}
 * <li/> 我的页面: {@link MineFragment}
 * </ol>
 */
public class EShopHomeActivity extends BaseActivity implements OnTabSelectListener {

    @BindView(R.id.bottom_bar) BottomBar bottomBar;

    private HomeFragment mHomeFragment;
    private CategoryFragment mCategoryFragment;
    private CartFragment mCartFragment;
    private MineFragment mMineFragment;

    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (savedInstanceState != null) { // “内存重启”时调用
            // 注意必须在bottomBar.setOnTabSelectListener之前调用, 否则会出现Fragment重叠
            retrieveFragments();
        }
        super.onCreate(savedInstanceState);
    }

    @Override protected int getContentViewLayout() {
        return R.layout.activity_eshop_home;
    }

    @Override protected void initView() {
        bottomBar.setOnTabSelectListener(this);
    }

    @Override
    protected void onBusinessResponse(String apiPath, boolean success, ResponseEntity rsp) {

    }

    @Override public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                if (mHomeFragment == null) mHomeFragment = HomeFragment.newInstance();
                switchFragment(mHomeFragment);
                break;
            case R.id.tab_category:
                if (mCategoryFragment == null) mCategoryFragment = CategoryFragment.newInstance();
                switchFragment(mCategoryFragment);
                break;
            case R.id.tab_cart:
                if (mCartFragment == null) mCartFragment = CartFragment.newInstance();
                switchFragment(mCartFragment);
                break;
            case R.id.tab_mine:
                if (mMineFragment == null) mMineFragment = MineFragment.newInstance();
                switchFragment(mMineFragment);
                break;
            default:
                throw new UnsupportedOperationException("Illegal branch!");
        }
    }

    @Override public void onBackPressed() {
        if (mCurrentFragment != mHomeFragment) {
            // 如果不是在HomeFragment, 则按返回键回到HomeFragment
            bottomBar.selectTabWithId(R.id.tab_home);
            return;
        }

        // 将Activity所在的Task移到后台, 而不是finish此Activity
        moveTaskToBack(true);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(CartEvent event) {

        if (UserManager.getInstance().hasCart()) {
            int total = UserManager.getInstance().getCartBill().getGoodsCount();
            bottomBar.getTabAtPosition(2).setBadgeCount(total);
        } else {
            bottomBar.getTabAtPosition(2).removeBadge();
        }

    }


    /**
     * 首页4个Fragment切换, 使用hide和show, 而不是replace.
     *
     * @param target 要显示的目标Fragment.
     */
    private void switchFragment(Fragment target) {
        if (mCurrentFragment == target) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (mCurrentFragment != null) {
            // 隐藏当前正在显示的Fragment
            transaction.hide(mCurrentFragment);
        }

        if (target.isAdded()) {
            // 如果目标Fragment已经添加过, 就显示它
            transaction.show(target);
        } else {
            // 否则直接添加该Fragment
            transaction.add(R.id.layout_container, target, target.getClass().getName());
        }

        transaction.commit();

        mCurrentFragment = target;
    }

    // 找回FragmentManager中存储的Fragment
    private void retrieveFragments() {
        FragmentManager manager = getSupportFragmentManager();
        mHomeFragment = (HomeFragment) manager.findFragmentByTag(HomeFragment.class.getName());
        mCategoryFragment = (CategoryFragment) manager
                .findFragmentByTag(CategoryFragment.class.getName());
        mCartFragment = (CartFragment) manager.findFragmentByTag(CartFragment.class.getName());
        mMineFragment = (MineFragment) manager.findFragmentByTag(MineFragment.class.getName());
    }


}
