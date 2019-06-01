package com.example.administrator.opencvdemo;

import android.app.Application;

import com.example.module_orc.OrcHelper;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OrcHelper.getInstance().init(this);
    }
}
