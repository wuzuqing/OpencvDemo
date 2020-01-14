package com.example.administrator.opencvdemo.util.http;

import android.content.Context;
import android.text.TextUtils;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.model.PagePointsModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.ServicePointModel;
import com.example.administrator.opencvdemo.model.TaskRecordModel;
import com.example.administrator.opencvdemo.util.JsonUtils;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;

public class HttpManager {
    private static String flag = "";
    private static String area = "142";

    public static void init(Context context) {
        OkHttp3Utils.getInstance().getHandler();
        flag = String.format(Locale.CHINA, "%d_%d", BaseApplication.getScreenWidth(), BaseApplication.getScreenHeight());
    }

    public static void updateTask(String tasks) {
        Map<String, String> map = new HashMap<>();
        map.put("account", TaskState.get().getUserInfo().getName());
        map.put("area", area);
        map.put("task", tasks);
        OkHttp3Utils.doPost("/game/updateTaskRecord", map, new GsonObjectCallback<HttpBaseModel>() {
            @Override
            public void onUi(HttpBaseModel result) {

            }

            @Override
            public void onFailed(Call call, IOException e) {

            }
        });
    }

    public static void updatePoint(String type, String points) {
        Map<String, String> map = new HashMap<>();
        map.put("flag", flag);
        map.put("type", type);
        map.put("points", points);
        OkHttp3Utils.doPost("/game/updatePoints", map, new GsonObjectCallback<HttpBaseModel>() {
            @Override
            public void onUi(HttpBaseModel result) {

            }

            @Override
            public void onFailed(Call call, IOException e) {

            }
        });
    }

    public static void updatePageData(String page, String data) {
        Map<String, String> map = new HashMap<>();
        map.put("flag", flag);
        map.put("page", page);
        map.put("data", data);
        OkHttp3Utils.doPost("/game/updatePageData", map, new GsonObjectCallback<HttpBaseModel>() {
            @Override
            public void onUi(HttpBaseModel result) {

            }

            @Override
            public void onFailed(Call call, IOException e) {

            }
        });
    }

    public static ServicePointModel getBasePoints() {
        return OkHttp3Utils.doGetSync("/game/getPoints?flag=base" , ServicePointModel.class);
    }

    public static ServicePointModel getPoints() {
        return OkHttp3Utils.doGetSync("/game/getPoints?flag=" + flag, ServicePointModel.class);
    }

    public static TaskRecordModel getTaskRecord() {
        return OkHttp3Utils.doGetSync("/game/getTaskRecord?account=" + area, TaskRecordModel.class);
    }

    public static List<Result.ItemsBean> getItemCoord(String page){
        PagePointsModel model = OkHttp3Utils.doGetSync("/game/getItemCoord?flag=" + flag +"&page="+page, PagePointsModel.class);
        if (TextUtils.isEmpty(model.getData())){
            return null;
        }
        return (List<Result.ItemsBean>) JsonUtils.fromJson(model.getData(), new TypeToken<List<Result.ItemsBean>>() {
        }.getType());
    }

    public static void getTaskRecord(Callback callback) {
         OkHttp3Utils.doGet("/game/getTaskRecords?area=" + area, callback);
    }
}
