package com.feicuiedu.eshop.network;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>网络接口的操作类, 网络请求使用{@link OkHttpClient}实现.
 */
public class EShopClient {

    public static final String BASE_URL = "http://106.14.32.204/eshop/emobile/?url=";

    private static EShopClient sInstance;

    public static EShopClient getInstance() {
        if (sInstance == null) {
            sInstance = new EShopClient();
        }
        return sInstance;
    }

    private final OkHttpClient mOkHttpClient;
    private final Gson mGson;

    private EShopClient() {
        mGson = new Gson();
        mOkHttpClient = new OkHttpClient();
    }

    /**
     * 同步执行Api请求.
     * @param apiInterface 服务器Api接口.
     * @param <T> 响应体的实体类型.
     * @return 响应数据实体.
     * @throws IOException 请求被取消, 连接超时, 失败的响应码等等.
     */
    public <T extends ResponseEntity> T execute(ApiInterface<T> apiInterface)
            throws IOException {

        Response response = newApiCall(apiInterface).execute();
        ParameterizedType type = (ParameterizedType)
                (apiInterface.getClass().getGenericSuperclass());
        //noinspection unchecked
        Class<T> entityClass = (Class<T>) type.getActualTypeArguments()[0];
        return getResponseEntity(response, entityClass);
    }

    /**
     * 异步执行Api请求.
     * @param apiInterface 服务器Api接口.
     * @param uiCallback 回调
     * @param <T> 响应体的实体类型.
     * @return {@link Call}对象.
     */
    public <T extends ResponseEntity> Call enqueue(ApiInterface<T> apiInterface,
                                                   UiCallback<T> uiCallback) {
        Call call = newApiCall(apiInterface);
        call.enqueue(uiCallback);
        return call;
    }

    <T extends ResponseEntity> T getResponseEntity(Response response, Class<T> clazz)
            throws IOException {
        if (!response.isSuccessful()) {
            throw new IOException("Response code is " + response.code());
        }
        return mGson.fromJson(response.body().charStream(), clazz);
    }

    private Call newApiCall(ApiInterface apiInterface) {
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + apiInterface.getPath());

        if (apiInterface.getRequestParam() != null) {
            String param = mGson.toJson(apiInterface.getRequestParam());
            FormBody formBody = new FormBody.Builder()
                    .add("json", param)
                    .build();
            builder.post(formBody);
        }

        Request request = builder.build();
        return mOkHttpClient.newCall(request);
    }

}
