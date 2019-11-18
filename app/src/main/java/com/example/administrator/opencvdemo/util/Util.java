package com.example.administrator.opencvdemo.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.floatservice.MainService;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.model.UserInfo;
import com.example.administrator.opencvdemo.notroot.WPZMGService2;
import com.example.administrator.opencvdemo.v2.TaskElement;
import com.example.administrator.opencvdemo.v2.TaskState;
import com.example.administrator.opencvdemo.v2.task.ClzwTaskElement;
import com.example.administrator.opencvdemo.v2.task.FengluTaskElement;
import com.example.administrator.opencvdemo.v2.task.JoinGameTaskElement;
import com.example.administrator.opencvdemo.v2.task.JyzcTaskElement;
import com.example.administrator.opencvdemo.v2.task.MobaiTaskElement;
import com.example.administrator.opencvdemo.v2.task.ShuyuanTaskElement;
import com.example.administrator.opencvdemo.v2.task.StartAndLoginTaskElement;
import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/22 13:58
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/22$
 * @updateDes ${TODO}
 */

public class Util implements Constant {
    private static List<TaskModel> sTaskModelList;
    private static String pathName = CmdData.saveFilePath.getAbsolutePath();
    public static boolean isWPZMGServiceRunning;
    private static String action;

    public static void setAction(String action) {
        Util.action = action;
        LogUtils.logd("action:" + action);
    }

    public static boolean isScreenOff() {
        return !Intent.ACTION_SCREEN_ON.equals(action);
    }

    public static String getAction() {
        return action;
    }

    public static List<TaskModel> getTask(int spaceTime) {
        List<TaskModel> taskModels = new ArrayList<>();
        if (SPUtils.getBoolean(KEY_XIAN_SHI)) {
            taskModels.add(getSimpleModel("限时奖励", TIME_LIMIT_REWARD, KEY_SPACE_TIME_THREE, 86400)); //政绩
        }
        if (SPUtils.getBoolean(KEY_KUA_FU_JL)) {
            taskModels.add(getSimpleModel("跨服奖励", TASK_KUA_FU_JIANG_LI, KEY_SPACE_TIME_THREE, 86400)); //政绩
        }
        if (SPUtils.getBoolean(KEY_WORK_ZW,true)) {
            taskModels.add(getSimpleModel("政务", TASK_ZHENG_WU, KEY_SPACE_TIME_TWO, 20000)); //政绩
        }
        if (SPUtils.getBoolean(KEY_CHOU_CAI,true)) {
            taskModels.add(getTaskModel(spaceTime));    //收菜
        }
        if (SPUtils.getBoolean(KEY_SHU_YUAN)) {
            taskModels.add(getSimpleModel("书院", TASK_SHU_YUAN, KEY_SPACE_TIME_SHU_YUAN, 180)); //政绩
        }
        if (SPUtils.getBoolean(KEY_ZHAO_HUAN)) {
            taskModels.add(getSimpleModel("召唤", SUI_JI_ZHAO_HUAN, KEY_SPACE_TIME_SHU_YUAN, 180)); //政绩
        }
        if (SPUtils.getBoolean(KEY_EMAIL)) {
            taskModels.add(getSimpleModel("邮箱", EMAIL, KEY_SPACE_TIME_THREE, 86400)); //邮箱
        }
        if (SPUtils.getBoolean(KEY_WORK_FL,true)) {
            taskModels.add(getSimpleModel("俸禄", TASK_FENG_LU, KEY_SPACE_TIME_FOUR, 86400)); //政绩
        }


        if (SPUtils.getBoolean(KEY_WORK_MB)) {
            taskModels.add(getSimpleModel("膜拜", TASK_MO_BAI, KEY_SPACE_TIME_THREE, 86400)); //政绩
        }
        if (SPUtils.getBoolean(KEY_LAO_FANG)) {
            taskModels.add(getSimpleModel("牢房", TASK_LAO_FANG, KEY_SPACE_TIME_THREE, 86400)); //政绩
        }
        if (SPUtils.getBoolean(KEY_YA_MEN)) {
            taskModels.add(getSimpleModel("衙门", TASK_YA_MEN, KEY_SPACE_TIME_THREE, 86400)); //政绩
        }

        if (SPUtils.getBoolean(KEY_WORK_TASK)) {
            taskModels.add(getSimpleModel("任务奖励", TASK_REN_WU_JIANG_LU, KEY_SPACE_TIME_THREE, 86400)); //政绩
        }

        if (SPUtils.getBoolean(KEY_CHENG_JIU)) {
            taskModels.add(getSimpleModel("成就", TASK_CHEGN_JIU, KEY_SPACE_TIME_THREE, 86400)); //政绩
        }

//        if (SPUtils.getBoolean(KEY_YAN_HUI)) {
//            taskModels.add(getSimpleModel("宴会", TASK_YAN_HUI, KEY_SPACE_TIME_SHU_YUAN, 180)); //政绩
//        }
        if (SPUtils.getBoolean(KEY_GUAN_KA)) {
            taskModels.add(getSimpleModel("关卡", TASK_GUAN_KA, KEY_SPACE_TIME_TWO, 20000)); //政绩
        }
        if (SPUtils.getBoolean(KEY_REGISTER)) {
            taskModels.add(getSimpleModel("注册账号", TASK_REGISTER, KEY_SPACE_TIME_THREE, 86400)); //政绩
        }

        if (SPUtils.getBoolean(KEY_LIAN_MENG)) {
            taskModels.add(getSimpleModel("联盟建设", LIAN_MENG_GAO_JIAN, KEY_SPACE_TIME_THREE, 86400)); //邮箱
        }
        if (SPUtils.getBoolean(KEY_LIAN_MENG_FU_BEN)) {
            taskModels.add(getSimpleModel("联盟副本", TASK_LIAN_MENG_FU_BEN, KEY_SPACE_TIME_THREE, 86400)); //邮箱
        }
        if (SPUtils.getBoolean(KEY_GUAN_YAN)) {
            taskModels.add(getSimpleModel("官宴", GUAN_YAN, KEY_SPACE_TIME_THREE, 86400)); //邮箱
        }

        return taskModels;
    }

