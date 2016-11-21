package com.feicuiedu.eshop.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * <p> Api接口, 每一个实现类代表一个服务器接口.
 * @param <T> 响应实体的类型
 */
public interface ApiInterface<T extends ResponseEntity> {

    // 接口路径
    @NonNull String getPath();

    // 请求参数
    @Nullable RequestParam getRequestParam();

    // 响应类型
    @NonNull Class<T> getResponseType();
}
