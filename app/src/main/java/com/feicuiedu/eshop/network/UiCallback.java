package com.feicuiedu.eshop.network;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.utils.LogUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @param <T> 响应体的实体类型.
 */
public abstract class UiCallback<T extends ResponseEntity> implements Callback {


    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private Context mContext;

    public UiCallback(Context context) {
        mContext = context.getApplicationContext();
    }

    /**
     * <p>当网络请求由于以下原因失败时被调用: 请求被取消, 网络连接问题或者请求超时.
     * <p>由于网络可能在数据交换的中途发生故障, 有可能在失败前, 服务器处理了请求.
     */
    @Override public final void onFailure(Call call, final IOException e) {
        HANDLER.post(new Runnable() {
            @Override public void run() {
                onFailureInUi(e);
            }
        });
    }


    /**
     * 当HTTP响应从服务器成功返回时被调用. 可以通过{@link Response#body}读取响应体.
     * 在响应体变为{@linkplain ResponseBody closed}状态之前, 响应会一直处于可用状态.
     * <p/>
     * 注意: 此回调在后台线程执行, 响应的接收者可以将数据发送到另一个线程来处理.
     * <p/>
     * 注意: 传输层的成功(接受到了HTTP响应码, 响应头和响应体)不代表应用层的成功:
     * {@code response}依然可能是一个失败的响应码, 例如404或500.
     */
    @Override public final void onResponse(Call call, Response response) throws IOException {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        //noinspection unchecked
        Class<T> entityClass = (Class<T>) type.getActualTypeArguments()[0];
        final T responseEntity = EShopClient.getInstance().getResponseEntity(response, entityClass);

        HANDLER.post(new Runnable() {
            @Override public void run() {
                onResponseInUi(responseEntity);
            }
        });
    }

    public final void onFailureInUi(IOException e) {
        LogUtils.error("onFailureInUi", e);
        Toast.makeText(mContext, R.string.error_network, Toast.LENGTH_SHORT).show();
        onBusinessResponse(false, null);
    }

    public final void onResponseInUi(T responseEntity) {

        if (responseEntity == null || responseEntity.getStatus() == null) {
            throw new RuntimeException("Fatal Api Error!");
        }

        if (responseEntity.getStatus().isSucceed()) {
            onBusinessResponse(true, responseEntity);
        } else {
            Toast.makeText(mContext, responseEntity.getStatus().getErrorDesc(), Toast.LENGTH_SHORT)
                    .show();
            onBusinessResponse(false, null);
        }
    }

    public abstract void onBusinessResponse(boolean success, T responseEntity);
}
