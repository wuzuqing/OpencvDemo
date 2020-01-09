package com.example.administrator.opencvdemo.util;

import android.os.Environment;
import android.text.TextUtils;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.event.InputEventManager;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.util.http.HttpManager;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author 吴祖清
 * @version 1.0
 * @createDate 2017/12/24 11:17
 * @des ${TODO}
 * @updateAuthor #author
 * @updateDate 2017/12/24
 * @updateDes ${TODO}
 */

public class PointManagerV2 implements Constant {

    public static File saveFilePath = new File(Environment.getExternalStorageDirectory(), "/cap/1.jpg");
    public static String screenCap = "screencap -p /sdcard/cap/1.jpg";
    public static Map<String, PointModel> coordinateMap = new TreeMap<>();
    public static List<PointModel> coordinateList = new ArrayList<>();

    public static PointModel get(String key) {
        return coordinateMap.get(key);
    }

    public static void initCoordinate() {
        String string = Util.getFileStringAndSp(COORDINATE_KEY);
        if (TextUtils.isEmpty(string)) {
            string = JsonUtils.getJson("x_1080x1920_480.json", BaseApplication.getAppContext());
        }
        if (TextUtils.isEmpty(string)) {
            coordinateList = new ArrayList<>();
        } else {
            coordinateList = (List<PointModel>) JsonUtils.fromJson(string, new TypeToken<List<PointModel>>() {
            }.getType());
        }
        coordinateMap.clear();
        if (coordinateList == null || coordinateList.size() == 0) {
            return;
        }
        for (PointModel pointModel : coordinateList) {
            coordinateMap.put(pointModel.getKey(), pointModel);
        }
    }

    public static void init() {
        screenCap = "screencap -p " + saveFilePath.getPath();
        initCoordinate();
    }


    public static List<PointModel> getChengJiuGet() {
        List<PointModel> result = new ArrayList<>();
        result.add(PointManagerV2.get(CHENG_JIU_GET_1));
        result.add(PointManagerV2.get(CHENG_JIU_GET_2));
        result.add(PointManagerV2.get(CHENG_JIU_GET_3));
        result.add(PointManagerV2.get(CHENG_JIU_GET_4));
        result.add(PointManagerV2.get(CHENG_JIU_GET_5));
        return result;
    }

    public static List<PointModel> getXianShi() {
        List<PointModel> result = new ArrayList<>();
        result.add(PointManagerV2.get(TIME_LIMIT_REWARD1_GET_1));
        result.add(PointManagerV2.get(TIME_LIMIT_REWARD1_GET_2));
        result.add(PointManagerV2.get(TIME_LIMIT_REWARD1_GET_3));
        return result;
    }

    public static List<PointModel> getEmail() {
        List<PointModel> result = new ArrayList<>();
        result.add(PointManagerV2.get(EMAIL_1));
        result.add(PointManagerV2.get(EMAIL_2));
        result.add(PointManagerV2.get(EMAIL_3));
        result.add(PointManagerV2.get(EMAIL_4));
        result.add(PointManagerV2.get(EMAIL_5));
        return result;
    }

    private static List<PointModel> findGuanYanPoints;

    public static List<PointModel> getFindPoints() {
        if (findGuanYanPoints == null || findGuanYanPoints.size() == 0) {
            String str = "";
            if (SPUtils.getBoolean(KEY_GENG_XIN)) {
                str = JsonUtils.getJsonFromMusic("guanYan_1080x1920.json");
            }
            if (TextUtils.isEmpty(str)) {
                str = SPUtils.getString("findPoint");
            }
            findGuanYanPoints = (List<PointModel>) JsonUtils.fromJson(str, new TypeToken<List<PointModel>>() {
            }.getType());
        }
        if (findGuanYanPoints == null) {
            findGuanYanPoints = new ArrayList<>();
        }
        return findGuanYanPoints;
    }


    private static PointModel chuFUPointModel;

    public static void execShellCmdClose() {
        PointModel pointModel = PointManagerV2.get(Constant.SCREEN_CLOSE);
        InputEventManager.getInstance().click(pointModel);
    }

    public static void execShellCmdChuFu() {
        if (chuFUPointModel == null) {
            chuFUPointModel = PointManagerV2.get(CHU_FU);
        }
        InputEventManager.getInstance().click(chuFUPointModel);
    }

    public static void execShellCmdChuFuV2() {
        if (chuFUPointModel == null) {
            chuFUPointModel = PointManagerV2.get(CHU_FU);
        }
        InputEventManager.getInstance().click(chuFUPointModel);
    }

    public static void saveCoordinate() {
        String jsonList = JsonUtils.toJson(PointManagerV2.coordinateList);
        Util.setFileStrAndSp(COORDINATE_KEY,jsonList);
        HttpManager.updatePoint("total",jsonList);
    }
}