    public static TaskModel getSimpleModel(String name, int type, String spacekey, int spaceTime) {
        TaskModel taskModel = new TaskModel();
        taskModel.setName(name);
        taskModel.setType(type);
        taskModel.setSpaceTime(SPUtils.getInt(spacekey, spaceTime));
        return taskModel;
    }

    private static List<PointModel> renWuTopModel;
    private static List<PointModel> renWuRightModel;

    public static List<PointModel> getRenWuTopModel() {
        if (renWuTopModel == null) {
            renWuTopModel = new ArrayList<>();
            renWuTopModel.add(CmdData.get(TASK_TOP_10));
            renWuTopModel.add(CmdData.get(TASK_TOP_30));
            renWuTopModel.add(CmdData.get(TASK_TOP_60));
            renWuTopModel.add(CmdData.get(TASK_TOP_100));
            renWuTopModel.add(CmdData.get(TASK_TOP_140));
        }
        return renWuTopModel;
    }

    public static List<PointModel> getRenWuRightModel() {
        if (renWuRightModel == null) {
            renWuRightModel = new ArrayList<>();
            renWuRightModel.add(CmdData.get(Constant.TASK_RIGHT_1));
            renWuRightModel.add(CmdData.get(Constant.TASK_RIGHT_2));
            renWuRightModel.add(CmdData.get(Constant.TASK_RIGHT_3));
            renWuRightModel.add(CmdData.get(Constant.TASK_RIGHT_4));
            renWuRightModel.add(CmdData.get(Constant.TASK_RIGHT_5));
        }
        return renWuRightModel;
    }


    @NonNull
    private static List<PointModel> getPointModels(String... keys) {
        List<PointModel> data = new ArrayList<>();
        for (String key : keys) {
            PointModel pointModel = CmdData.get(key);
            if (pointModel == null) {
                data.add(new PointModel("11", "22"));
            } else {
                data.add(pointModel);
            }
        }
        return data;
    }

    /**
     * 收菜
     *
     * @param spaceTime
     * @return
     */
    @NonNull
    private static TaskModel getTaskModel(int spaceTime) {
        TaskModel taskModel = new TaskModel();
        taskModel.setSpaceTime(spaceTime);
        taskModel.setName("收取资源");
        taskModel.setType(ONE);
        taskModel.setSpaceTime(SPUtils.getInt(KEY_SPACE_TIME_ONE, 120));
        taskModel.setCount(1);
        taskModel.setData(getPointModels(BUSINESS, AGRICULTURAL, SOLDIERS));
        return taskModel;
    }

