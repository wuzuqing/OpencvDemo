package com.example.administrator.opencvdemo.config;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.util.AccountManager;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.util.http.HttpManager;
import com.example.administrator.opencvdemo.youtu.StaticVal;

public class BaseConfig {
    public static void init(){
        PointManagerV2.init();
        Util.init();
        AccountManager.init();
        HttpManager.init(BaseApplication.getAppContext());
        StaticVal.init();
        LogUtils.logd("BaseConfig:init");
    }
}
