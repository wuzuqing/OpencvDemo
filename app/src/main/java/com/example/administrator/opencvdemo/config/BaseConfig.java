package com.example.administrator.opencvdemo.config;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.http.HttpManager;
import com.example.administrator.opencvdemo.youtu.StaticVal;

public class BaseConfig {
    public static void init(){
        HttpManager.init(BaseApplication.getAppContext());
        PointManagerV2.init();
        StaticVal.init();
        LogUtils.logd("BaseConfig:init");
    }
}