    public static void resetTaskModel() {
        sTaskModelList = null;
        getTaskModel();
    }

    public static List<TaskModel> getTaskModel() {
        if (sTaskModelList == null) {
            synchronized (TaskModel.class) {
                if (sTaskModelList == null) {
                    sTaskModelList = getTask(60);
                }
            }
        }
        return sTaskModelList;
    }

    public static List<TaskElement> getTaskElement() {
        List<TaskModel> taskModels = getTaskModel();
        List<TaskElement> result = new ArrayList<>();
        result.add(new StartAndLoginTaskElement(new TaskModel("登录")));
        result.add(new JoinGameTaskElement(new TaskModel("进入游戏")));
        for (TaskModel model : taskModels) {
            TaskElement element = createTaskElement(model);
            if (element == null) {
                continue;
            }
            result.add(element);
        }
        LogUtils.logd(taskModels.toString());
        return result;
    }

    private static TaskElement createTaskElement(TaskModel model) {
        switch (model.getType()) {
            case ONE:
                return new JyzcTaskElement(model);
            case TASK_ZHENG_WU:
                return new ClzwTaskElement(model);
            case TASK_MO_BAI:
                return new MobaiTaskElement(model);
            case TASK_FENG_LU:
                return new FengluTaskElement(model);
            case TASK_SHU_YUAN:
                return new ShuyuanTaskElement(model);
        }
        return null;
    }


    private static String sDefUserInfo =
//            "ck52434333,520333&ck83250887,520333&ck84012149,520333&ck75077701,520333&ck74266770,520333&" +
//            "ck56270983,520333&ck41351036,520333&" +
//            "ck97381288,520333&ck68721497,520333&ck99371248,520333&ck21627506,520333&" +
            "ck69539153,520333&ck82369145,520333&ck19656822,520333&ck92984644,520333";
    public static long nextDayTime;

    public static void init() {
        File parentFile = CmdData.saveFilePath.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        getTaskModel();
        if (TextUtils.isEmpty(SPUtils.getString(INFO_KEY))) {
            saveUserInfo(sDefUserInfo);
        }
        refreshNextDayTime();

//        List<GuanKaPoint> points = DbCore.getDaoSession().getGuanKaPointDao().loadAll();
//        if (points == null || points.size() == 0) {
//            String string = JsonUtils.getJsonFromMusic("guanka.json");
//            if (TextUtils.isEmpty(string)) {
//                string = JsonUtils.getJson("guanka.json", BaseApplication.getAppContext());
//            }
//            points = (List<GuanKaPoint>) JsonUtils.fromJson(string, new TypeToken<List<GuanKaPoint>>() {
//            }.getType());
//            for (GuanKaPoint point : points) {
//                DbCore.getDaoSession().getGuanKaPointDao().insertOrReplace(point);
//            }
////            LogUtils.logd("points:" + points.toString());
//        }

    }

    public static void refreshNextDayTime() {
        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.HOUR, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.add(Calendar.DATE, 1);
        nextDayTime = instance.getTimeInMillis();
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(instance.getTime());
        LogUtils.logd("format:" + format);
    }


    public static String getColor(Bitmap bitmap, int x, int y) {
        if (bitmap == null) {
            return "";
        }
        int pixel = bitmap.getPixel(x, y);
        return getColorHtml(pixel);
    }

    public static String getColor(PointModel model) {
        return getColor(ScreenCapture.get().getCurrentBitmap(), model.getX(), model.getY());
    }

    public static boolean checkColor(Bitmap bitmap, PointModel pointModel) {
        if (bitmap == null || pointModel == null) {
            return false;
        }
        String color = getColor(bitmap, pointModel.getX(), pointModel.getY());
        LogUtils.logd("color:" + color + " pointModel:" + pointModel.toString());
        return color.equals(pointModel.getNormalColor()) || likeColor(color, pointModel.getNormalColor());
    }

