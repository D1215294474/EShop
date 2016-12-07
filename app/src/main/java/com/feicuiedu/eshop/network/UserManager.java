package com.feicuiedu.eshop.network;


import android.content.Context;
import android.support.annotation.NonNull;

import com.feicuiedu.eshop.network.api.ApiCartList;
import com.feicuiedu.eshop.network.entity.CartBill;
import com.feicuiedu.eshop.network.entity.CartGoods;
import com.feicuiedu.eshop.network.entity.Session;
import com.feicuiedu.eshop.network.entity.User;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.Call;

public class UserManager {

    private static UserManager sInstance = new UserManager();

    public static UserManager getInstance() {
        return sInstance;
    }

    private Session mSession;

    private User mUser;

    private List<CartGoods> mCartGoodsList;

    private CartBill mCartBill;

    private Call mCartCall;

    public void signIn(@NonNull Session session,
                       @NonNull User user) {
        mSession = session;
        mUser = user;

        EventBus.getDefault().post(new UpdateUserEvent());
    }


    public boolean isSignIn() {
        return mSession != null;
    }

    public void updateCart(Context context) {

        if (mCartCall != null) return;

        ApiCartList apiCartList = new ApiCartList();

        Context app = context.getApplicationContext();
        UiCallback<ApiCartList.Rsp> cb = new UiCallback<ApiCartList.Rsp>(app) {
            @Override
            public void onBusinessResponse(boolean success, ApiCartList.Rsp responseEntity) {
                mCartCall = null;

                if (success) {
                    mCartGoodsList = responseEntity.getData().getGoodsList();
                    mCartBill = responseEntity.getData().getCartBill();
                }

                EventBus.getDefault().post(new UpdateCartEvent(success));
            }
        };

        mCartCall = EShopClient.getInstance().enqueue(apiCartList, cb, null);
    }

    public Session getSession() {
        return mSession;
    }

    public User getUser() {
        return mUser;
    }

    public List<CartGoods> getCartGoodsList() {
        return mCartGoodsList;
    }

    public CartBill getCartBill() {
        return mCartBill;
    }

    public static class UpdateUserEvent {
    }

    public static class UpdateCartEvent {

        public final boolean success;

        public UpdateCartEvent(boolean success) {
            this.success = success;
        }
    }
}
