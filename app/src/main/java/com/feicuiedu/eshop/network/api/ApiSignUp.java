package com.feicuiedu.eshop.network.api;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.feicuiedu.eshop.network.ApiInterface;
import com.feicuiedu.eshop.network.RequestParam;
import com.feicuiedu.eshop.network.ResponseEntity;
import com.feicuiedu.eshop.network.entity.Session;
import com.feicuiedu.eshop.network.entity.User;
import com.google.gson.annotations.SerializedName;

/**
 * 服务器接口: 用户注册.
 */
public class ApiSignUp implements ApiInterface {

    private ApiSignUp.Req mReq;

    public ApiSignUp(@NonNull String name,
                     @NonNull String email,
                     @NonNull String password) {
        mReq = new Req();
        mReq.mName = name;
        mReq.mPassword = password;
        mReq.mEmail = email;
    }

    @NonNull @Override public String getPath() {
        return "/user/signup";
    }

    @Nullable @Override public RequestParam getRequestParam() {
        return mReq;
    }

    @NonNull @Override public Class<? extends ResponseEntity> getResponseType() {
        return Rsp.class;
    }

    public static class Req extends RequestParam {
        @SerializedName("name") private String mName;
        @SerializedName("email") private String mEmail;
        @SerializedName("password") private String mPassword;
    }

    public static class Rsp extends ResponseEntity {

        @SerializedName("data") Data mData;

        public Data getData() {
            return mData;
        }

        public static class Data {
            @SerializedName("session") private Session mSession;
            @SerializedName("user") private User mUser;

            public Session getSession() {
                return mSession;
            }

            public User getUser() {
                return mUser;
            }
        }
    }
}