    public static boolean checkColor(PointModel pointModel) {
        if (TaskUtil.bitmap == null || pointModel == null) {
            return false;
        }
        String color = getColor(TaskUtil.bitmap, pointModel.getX(), pointModel.getY());
        LogUtils.logd("color:" + color + " pointModel:" + pointModel.toString());
        return color.equals(pointModel.getNormalColor()) || likeColor(color, pointModel.getNormalColor());
    }
    public static boolean checkSubColor(PointModel pointModel) {
        if (TaskUtil.bitmap == null || pointModel == null || TextUtils.isEmpty(pointModel.getSubColor())) {
            return false;
        }
        String color = getColor(TaskUtil.bitmap, pointModel.getX(), pointModel.getSubY());
        LogUtils.logd("color:" + color + " pointModel:" + pointModel.toString());
        return color.equals(pointModel.getSubColor()) || likeColor(color, pointModel.getSubColor());
    }
    public static boolean checkColorAndOffset(PointModel pointModel) {
        if (TaskUtil.bitmap == null || pointModel == null) {
            return false;
        }
        String color = getColor(TaskUtil.bitmap, pointModel.getX(), pointModel.getY()+OrcConfig.offsetHeight );
        LogUtils.logd("checkColorAndOffset color:" + color + " offsetHeight:"+OrcConfig.offsetHeight + " pointModel:" + pointModel.toString());
        return color.equals(pointModel.getNormalColor()) || likeColor(color, pointModel.getNormalColor());
    }

    public static boolean checkColorAndClick(PointModel pointModel) {
        boolean isTrue = checkColor(pointModel);
        if (isTrue) {
            AutoTool.execShellCmd(pointModel);
        }
        return isTrue;
    }

    public static boolean checkColor(PointModel pointModel, int offset, int xiangXi) {
        if (TaskUtil.bitmap == null || pointModel == null) {
            return false;
        }
        String color = getColor(TaskUtil.bitmap, pointModel.getX(), pointModel.getY());
        LogUtils.logd("color:" + color + " pointModel:" + pointModel.toString());
        return color.equals(pointModel.getNormalColor()) || likeColor(color, pointModel.getNormalColor(), offset, xiangXi);
    }

    public static boolean checkColor(Bitmap bitmap, PointModel pointModel, PointModel pointModel2) {
        return checkColor(pointModel) || checkColor(pointModel2);
    }

    public static boolean likeColor(int old, int newColor) {
        return likeColor(old, newColor, 8, 1);
    }

    public static boolean likeColor(int old, int newColor, int offset, int xianXi) {
        int redO = Color.red(old);
        int greenO = Color.green(old);
        int blueO = Color.blue(old);
        int redN = Color.red(newColor);
        int greenN = Color.green(newColor);
        int blueN = Color.blue(newColor);
        int absR = Math.abs(redO - redN);
        int absG = Math.abs(greenO - greenN);
        int absB = Math.abs(blueO - blueN);
        int count = 0;
        if (absR < offset) count++;
        if (absG < offset) count++;
        if (absB < offset) count++;
        return count > xianXi;
    }

    public static boolean likeColor(String old, String newColorStr) {
        return likeColor(Color.parseColor(old), Color.parseColor(newColorStr));
    }

    public static boolean likeColor(String old, String newColorStr, int offset) {
        return likeColor(Color.parseColor(old), Color.parseColor(newColorStr), offset, 1);
    }

    public static boolean likeColor(String old, String newColorStr, int offset, int count) {
        return likeColor(Color.parseColor(old), Color.parseColor(newColorStr), offset, count);
    }

    public static boolean checkColor(int color, PointModel pointModel) {
        if (pointModel == null) {
            return false;
        }
        return getColorHtml(color).equals(pointModel.getNormalColor());
    }

    public static void setResLastTime(int time) {
        List<TaskModel> taskModel1 = getTaskModel();
        for (TaskModel taskModel : taskModel1) {
            if (taskModel.getType() == ONE) {
                taskModel.setLastRefreshTime(time);
                return;
            }
        }
    }


    public static String getColorHtml(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }


    public static int[] getColorRGB(int selectedColor) {
        int[] rgb = new int[3];
        int color = (int) Long.parseLong(String.format("%06X", (0xFFFFFF & selectedColor)), 16);
        rgb[0] = (color >> 16) & 0xFF; // hex to int : R
        rgb[1] = (color >> 8) & 0xFF; // hex to int : G
        rgb[2] = (color >> 0) & 0xFF; // hex to int : B
        return rgb;
    }


