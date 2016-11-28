package com.feicuiedu.eshop.network;


import com.feicuiedu.eshop.network.entity.Session;

public class SessionManager {

    private static SessionManager sInstance = new SessionManager();

    public static SessionManager getInstance() {
        return sInstance;
    }

    public Session get() {
        return null;
    }
}
