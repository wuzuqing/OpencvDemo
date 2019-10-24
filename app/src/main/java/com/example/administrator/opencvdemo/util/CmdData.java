package com.example.administrator.opencvdemo.util;

import android.os.Environment;
import android.text.TextUtils;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.model.PointModel;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

public class CmdData implements Constant {


    public static int textX = 0, textY = 0;
    public static String screenClose = "";
    public static File saveFilePath = new File(Environment.getExternalStorageDirectory(), "/cap/1.jpg");
    public static String screenCap = "screencap -p /sdcard/cap/1.jpg";
    private static int startPointX = 400;  //300 - 400
    private static int startPointY = 400;  //300 - 400
    public static String swipeToLeft = "input swipe 100 400 3000 400";
    public static String swipeToRight = "input swipe 100 400 -3000 400";
    public static String inputTextUserInfoName = "input text ";
    public static Map<String, PointModel> coordinateMap = new TreeMap<>();
    public static List<PointModel> coordinateList = new ArrayList<>();

    public static PointModel get(String key) {
        return coordinateMap.get(key);
    }

    public static void initCoordinate() {

        String string = "";
        if (TextUtils.isEmpty(string)) {
            if (BaseApplication.getScreenWidth() == 1080) {
                if (BaseApplication.densityDpi==420){
                    if (TextUtils.isEmpty(string)) {
                        LogUtils.logd("getJsonFromMusic:" + string);
                        string = JsonUtils.getJsonFromMusic("x_1080x1920.json");
                    }
                    if (TextUtils.isEmpty(string)){
                        string = JsonUtils.getJson("x_1080x1920.json", BaseApplication.getAppContext());
                    }
                }else{
                    string = JsonUtils.getJson("x_1080x1920_480.json", BaseApplication.getAppContext());
                    LogUtils.logd("x_1080x1920_480");
                }
                string = JsonUtils.getJson("x_1080x1920_480.json", BaseApplication.getAppContext());
                if (TextUtils.isEmpty(string)) {
                    string = SPUtils.getString(COORDINATE_KEY);
                }
            } else if (BaseApplication.getScreenWidth() == 720) {
                string = JsonUtils.getJsonFromMusic("x_760x1280.json");
                if (TextUtils.isEmpty(string)) {
                    string = JsonUtils.getJson("x_760x1280.json", BaseApplication.getAppContext());
                }
                if (TextUtils.isEmpty(string)) {
                    string = SPUtils.getString(COORDINATE_KEY);
                }
            }
        }

        if (TextUtils.isEmpty(string)) {
            coordinateList = new ArrayList<>();
        } else {
            coordinateList = (List<PointModel>) JsonUtils.fromJson(string, new TypeToken<List<PointModel>>() {
            }.getType());
        }
        coordinateMap.clear();
        if (coordinateList == null || coordinateList.size() == 0) return;
        for (PointModel pointModel : coordinateList) {
            coordinateMap.put(pointModel.getKey(), pointModel);
        }
    }

    public static void init() {
        screenCap = "screencap -p "+saveFilePath.getPath();
        startPointX = (int) (BaseApplication.getScreenWidth() * 0.45f);
        initCoordinate();
        PointModel pointModel = get(SCREEN_CLOSE);
        if (pointModel!=null){
            screenClose = clickInt(pointModel.getX(), pointModel.getY());
        }
        AutoTool.init();
    }

    public static String clickFloat(float x, float y) {
        return String.format(Locale.getDefault(), "input tap %d %d", getXRatio(x), getXRatio(y));
    }

    public static String clickInt(int x, int y) {
        return String.format(Locale.getDefault(), "input tap %d %d", x, y);
    }

    public static String swipeLeft(int tx) {
        return swipe(startPointX, tx);
    }

    public static String swipeRight(int tx) {
        return swipe(tx, startPointX);
    }

    public static String swipe(int fx, int fy, int tx, int ty) {
        return String.format(Locale.getDefault(), "input swipe %d %d %d %d", fx, fy, tx, ty);
    }

    public static String swipe(int fx, int tx) {
        return swipe(fx, startPointY, tx, startPointY);
    }

    public static String swipeY(int fy, int ty) {
        return swipe(startPointX, fy, startPointX, ty);
    }

    public static int getXRatio(float ratio) {
        return (int) (BaseApplication.getScreenWidth() * ratio);
    }
    public static int getYRatio(float ratio) {
        return (int) (BaseApplication.getScreenHeight() * ratio);
    }



    public static List<PointModel> getChengJiuGet() {
        List<PointModel> result = new ArrayList<>();
        result.add(CmdData.get(CHENG_JIU_GET_1));
        result.add(CmdData.get(CHENG_JIU_GET_2));
        result.add(CmdData.get(CHENG_JIU_GET_3));
        result.add(CmdData.get(CHENG_JIU_GET_4));
        result.add(CmdData.get(CHENG_JIU_GET_5));
        return result;
    }

    public static List<PointModel> getXianShi() {
        List<PointModel> result = new ArrayList<>();
        result.add(CmdData.get(TIME_LIMIT_REWARD1_GET_1));
        result.add(CmdData.get(TIME_LIMIT_REWARD1_GET_2));
        result.add(CmdData.get(TIME_LIMIT_REWARD1_GET_3));
        return result;
    }

    public static List<PointModel> getEmail() {
        List<PointModel> result = new ArrayList<>();
        result.add(CmdData.get(EMAIL_1));
        result.add(CmdData.get(EMAIL_2));
        result.add(CmdData.get(EMAIL_3));
        result.add(CmdData.get(EMAIL_4));
        result.add(CmdData.get(EMAIL_5));
        return result;
    }

    private static List<PointModel> findGuanYanPoints;

    public static List<PointModel> getFindPoints() {
        if (findGuanYanPoints == null || findGuanYanPoints.size() == 0) {
            String str = "";
            if (SPUtils.getBoolean(KEY_GENG_XIN)) str = JsonUtils.getJsonFromMusic("guanYan_1080x1920.json");
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

}