    public static Bitmap getCapBitmap() {
        return getCapBitmap(false);
    }

    public static Bitmap getCapBitmap(boolean delete) {
        if (delete) {
            CmdData.saveFilePath.delete();
        }
        return getCapBitmap(1500);
    }


    public static Bitmap getCapBitmap(long time) {


        try {
            AutoTool.execShellCmd(CmdData.screenCap);
            Thread.sleep(time);
            while (true) {
                TaskUtil.bitmap = BitmapFactory.decodeFile(pathName);
                Thread.sleep(250);
                if (TaskUtil.bitmap != null) break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return TaskUtil.bitmap;
    }

    public static Bitmap getCapBitmapNew() {
//        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 21) {
            ScreenCapture.startCaptureSync();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TaskUtil.bitmap = ScreenCapture.get().getCurrentBitmap();
            return ScreenCapture.get().getCurrentBitmap();
        }
        try {
            AutoTool.execShellCmd(CmdData.screenCap);
            Thread.sleep(1500);
            while (true) {
                TaskUtil.bitmap = BitmapFactory.decodeFile(pathName);
                Thread.sleep(250);
                if (TaskUtil.bitmap != null) break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return TaskUtil.bitmap;
    }

    public static Bitmap getCapBitmapWithOffset(){
        Bitmap bitmap = getCapBitmapNew();
        if (bitmap==null){
            return null;
        }
        if (bitmap.getHeight()>1920){
            OrcConfig.offsetHeight = (bitmap .getHeight() - 1920 -OrcHelper.getInstance().getNavigationBarHeight() ) / 2;
        }else{
            OrcConfig.offsetHeight =0;
        }
        return bitmap;
    }

    private static List<PointModel> shuYanModel;

    public static List<PointModel> getRenShuYanModel() {
        if (shuYanModel == null) {
            shuYanModel = new ArrayList<>();
            shuYanModel.add(CmdData.get(ACADEMY_GET_1));
            shuYanModel.add(CmdData.get(ACADEMY_GET_2));
            shuYanModel.add(CmdData.get(ACADEMY_GET_3));
            shuYanModel.add(CmdData.get(ACADEMY_GET_4));
        }
        return shuYanModel;
    }

    public static List<PointModel> getShuYanModel() {
        List<PointModel> shuYanModel = new ArrayList<>();
        shuYanModel.add(CmdData.get(SHUYUAN_GET_1));
        shuYanModel.add(CmdData.get(SHUYUAN_GET_2));
        shuYanModel.add(CmdData.get(SHUYUAN_GET_3));
        shuYanModel.add(CmdData.get(SHUYUAN_GET_4));
        shuYanModel.add(CmdData.get(SHUYUAN_GET_5));
        shuYanModel.add(CmdData.get(SHUYUAN_GET_6));
        return shuYanModel;
    }

    public static boolean checkColor(Bitmap bitmap, PointModel pointModel, int min, int max) {
        if (bitmap == null || pointModel == null) {
            return false;
        }
        boolean checkColor;
        int normalY = pointModel.getY();
        for (int i = min; i <= max; i += 4) {
            String color = getColor(bitmap, pointModel.getX(), normalY - i);
            checkColor = color.equals(pointModel.getNormalColor()) || likeColor(color, pointModel.getNormalColor());
            if (checkColor) {
                LogUtils.logd("color:" + color + " pointModel:" + pointModel.toString());
                return true;
            }
        }
        return false;
    }

    public static boolean checkColor(Bitmap bitmap, PointModel pointModel, int min, int max, int offset) {
        if (bitmap == null || pointModel == null) {
            return false;
        }
        boolean checkColor;
        int normalY = pointModel.getY();
        for (int i = min; i <= max; i += 2) {
            String color = getColor(bitmap, pointModel.getX(), normalY - i);
            checkColor = color.equals(pointModel.getNormalColor()) || likeColor(color, pointModel.getNormalColor(), offset, 2);
            if (checkColor) {
                LogUtils.logd("color:" + color + " pointModel:" + pointModel.toString() + " y:" + i);
                return true;
            }
        }
        return false;
    }

    static String[] colors = {"#B16222", "#7D4F1D", "#A88230", "#FEF787"};

    public static boolean guanKaPosition(String color, int offset) {
        for (String s : colors) {
            if (Util.likeColor(s, color, offset, 2)) return true;
        }
        return false;
    }

    public static File saveFile;
    private static final String INFO_KEY = "INFO_KEY";

    public static List<UserInfo> getUserInfo() {
        String infos = SPUtils.getString(INFO_KEY);
        if (TextUtils.isEmpty(infos)) {
            return null;
        }
        return (List<UserInfo>) JsonUtils.fromJson(infos, new TypeToken<List<UserInfo>>() {
        }.getType());
    }

    public static void saveUserInfo(String userInfo) {
        String[] userInfos = userInfo.split("&");
        if (userInfos.length > 0) {
            try {
                List<UserInfo> userInfoList = new ArrayList<>();
                for (String info : userInfos) {
                    if (info.contains(",")) {
                        String[] split = info.split(",");
                        userInfoList.add(new UserInfo(split[0], split[1]));
                    } else {
                        userInfoList.add(new UserInfo(info, ""));
                    }
                }
                saveUserInfo(userInfoList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveUserInfo(List<UserInfo> userInfos){
        SPUtils.setString(INFO_KEY, JsonUtils.toJson(userInfos));
    }

    public static void stopTask() {
        Util.isWPZMGServiceRunning = false;
        Context context = BaseApplication.getAppContext();
        Intent intent2 = new Intent(context, WPZMGService2.class);
        if (!Util.isWPZMGServiceRunning) {
            intent2.putExtra("stop", true);
            Util.setResLastTime(0);
        }
        context.startService(intent2);
    }

    public static synchronized String getFileString(String key) {
        initSaveFile();
        return ACache.get(saveFile).getAsString(key);
    }

    public static synchronized String getFengLuFileString(String key) {
        if (BaseApplication.densityDpi == 480) {
            initSaveFile();
            fengLuFile = saveFile;
        } else {
            initFengLu();
        }
        return ACache.get(fengLuFile).getAsString(key);
    }

    private static void initFengLu() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "/fengLu");
        if (!file.exists()) {
            file.mkdir();
        }
        if (fengLuFile == null) {
            String format = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            fengLuFile = new File(file, "/fl" + format);
        }
        if (!fengLuFile.exists()) {
            fengLuFile.mkdir();
        }
    }

    public static synchronized void saveLastRefreshTime(String user, String type, String time, int saveTime) {
        initSaveFile();
        ACache.get(saveFile).put(String.format("%s%s", user, type), time, saveTime);
    }

    private static void initSaveFile() {
        if (saveFile == null) {
            saveFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        }
    }

    private static File fengLuFile;

    public static synchronized void saveFengLuLastRefreshTime(String user, String type, String time, int saveTime) {
        if (BaseApplication.densityDpi == 480) {
            initSaveFile();
            fengLuFile = saveFile;
        } else {
            initFengLu();
        }
        ACache.get(fengLuFile).put(String.format("%s%s", user, type), time, saveTime);
    }

    public static synchronized void lockFengLu(String user, String type, String time, int saveTime) {
        if (BaseApplication.densityDpi == 480) {
            initSaveFile();
            fengLuFile = saveFile;
        } else {
            initFengLu();
        }
        ACache.get(fengLuFile).put(String.format("%s%s", user, type), time, saveTime);
    }

    public static synchronized void unLockFengLu(String user, String type) {
        ACache.get(fengLuFile).remove(String.format("%s%s", user, type));
    }


    public static boolean checkTime(UserInfo userInfo, String type, int saveTime) {
        LogUtils.logd("checkTime:" + userInfo.getName() + " type:" + type + "  saveTime:" + saveTime);
//        return false;
        if (TextUtils.isEmpty(Util.getFileString(String.format("%s%s", userInfo.getName(), type)))) {
//            Util.saveLastRefreshTime(userInfo.getName(), type, "1", saveTime);
            return false;
        }
        return true;
    }
    public static void saveLastRefreshTime( String type, int saveTime) {
        Util.saveLastRefreshTime(TaskState.get().getUserInfo().getName(), type, "1", saveTime);
    }

    public static boolean checkTime(UserInfo userInfo, String type, int saveTime, boolean isFengLu) {
        LogUtils.logd("checkTime:" + userInfo.getName() + " type:" + type + "  saveTime:" + saveTime);
        return false;
//        if (TextUtils.isEmpty(Util.getFengLuFileString(String.format("%s%s", userInfo.getName(), type)))) {
//            if (isFengLu) {
//                Util.saveFengLuLastRefreshTime(userInfo.getName(), type, "1", saveTime);
//            } else {
//                Util.saveLastRefreshTime(userInfo.getName(), type, "1", saveTime);
//            }
//            return false;
//        }
//        return true;
    }

    public static int getSaveTime() {
        return (int) ((nextDayTime - System.currentTimeMillis()) / 1000);
    }

    public static void exit(final Activity activity) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.stopService(new Intent(activity, MainService.class));
                activity.finish();
            }
        }, 300);
    }

    public static boolean checkHasGengXin() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (day == 4) {
            if (hour > 16) return true;
        } else if (day == 5) {
            if (hour < 10) return true;
        }
        LogUtils.logd("day :" + day + " hour:" + hour);
        return false;
    }


