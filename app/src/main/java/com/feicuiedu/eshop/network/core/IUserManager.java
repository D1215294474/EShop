package com.feicuiedu.eshop.network.core;


import android.support.annotation.NonNull;

import com.feicuiedu.eshop.network.entity.Address;
import com.feicuiedu.eshop.network.entity.CartBill;
import com.feicuiedu.eshop.network.entity.CartGoods;
import com.feicuiedu.eshop.network.entity.Session;
import com.feicuiedu.eshop.network.entity.User;

import java.util.List;

public interface IUserManager {

    // --------账号管理-------- //
    void setUser(@NonNull User user, @NonNull Session session);

    void clear();

    boolean hasUser();

    Session getSession();

    User getUser();


    // --------购物车管理-------- //
    void retrieveCartList();

    boolean hasCart();

    List<CartGoods> getCartGoodsList();

    CartBill getCartBill();

    // --------收件地址管理-------- //

    void retrieveAddressList();

    boolean hasAddress();

    List<Address> getAddressList();

    Address getDefaultAddress();
}
