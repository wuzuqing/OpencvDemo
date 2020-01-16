package com.example.administrator.opencvdemo.util;

import android.os.AsyncTask;
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

    private static int width = 1028;
    private static int oneWidth = width / 6;

    private static int firstY = 386;
    private static int secondY = 558;
    private static final float originalWidth = 1080f;
    private static final float originalHeight = 1920f;

    public static File saveFilePath = new File(Environment.getExternalStorageDirectory(), "/cap/1.jpg");
    public static String screenCap = "screencap -p /sdcard/cap/1.jpg";
    public static Map<String, PointModel> coordinateMap = new TreeMap<>();
    public static List<PointModel> coordinateList = new ArrayList<>();

    public static PointModel get(String key) {
        return coordinateMap.get(key);
    }

    public static void initCoordinate() {
        String string = HttpManager.getBasePoints().getData().getTotalPoints();
        //        if (TextUtils.isEmpty(string)) {
        //            string = Util.getFileStringAndSp(COORDINATE_KEY);
        //        }
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
        int selfWidth = BaseApplication.getScreenWidth();
        int selfHeight = BaseApplication.getScreenHeight();

        int tempHeight = (int) (selfWidth * originalHeight / originalWidth);
        int tempTop = (selfHeight - tempHeight) / 2;
        float radioX = selfWidth / originalWidth;
        float radioY = tempHeight / originalHeight;
        for (PointModel pointModel : coordinateList) {
            //没有重置或者尺寸与原始不一致则需要计算新的x y坐标
            if (!pointModel.isReset() && (originalWidth != selfWidth || originalHeight != selfHeight)
            ) {
                //重新计算坐标
                compute(pointModel, selfHeight, tempTop, radioX, radioY);
            }
            coordinateMap.put(pointModel.getKey(), pointModel);
        }
    }

    private static void compute(PointModel pointModel,
        int selfHeight, int tempTop, float radioX, float radioY) {
        if (selfHeight < originalHeight) {
            return;
        }
        int oldX = pointModel.getBaseX();
        int oldY = pointModel.getBaseY();
        int realX = (int) (oldX * radioX);
        int realY = (int) (oldY * radioY) + tempTop;
        pointModel.setComputeX(realX);
        pointModel.setComputeY(realY);
        LogUtils.logd("compute " + pointModel.getName() + " : " + oldX + "," + oldY + " real:" + realX + "," + realY);
    }

    public static void init() {
        screenCap = "screencap -p " + saveFilePath.getPath();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    initCoordinate();
                    Util.init();
                    AccountManager.init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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


    public static void execShellCmdChuFuV2() {
        if (chuFUPointModel == null) {
            chuFUPointModel = PointManagerV2.get(CHU_FU);
        }
        InputEventManager.getInstance().click(chuFUPointModel);
    }

    public static void saveCoordinate() {
        String jsonList = JsonUtils.toJson(PointManagerV2.coordinateList);
        Util.setFileStrAndSp(COORDINATE_KEY, jsonList);
        HttpManager.updatePoint("total", jsonList);
    }

    // 府内活动的位置
    private static PointModel defaultPoint;

    /**
     * 获取府内的活动按钮
     */
    public static PointModel getPointModel(int row, int col) {
        if (defaultPoint == null) {
            defaultPoint = new PointModel("XB", "榜单");
        }
        int y = row == 1 ? firstY : secondY;
        int x = col * oneWidth + oneWidth / 2 + 30;
        defaultPoint.setX(x);
        defaultPoint.setY(y);
        return defaultPoint;
    }
}
