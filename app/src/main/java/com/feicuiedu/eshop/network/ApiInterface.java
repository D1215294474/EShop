package com.feicuiedu.eshop.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <p> Api接口的抽象, 每一个子类代表一个服务器接口.
 * @param <T> 响应实体的类型
 */
public abstract class ApiInterface<T extends ResponseEntity> {

    // 接口路径
    @NonNull public abstract String getPath();

    // 请求参数
    @Nullable public abstract RequestParam getRequestParam();
}
