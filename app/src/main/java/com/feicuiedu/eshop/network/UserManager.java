package com.feicuiedu.eshop.network;


import android.support.annotation.NonNull;

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

    public void signIn(@NonNull Session session,
                       @NonNull User user) {
        mSession = session;
        mUser = user;

        EventBus.getDefault().post(new UpdateUserEvent());
    }


    public boolean isSignIn() {
        return mSession != null;
    }

    public static class UpdateUserEvent {
    }
}
