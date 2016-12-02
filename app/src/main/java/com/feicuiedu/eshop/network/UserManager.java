package com.feicuiedu.eshop.network;


import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.entity.Session;
import com.feicuiedu.eshop.network.entity.User;

import org.greenrobot.eventbus.EventBus;

public class UserManager {

    private static UserManager sInstance = new UserManager();

    public static UserManager getInstance() {
        return sInstance;
    }

    private Session mSession;

    private User mUser;

    public Session getSession() {
        return mSession;
    }

    public User getUser() {
        return mUser;
    }

    public void update(@Nullable Session session,
                       @Nullable User user) {
        mSession = session;
        mUser = user;

        EventBus.getDefault().post(new UpdateUserEvent());
    }

    public static class UpdateUserEvent {
    }
}
