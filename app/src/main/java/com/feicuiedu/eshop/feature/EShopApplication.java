package com.feicuiedu.eshop.feature;


import android.app.Application;
import android.preference.PreferenceManager;

import com.feicuiedu.eshop.R;
import com.feicuiedu.eshop.base.wrapper.ToastWrapper;
import com.squareup.leakcanary.LeakCanary;

public class EShopApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // 此进程是LeakCanary用于内存堆分析的.
            // 不应该在此进程中做任何app初始化工作.
            return;
        }

        LeakCanary.install(this);

        ToastWrapper.init(this);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }
}
