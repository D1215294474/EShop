package com.feicuiedu.eshop.network;


import android.support.annotation.NonNull;

import com.feicuiedu.eshop.network.api.ApiAddressList;
import com.feicuiedu.eshop.network.api.ApiCartList;
import com.feicuiedu.eshop.network.core.IUserManager;
import com.feicuiedu.eshop.network.core.ResponseEntity;
import com.feicuiedu.eshop.network.core.UiCallback;
import com.feicuiedu.eshop.network.entity.Address;
import com.feicuiedu.eshop.network.entity.CartBill;
import com.feicuiedu.eshop.network.entity.CartGoods;
import com.feicuiedu.eshop.network.entity.Session;
import com.feicuiedu.eshop.network.entity.User;
import com.feicuiedu.eshop.network.event.AddressEvent;
import com.feicuiedu.eshop.network.event.CartEvent;
import com.feicuiedu.eshop.network.event.UserEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class UserManager implements IUserManager {

    private static IUserManager sInstance = new UserManager();

    public static IUserManager getInstance() {
        return sInstance;
    }

    private EShopClient mClient = EShopClient.getInstance();

    private EventBus mBus = EventBus.getDefault();

    private Session mSession;

    private User mUser;

    private List<CartGoods> mCartGoodsList;

    private CartBill mCartBill;

    private List<Address> mAddressList;

    @Override public void setUser(@NonNull User user, @NonNull Session session) {
        mUser = user;
        mSession = session;

        mBus.postSticky(new UserEvent());
        retrieveCartList();
        retrieveAddressList();
    }

    @Override public void retrieveCartList() {
        ApiCartList apiCartList = new ApiCartList();
        UiCallback cb = new UiCallback() {
            @Override
            public void onBusinessResponse(boolean success, ResponseEntity rsp) {

                if (success) {
                    ApiCartList.Rsp listRsp = (ApiCartList.Rsp) rsp;
                    mCartGoodsList = listRsp.getData().getGoodsList();
                    mCartBill = listRsp.getData().getCartBill();
                }

                mBus.postSticky(new CartEvent());
            }
        };

        mClient.enqueue(apiCartList, cb, null);
    }

    @Override public void retrieveAddressList() {
        ApiAddressList apiAddressList = new ApiAddressList();
        UiCallback uiCallback = new UiCallback() {
            @Override
            public void onBusinessResponse(boolean success, ResponseEntity responseEntity) {
                if (success) {
                    ApiAddressList.Rsp listRsp = (ApiAddressList.Rsp) responseEntity;
                    mAddressList = listRsp.getData();
                }
                mBus.postSticky(new AddressEvent());
            }
        };
        mClient.enqueue(apiAddressList, uiCallback, getClass().getSimpleName());
    }

    @Override public Address getDefaultAddress() {
        if (hasAddress()) {
            for (Address address : mAddressList) {
                if (address.isDefault()) return address;
            }
        }
        return null;
    }

    @Override public void clear() {
        mUser = null;
        mSession = null;
        mCartBill = null;
        mCartGoodsList = null;

        mClient.cancelByTag(getClass().getSimpleName());

        mBus.postSticky(new UserEvent());
        mBus.postSticky(new CartEvent());
        mBus.postSticky(new AddressEvent());
    }

    @Override public boolean hasUser() {
        return mSession != null && mUser != null;
    }

    @Override public boolean hasCart() {
        return mCartGoodsList != null && !mCartGoodsList.isEmpty();
    }

    @Override public boolean hasAddress() {
        return mAddressList != null && !mAddressList.isEmpty();
    }

    @Override public Session getSession() {
        return mSession;
    }

    @Override public User getUser() {
        return mUser;
    }

    @Override public List<CartGoods> getCartGoodsList() {
        return mCartGoodsList;
    }

    @Override public CartBill getCartBill() {
        return mCartBill;
    }

    @Override public List<Address> getAddressList() {
        return mAddressList;
    }

}