    public static long getAvailMemory() {// 获取android当前可用内存大小
        Context context = BaseApplication.getAppContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        long usedMem = mi.totalMem - mi.availMem;
        LogUtils.logd("usedMem:" + usedMem + "  usedMem:" + Formatter.formatFileSize(context, usedMem));
        //mi.availMem; 当前系统的可用内存
        return usedMem;
    }

    public static String getAvailMemoryStr() {// 获取android当前可用内存大小
        Context context = BaseApplication.getAppContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        long usedMem = mi.totalMem - mi.availMem;
        LogUtils.logd("usedMem:" + usedMem + "  usedMem:" + Formatter.formatFileSize(context, usedMem));
        //mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
    }

    public static String getTotalMemory() {
        Context context = BaseApplication.getAppContext();
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }


    /**
     * 唤醒手机屏幕并解锁
     */
    public static void wakeUpAndUnlock() {
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) BaseApplication.getAppContext().getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if (!screenOn) {
            // 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire(10000); // 点亮屏幕
            wl.release(); // 释放
        }
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) BaseApplication.getAppContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁
    }

//    public static void lock() {
//        PowerManager pm = (PowerManager) BaseApplication.getAppContext().getSystemService(Context.POWER_SERVICE);
////        pm.goToSleep(SystemClock.uptimeMillis());
//        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
//        wakeLock.acquire();
//        wakeLock.release();
//    }

//
//    public void setAlg(int minute) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MINUTE, minute);
//        Intent alarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM);
//        alarmIntent.putExtra(AlarmClock.EXTRA_MESSAGE, "提醒消息 下午去XXX开会");
//        alarmIntent.putExtra(AlarmClock.EXTRA_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
//        alarmIntent.putExtra(AlarmClock.EXTRA_MINUTES, calendar.get(Calendar.MINUTE) + 1);
//        alarmIntent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
//        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(alarmIntent);
//    }


    public static List<OrcModel> getPageData() {
        return OrcHelper.getInstance().executeCallSync(TaskUtil.bitmap);
    }

    public static List<OrcModel> getBitmapAndPageData() {
        return OrcHelper.getInstance().executeCallSync(Util.getCapBitmapNew());
    }

    public static void setNewCoord(PointModel model, Result.ItemsBean.ItemcoordBean coord) {
        if (model == null || model.isReset()) {
            LogUtils.logd("model is null");
            return;
        }
        int oldX = model.getX();
        int oldY = model.getY();
        String oldColor = model.getNormalColor();
        model.setX(coord.getX() + coord.getWidth() / 2);
        model.setY(coord.getY() + coord.getHeight() / 2);
        model.setNormalColor(Util.getColor(model));
        LogUtils.logd(model.getName() + "oldX:" + oldX + " newX:" + model.getX());
        LogUtils.logd("oldY:" + oldY + " newY:" + model.getY());
        LogUtils.logd("oldColor:" + oldColor + " newColor:" + model.getNormalColor());
        model.setReset(true);
    }
}
