package com.example.administrator.opencvdemo.util;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.model.GuanKaPoint;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.model.UserInfo;
import com.example.administrator.opencvdemo.youtu.ImageParse;
import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2018/1/6 16:32
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2018/1/6$
 * @updateDes ${TODO}
 */

public class TaskUtil implements Constant {

    public static boolean isDestory;


    private static int startX;
    private static int endX;
    private static int startY;
    private static int endY;


    private static boolean checkPositionNew(PointModel guanKaDialogClose, PointModel plotPoint, PointModel bloodVolume, List<GuanKaPoint> guanKaPoints) throws
            InterruptedException {
        PointModel xxStart = CmdData.get(GUAN_KA_XX_START);
        PointModel xxEnd = CmdData.get(GUAN_KA_XX_END);
        startX = xxStart.getX();
        endY = xxEnd.getY();
        endX = xxEnd.getX();
        startY = xxStart.getY();
        int xMargin = 2;
        int yMargin = 2;
        int count = 0;
        long time = System.currentTimeMillis();
        if (guanKaPoints != null && guanKaPoints.size() > 0) {
            for (GuanKaPoint guanKaPoint : guanKaPoints) {
                String color = Util.getColor(bitmap, guanKaPoint.getX(), guanKaPoint.getY());
                if (Util.guanKaPosition(color, 2)) {
                    AutoTool.execShellCmd(CmdData.clickInt(guanKaPoint.getX(), guanKaPoint.getY()));

                    Thread.sleep(800);
                    LogUtils.logd("color:" + color + " guanKaPoint:" + guanKaPoint);
                    if (isFirstPlot) {
                        Thread.sleep(1000);
                        isFirstPlot = false;
                        return true;
                    }
                    for (int k = 0; k < 5; k++) {
                        AutoTool.execShellCmd(bloodVolume);
                        Thread.sleep(isNewApi ? 900 : 700);
                    }
                    LogUtils.logd("currentGk:" + currentGk);
                    if (currentGk < 4) {

                        saoTu(startZhanDou, guanKaDialogClose, guanKa);
                    }
                    return true;
                }
            }
        }
        while (true) {
            for (int i = endY; i >= startY; i -= yMargin) {
                for (int j = startX; j < endX; j += xMargin) {
                    String color = Util.getColor(bitmap, j, i);
                    if (!TextUtils.isEmpty(color) && (Util.guanKaPosition(color, 2))) {
                        GuanKaPoint guanKaPoint = new GuanKaPoint();
                        guanKaPoint.setX(j);
                        guanKaPoint.setY(i);
                        guanKaPoint.setColor(color);
                        guanKaPoints.add(guanKaPoint);
//                        DbCore.getDaoSession().getGuanKaPointDao().insertOrReplace(guanKaPoint);
                        String toJson = JsonUtils.toJson(guanKaPoints);
                        SPUtils.setString("guanKaPoint", toJson);
                        LogUtils.logd("guanKaPoint: " + toJson);
                        AutoTool.execShellCmd(CmdData.clickInt(j, i));
                        Thread.sleep(800);
                        LogUtils.logd("color:" + color + "  x:" + j + " y:" + i);
                        if (isFirstPlot) {
                            Thread.sleep(1000);
                            isFirstPlot = false;
                            return true;
                        }
                        for (int k = 0; k < 5; k++) {
                            AutoTool.execShellCmd(bloodVolume);
                            Thread.sleep(isNewApi ? 900 : 700);
                        }
                        if (currentGk < 4) {
                            saoTu(startZhanDou, guanKaDialogClose, guanKa);
                        }
                        return true;
                    }
                }
            }
            endY -= 1;
            startX += 1;
            if (count == 2) {
                break;
            } else {
                count++;
            }
        }
        LogUtils.logd("checkPositionNew time:" + (System.currentTimeMillis() - time));
        startX = xxStart.getX();
        endY = xxEnd.getY();
        xMargin = (endX - startX) / 8;
        yMargin = (endY - startY) / 8;
        for (int i = endY; i >= startY; i -= yMargin) {
            if (isDestory) return false;
            for (int j = startX; j < endX; j += xMargin) {
                if (isDestory) return false;
                AutoTool.execShellCmd(CmdData.clickInt(j, i));
                Thread.sleep(100);
            }
            if (isNewApi) {
                Thread.sleep(600);
            }
            Util.getCapBitmapNew();
            if (Util.checkColor(bitmap, guanKaDialogClose, plotPoint)) return true;
        }
        Thread.sleep(1000);
        return false;
    }


    private static boolean isFirstPlot;
    private static int currentGk;
    private static PointModel startZhanDou;
    private static PointModel guanKa;

    public static void sixTaskNewV2(TaskModel task, UserInfo userInfo) {
//        isDestory = false;
//        guanKa = CmdData.get(GUAN_KA);
//        final PointModel guanKaDialogClose = CmdData.get(GUAN_KA_DIALOG_CLOSE);
//        startZhanDou = CmdData.get(GUAN_KA_START_ZHUAN_DOU_BG);
//        final PointModel boss = CmdData.get(GUAN_KA_BOSS);
//        final PointModel netPoint = CmdData.get(Constant.NET_CLOSE);
//        final PointModel plotPoint = CmdData.get(Constant.GUAN_KA_VICTORY);//遮盖的战斗对话框关闭
//        final PointModel positionClose = CmdData.get(Constant.GUAN_KA_POSITION_CLOSE);//地图关闭
//        final PointModel bloodVolume = CmdData.get(Constant.GUAN_KA_BLOOD_VOLUME);//血量
//        final PointModel positionPlot = CmdData.get(Constant.GUAN_KA_POSITION_PLOT);//血量
//        final PointModel positionPlot2 = CmdData.get(Constant.GUAN_KA_POSITION_PLOT2);//血量
//        List<GuanKaPoint> points = DbCore.getDaoSession().getGuanKaPointDao().loadAll();
//        currentGk = 10;
//        isFirstPlot = true;
//        try {
//            AutoTool.execShellCmdClose();
//            Thread.sleep(800);
//            AutoTool.execShellCmd(guanKa);
//            Thread.sleep(isNewApi ? 2000 : 2000);
//            while (true) {
//                if (isDestory) return;
//                isEnd = false;
//                Util.getCapBitmapNew();
//                if (checkExp(bitmap, netPoint, "当前网络异常")) continue;//检查网络环境
//                if (Util.checkColor(bitmap, positionClose)) { //扫图
//                    boolean checkPosition = TaskUtil.checkPositionNew(guanKaDialogClose, plotPoint, bloodVolume, points);
//                    currentGk++;
//                    if (checkPosition) {
//                        Thread.sleep(800);
//                        continue;
//                    }
//                } else if (Util.checkColor(bitmap, guanKaDialogClose, plotPoint)) { //战斗
//                    saoTu(startZhanDou, guanKaDialogClose, guanKa);
//                    continue;
//                } else if (Util.checkColor(bitmap, bloodVolume)) {//boss
//                    daBoss(guanKa, boss, positionPlot, positionPlot2);
//                    continue;
//                } else if (Util.checkColor(bitmap, positionPlot, positionPlot2)) {
//                    for (int i = 0; i < 6; i++) {
//                        AutoTool.execShellCmd(bloodVolume);
//                        Thread.sleep(800);
//                    }
//                    continue;
//                }
//                ImageParse.getSyncData(new ImageParse.Call() {
//                    @Override
//                    public void call(List<Result.ItemsBean> result) {
//                        if (result.size() == 0) return;
//                        try {
//                            for (Result.ItemsBean itemsBean : result) {
//                                if (itemsBean.getItemstring().contains("开始") || itemsBean.getItemstring().contains("战胜") || itemsBean.getItemstring().contains("战斗") || Util
//                                        .checkColor(bitmap, guanKaDialogClose, plotPoint)) {
//                                    if (saoTu(startZhanDou, guanKaDialogClose, guanKa)) isEnd = true;
//                                    return;
//                                } else if (itemsBean.getItemstring().contains("血量")) {
//                                    daBoss(guanKa, boss, positionPlot, positionPlot2);
//                                    return;
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                if (isEnd) return;
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static boolean saoTu(PointModel startZhanDou, PointModel guanKaDialogClose, PointModel guanKa) throws InterruptedException {
        int count = 8;
        if (isNewApi) {
            while (true) {
                if (isDestory) return true;
                count = 4;
                for (int i = 0; i < count; i++) {
                    AutoTool.execShellCmd(startZhanDou);
                    Thread.sleep(2900);
                    AutoTool.execShellCmd(startZhanDou);
                    Thread.sleep(500);
                }
                Util.getCapBitmapNew();
                if (!Util.checkColor(guanKaDialogClose)) {
                    Thread.sleep(1000);
                    break;
                }
            }
        } else {
            if (isDestory) return true;
            for (int i = 0; i < count; i++) {
                AutoTool.execShellCmd(startZhanDou);
                Thread.sleep(3000);
                AutoTool.execShellCmd(startZhanDou);
                Thread.sleep(650);
            }
            Thread.sleep(1000);
        }

        return false;
    }

    private static void daBoss(PointModel guanKa, PointModel boss, PointModel pointModel, PointModel pointModel2) throws InterruptedException {
        AutoTool.execShellCmd(boss);
        Thread.sleep(2000);
        AutoTool.execShellCmd(boss);
        Thread.sleep(1400);
        AutoTool.execShellCmd(boss);
        Thread.sleep(800);
        AutoTool.execShellCmd(boss);
        currentGk = 0;
        Thread.sleep(800);
        if (isNewApi) {
            while (true) {
                AutoTool.execShellCmd(guanKa);
                Thread.sleep(isNewApi ? 800 : 600);
                Util.getCapBitmapNew();
                if (!Util.checkColor(bitmap, pointModel, pointModel2)) {
                    return;
                }
            }
        } else {
            for (int i = 0; i < 5; i++) {
                AutoTool.execShellCmd(guanKa);
                Thread.sleep(isNewApi ? 800 : 600);
            }
        }

    }

    private static boolean isEnd;
    private static boolean isFirstOneKey;

    /**
     * 书院
     *
     * @param task
     * @param userInfo
     */
    public static void shuYan(TaskModel task, UserInfo userInfo) {
        if (Util.checkTime(userInfo, KEY_SHU_YUAN, 1800)) {
            return;
        }
        isDestory = false;
        PointModel academy = CmdData.get(ACADEMY);
        final PointModel academyGetOk = CmdData.get(ACADEMY_GET_OK);
        final PointModel oneKey = CmdData.get(ACADEMY_ONE_KEY);
        PointModel netPoint = CmdData.get(Constant.NET_CLOSE);
        try {
            //   200898
            sleep(1000);
            AutoTool.execShellCmdChuFu();
            sleep(1000);
            isEnd = false;
            isFirstOneKey = true;
            AutoTool.execShellCmd(academy);
            Thread.sleep(BaseApplication.densityDpi == 480 ? 3200 : BaseApplication.getScreenWidth() == 1080 ? 1200 : 2500);
            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
                ImageParse.getSyncData(new ImageParse.Call() {
                    @Override
                    public void call(List<Result.ItemsBean> result) {
                        if (result.size() == 0) return;
                        try {
                            for (Result.ItemsBean itemsBean : result) {
                                if (isDestory) {
                                    isEnd = true;
                                    return;
                                }
                                if (itemsBean.getItemstring().contains("/") && isFirstOneKey) {
                                    String str = itemsBean.getItemstring();
                                    isEnd = !(str.contains("/7") || str.contains("/8") || str.contains("/9") || str.contains("/10"));
                                    if (!isEnd) {
                                        AutoTool.execShellCmd(oneKey);
                                        sleep(4000);
                                    }
                                    isFirstOneKey = false;
                                }
                                if (itemsBean.getItemstring().contains("已完成")) {
                                    if (isEnd) {
                                        AutoTool.execShellCmd(CmdData.clickInt(itemsBean.getItemcoord().getX(), itemsBean.getItemcoord().getY()));
                                        Thread.sleep(4000);
                                        excShuYuan(itemsBean, academyGetOk);
                                    } else {
                                        excShuYuan(itemsBean, academyGetOk);
                                    }

                                } else if (itemsBean.getItemstring().contains("请点击") || itemsBean.getItemstring().contains("门客学习")) {
                                    excShuYuan(itemsBean, academyGetOk);
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            isEnd = true;
                        }
                    }
                });
                LogUtils.logd("");
                if (isEnd) {
                    break;
                } else {
                    AutoTool.execShellCmd(CmdData.swipeY(BaseApplication.getScreenHeight() - 100, 350));
                    sleep(1000);
                    isEnd = true;
                }
            }
            isFirstOneKey = true;
            AutoTool.execShellCmdClose();
            sleep(600);
            AutoTool.execShellCmdChuFu();
            sleep(600);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void excShuYuan(Result.ItemsBean itemsBean, PointModel academyGetOk) throws InterruptedException {
        AutoTool.execShellCmd(CmdData.clickInt(itemsBean.getItemcoord().getX() + 20, itemsBean.getItemcoord().getY() + 20));
        Thread.sleep(1200);
        AutoTool.execShellCmd(academyGetOk);
        Thread.sleep(1000);
    }

    public static boolean checkExp(Bitmap bitmap, PointModel model, String msg) throws InterruptedException {
        if (bitmap == null) return true;
        if (Util.getColor(bitmap, model.getX(), model.getY()).equals(model.getNormalColor())) { //检查网络环境
            AutoTool.execShellCmd(model);
            Thread.sleep(600);
            return true;
        }
        return false;
    }

    public static boolean checkExp(PointModel model, String msg) throws InterruptedException {
        if (bitmap == null) return true;
        if (Util.getColor(bitmap, model.getX(), model.getY()).equals(model.getNormalColor())) { //检查网络环境
            AutoTool.execShellCmd(model);
            Thread.sleep(600);
            return true;
        }
        return false;
    }


    public static boolean zhengWu(PointModel zhengJi, PointModel daoJu, List<Result.ItemsBean> result) {
        int count = 0;
        try {
            if (result == null || result.size() == 0) return true;
            for (Result.ItemsBean bean : result) {
                if (bean.getItemstring().contains("政务事件")) {
                    count = getCount(bean.getItemstring(), "政务事件");
                    break;
                }
            }
            if (count == 0) return true;
            for (int i = 0; i < count; i++) {
                if (i > 0) {
                    Thread.sleep(1600);
                }
                AutoTool.execShellCmd(zhengJi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private static int duration = 300;

    public static boolean getOneCount(List<Result.ItemsBean> result, List<PointModel> pointModels) {
        Map<String, Integer> data = new HashMap<>();
        if (result == null || result.size() == 0) return true;
        for (Result.ItemsBean bean : result) {
            String itemstring = bean.getItemstring();
            if (itemstring.contains("经营农产")) {
                getCount(itemstring, "经营农产", data);
            } else if (itemstring.contains("营商产")) {
                getCount(itemstring, "经营商产", data);
            } else if (itemstring.contains("招募士兵")) {
                getCount(itemstring, "招募士兵", data);
            }
            if (data.size() >= 3) {
                break;
            }
        }
        if (data.size() == 0) return true;
        try {
            int count = data.get("经营商产");
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    AutoTool.execShellCmd(pointModels.get(0));
                    Thread.sleep(duration);
                }
            }
            count = Math.min(data.get("经营农产"), data.get("招募士兵"));
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    AutoTool.execShellCmd(pointModels.get(1));
                    Thread.sleep(duration);
                    AutoTool.execShellCmd(pointModels.get(2));
                    Thread.sleep(duration);
                }
            }
            if (count < data.get("经营农产")) {
                count = data.get("经营农产") - count;
                for (int i = 0; i < count; i++) {
                    AutoTool.execShellCmd(pointModels.get(1));
                    Thread.sleep(duration);
                }
            }
            if (count < data.get("招募士兵")) {
                count = data.get("招募士兵") - count;
                for (int i = 0; i < count; i++) {
                    AutoTool.execShellCmd(pointModels.get(2));
                    Thread.sleep(duration);
                }
            }
            Thread.sleep(isNewApi ? 600 : 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static int getCount(String str, String tag) {
        if (TextUtils.isEmpty(str)) return 0;
        LogUtils.logd("str:" + str);
        if (str.contains("/")) {
            String replace = str.replace(tag, "");
            replace = replace.replace("营商产", "");
            replace = replace.replace(":", "");
            replace = replace.replace(" ", "");
            String[] split = replace.split("/");
            return Integer.parseInt(split[0]);
        }
        return 0;
    }

    public static int getCount(String str, String tag, Map<String, Integer> data) {
        data.put(tag, getCount(str, tag));
        return 0;
    }

    public static void twoTask(TaskModel task, UserInfo userInfo) {
        if (Util.checkTime(userInfo, KEY_WORK_ZW, ACache.TIME_HOUR * 2)) {
            return;
        }
        PointModel netPoint = CmdData.get(Constant.NET_CLOSE);
        PointModel shiye = CmdData.get(SHI_YE);
        PointModel getZhengJi = CmdData.get(ZHENG_JI_GET);
        PointModel zhengJiBG = CmdData.get(ZHENG_JI_BG);
        PointModel daoJu = CmdData.get(DIALOG_CLOSE3);
        try {
            AutoTool.execShellCmd(shiye);
            Thread.sleep(600);
            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
                if (Util.checkColor(zhengJiBG)) {
                    AutoTool.execShellCmd(daoJu);
                    Thread.sleep(800);
                    AutoTool.execShellCmd(CmdData.screenClose);
                    Thread.sleep(800);
                    break;
                } else {
                    AutoTool.execShellCmd(getZhengJi);
                    Thread.sleep(isNewApi ? 1500 : 400);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void yanHuiNew(TaskModel task, UserInfo userInfo) {
        if (!isDestory) {
            isDestory = true;
        }
        isDestory = false;
        isEnd = false;
        PointModel jiuLou = CmdData.get(YAN_HUI_CLICK);
        final PointModel yanHuiNumber = CmdData.get(YAN_HUI_NUMBER);
        final PointModel yanHuiInput = CmdData.get(YAN_HUI_INPUT);
        final PointModel yanHuiSearch = CmdData.get(YAN_HUI_SEARCH);
        final PointModel yanHuiGo = CmdData.get(YAN_HUI_GO);
        final PointModel yanHuiGaoJi = CmdData.get(YAN_HUI_GAO_JI);
        final PointModel yanHuiPositionStart = CmdData.get(YAN_HUI_POSITION_START);
        final PointModel yanHuiPositionEnd = CmdData.get(YAN_HUI_POSITION_END);
        final PointModel yanHuiPositionLeft = CmdData.get(YAN_HUI_POSITION_LEFT);
        final PointModel yanHuiPositionRight = CmdData.get(YAN_HUI_POSITION_RIGHT);
        final List<PointModel> findPoints = CmdData.getFindPoints();
        PointModel netPoint = CmdData.get(Constant.NET_CLOSE);
        String yanHuiNumberStr = SPUtils.getString(KEY_YAN_HUI_NUMBER);
        yanHuiNumberStr = TextUtils.isEmpty(yanHuiNumberStr) ? "12091951" : yanHuiNumberStr;
        PointModel pointModel = new PointModel("", "宴会位置");
        int fangXiang = 0;
        try {
            final PointModel yanHuiOk = CmdData.get(YAN_HUI_OK);
            final PointModel yanHuiDialogClose = CmdData.get(YAN_HUI_DIALOG_CLOSE);
            sleep(600);
            AutoTool.execShellCmdChuFu();
            sleep(600);
            AutoTool.execShellCmd(CmdData.swipeToLeft);
            sleep(600);
            AutoTool.execShellCmd(jiuLou);
            Thread.sleep(isNewApi ? 1800 : 1200);
            AutoTool.execShellCmd(yanHuiNumber);
            Thread.sleep(800);
            AutoTool.execShellCmd(yanHuiInput);
            Thread.sleep(300);
            AutoTool.execShellCmd(CmdData.inputTextUserInfoName + yanHuiNumberStr);
            Thread.sleep(800);
            AutoTool.execShellCmd(yanHuiSearch);
            Thread.sleep(isNewApi ? 1800 : 600);
            Util.getCapBitmapNew();

            if (Util.checkColor(bitmap, yanHuiGo)) {
                AutoTool.execShellCmd(yanHuiGo);
                Thread.sleep(isNewApi ? 4000 : 3500);
            } else {
                return;
            }
            int count = 0;
            if (findPoints != null) {
                Util.getCapBitmapNew();
                for (PointModel point : findPoints) {
                    if (Util.checkColor(point)) {
                        AutoTool.execShellCmd(point);
                        Thread.sleep(1000);
                        AutoTool.execShellCmd(yanHuiGaoJi);
                        Thread.sleep(1000);
                        Util.getCapBitmapNew();
                        if (Util.checkColor(yanHuiOk)) {
                            return;
                        } else {
                            AutoTool.execShellCmd(yanHuiDialogClose);
                            Thread.sleep(600);
                            AutoTool.execShellCmd(yanHuiDialogClose);
                            Thread.sleep(1000);
                            Util.getCapBitmapNew();
                        }
                    }
                }
            }
            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
                for (int i = yanHuiPositionEnd.getY(); i >= yanHuiPositionStart.getY(); i -= 40) {
                    pointModel.setY(i);
                    if (fangXiang % 2 == 0) {//左侧
                        pointModel.setX(yanHuiPositionLeft.getX());
                        pointModel.setNormalColor(yanHuiPositionLeft.getNormalColor());
                    } else {
                        pointModel.setX(yanHuiPositionRight.getX());
                        pointModel.setNormalColor(yanHuiPositionRight.getNormalColor());
                    }
                    fangXiang++;
                    if (Util.checkColor(bitmap, pointModel)) {
                        findPoints.add(pointModel);
                        SPUtils.setString("findPoint", JsonUtils.toJson(pointModel));
                        LogUtils.logd("findPoint:" + SPUtils.getString("findPoint"));
                        AutoTool.execShellCmd(pointModel);
                        Thread.sleep(1000);
                        AutoTool.execShellCmd(yanHuiGaoJi);
                        Thread.sleep(1000);
                        Util.getCapBitmapNew();
                        if (Util.checkColor(yanHuiOk)) {
                            return;
                        } else {
                            AutoTool.execShellCmd(yanHuiDialogClose);
                            Thread.sleep(600);
                            AutoTool.execShellCmd(yanHuiDialogClose);
                            Thread.sleep(1000);
                            Util.getCapBitmapNew();
                        }
                    }
                }
                if (count == 3) {
                    break;
                }
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void yanHui(TaskModel task, UserInfo userInfo) {
//        if (!isDestory) {
//            isDestory = true;
//        }
//        isDestory = false;
//        isEnd = false;
//        PointModel jiuLou = CmdData.get(YAN_HUI_CLICK);
//        final PointModel yanHuiNumber = CmdData.get(YAN_HUI_NUMBER);
//        final PointModel yanHuiInput = CmdData.get(YAN_HUI_INPUT);
//        final PointModel yanHuiSearch = CmdData.get(YAN_HUI_SEARCH);
//        final PointModel yanHuiGo = CmdData.get(YAN_HUI_GO);
//        final PointModel yanHuiGaoJi = CmdData.get(YAN_HUI_GAO_JI);
//        final List<PointModel> positionTop = CmdData.getYanHuiTop();
//        final List<PointModel> positionBottom = CmdData.getYanHuiBottom();
//        PointModel netPoint = CmdData.get(Constant.NET_CLOSE);
//        String yanHuiNumberStr = SPUtils.getString(KEY_YAN_HUI_NUMBER);
//        yanHuiNumberStr = TextUtils.isEmpty(yanHuiNumberStr) ? "12091951" : yanHuiNumberStr;
//        try {
//            Thread.sleep(600);
//            AutoTool.execShellCmdClose();
//            Thread.sleep(600);
////            AutoTool.execShellCmdClose();
////            Thread.sleep(600);
//            AutoTool.execShellCmd(CmdData.swipeToLeft);
//            Thread.sleep(600);
//            AutoTool.execShellCmd(jiuLou);
//            Thread.sleep(1200);
//            AutoTool.execShellCmd(yanHuiNumber);
//            Thread.sleep(800);
//            AutoTool.execShellCmd(yanHuiInput);
//            Thread.sleep(200);
//            AutoTool.execShellCmd(CmdData.inputTextUserInfoName + yanHuiNumberStr);
//            Thread.sleep(800);
//            AutoTool.execShellCmd(yanHuiSearch);
//            Thread.sleep(600);
//            Util.getCapBitmap();
//
//            if (Util.checkColor(yanHuiGo)) {
//                AutoTool.execShellCmd(yanHuiGo);
//                Thread.sleep(3500);
//            } else {
//                return;
//            }
//            AutoTool.execShellCmd(CmdData.swipe(500, 600, 500, 1200));
//            Thread.sleep(500);
//            while (true) {
//                if (isDestory) return;
//                Util.getCapBitmapNew();
//                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
//                for (int i = 0; i < positionTop.size(); i++) {
//                    PointModel pointModel = positionTop.get(i);
//                    if (Util.checkColor(bitmap, pointModel)) {
//                        AutoTool.execShellCmd(pointModel);
//                        Thread.sleep(800);
//                        AutoTool.execShellCmd(yanHuiGaoJi);
//                        Thread.sleep(800);
//                        return;
//                    }
//                }
//                break;
//            }
//            AutoTool.execShellCmd(CmdData.swipe(500, 1400, 500, 300));
//            Thread.sleep(400);
//            AutoTool.execShellCmd(CmdData.swipe(500, 1400, 500, 300));
//            Thread.sleep(400);
//            while (true) {
//                if (isDestory) return;
//                Util.getCapBitmapNew();
//                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
//                for (int i = 0; i < positionBottom.size(); i++) {
//                    PointModel pointModel = positionBottom.get(i);
//                    if (Util.checkColor(pointModel)) {
//                        AutoTool.execShellCmd(pointModel);
//                        Thread.sleep(800);
//                        AutoTool.execShellCmd(yanHuiGaoJi);
//                        Thread.sleep(800);
//                        return;
//                    }
//                    if (i == positionBottom.size() - 1) {
//                        Util.stopTask();
//                        return;
//                    }
//                }
//                break;
//            }
//            isEnd = false;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static Bitmap bitmap = null;

    public static void chengJiu(TaskModel task, UserInfo userInfo) {
        if (!isDestory) {
            isDestory = true;
        }
        isDestory = false;
        isEnd = false;

        final PointModel chengJiu = CmdData.get(CHENG_JIU_CLICK);
        final PointModel chengJiuLeft = CmdData.get(CHENG_JIU_LEFT);
        final PointModel chengJiuRight = CmdData.get(CHENG_JIU_RIGHT);
        final PointModel chengJiuRed = CmdData.get(Constant.CHENG_JIU_RED_POINT);
        final PointModel close = CmdData.get(CHENG_JIU_CLOSE);
        final List<PointModel> chengJiuGet = CmdData.getChengJiuGet();
        final int leftX = chengJiuLeft.getX();
        final int rightX = chengJiuRight.getX();
        final String normalColor = chengJiuRed.getNormalColor();
        final PointModel pointModel = new PointModel("", "");
        try {
            Thread.sleep(600);
            Util.getCapBitmapNew();
            if (!Util.checkColor(chengJiu)) return;
            AutoTool.execShellCmd(chengJiu);
            Thread.sleep(isNewApi ? 1500 : 800);
            int count = 0;
            while (true) {
                if (isDestory) return;
                ImageParse.getSyncData(new ImageParse.Call() {
                    @Override
                    public void call(List<Result.ItemsBean> result) {
                        if (result.size() == 0) return;
                        try {
                            for (Result.ItemsBean bean : result) {
                                if (isDestory) return;

                                ChengJiuArray.Item item = ChengJiuArray.getItem(bean.getItemstring());
                                LogUtils.logd("item:" + item + " bean:" + bean.toString());
                                if (item == null) continue;

                                pointModel.setNormalColor(normalColor);
                                pointModel.setX(item.getPosition() == 0 ? leftX : rightX);
                                pointModel.setY(bean.getItemcoord().getY());
                                pointModel.setName(item.getName());
                                int count = 0;
                                while (true) {
                                    if (isDestory) return;
                                    if (count > 10) {
                                        break;
                                    }
                                    boolean checkColor = Util.checkColor(bitmap, pointModel, 60, 165, 11);
                                    if (checkColor) {
                                        AutoTool.execShellCmd(pointModel);
                                        Thread.sleep(isNewApi ? 800 : 500);
                                        for (int i = 0; i < chengJiuGet.size(); i++) {
                                            AutoTool.execShellCmd(chengJiuGet.get(i));
                                            Thread.sleep(800);
                                        }
                                        AutoTool.execShellCmd(close);
                                        Thread.sleep(1200);
                                        Util.getCapBitmapNew();
                                        count++;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (count == 1) break;
                AutoTool.execShellCmd(CmdData.swipeY(1600, 250));
                Thread.sleep(1200);
                count++;
            }
            AutoTool.execShellCmdClose();
            Thread.sleep(TaskUtil.isNewApi ? 800 : 600);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void upgrade() {
        HandlerUtil.async(new Runnable() {
            @Override
            public void run() {
                int count = SPUtils.getInt("count");
                PointModel pointModel = CmdData.get(Constant.UPGRADE);
                for (int i = 0; i < count; i++) {
                    AutoTool.execShellCmd(pointModel);
                    try {
                        LogUtils.logd("升级中" + i);
                        Thread.sleep(1200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void zhengJiu(TaskModel taskModel, UserInfo userInfo) {

        try {
            PointModel point1 = CmdData.get(ZHENG_JIU_POINT);
            AutoTool.execShellCmdClose();
            Thread.sleep(1200);
            AutoTool.execShellCmdClose();
            Thread.sleep(1200);
            for (int i = 0; i < 25; i++) {
                AutoTool.execShellCmd(point1);
                Thread.sleep(1000);
            }
            Thread.sleep(1200);
            AutoTool.execShellCmdClose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static int count = 0;

    public static void kuaFuJlNew(TaskModel taskModel, UserInfo userInfo) {
        if (Util.checkTime(userInfo, KEY_KUA_FU_JL, ACache.TIME_DAY * 3, true)) {
            return;
        }
        List<PointModel> shuYanModel = Util.getRenShuYanModel();
        try {
            Thread.sleep(800);
            Util.getCapBitmap();
            int ff = SPUtils.getInt("count");
            PointModel pointModel1 = shuYanModel.get(ff);
            if (!Util.checkColor(bitmap, pointModel1)) return;
            final PointModel jlJoin = CmdData.get(KUA_FU_LJ_JOIN);
            final PointModel jlGet = CmdData.get(KUA_FU_LJ_GET);
            final PointModel close = CmdData.get(TIME_LIMIT_REWARD1_CLOSE);
            AutoTool.execShellCmd(pointModel1);
            Thread.sleep(isNewApi ? 1200 : 800);
            resetFail();
            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (Util.checkColor(jlJoin)) {
                    AutoTool.execShellCmd(jlJoin);
                    Thread.sleep(isNewApi ? 2000 : 1600);
                } else if (Util.checkColor(jlGet)) {
                    AutoTool.execShellCmd(jlGet);
                    Thread.sleep(isNewApi ? 2000 : 1600);
                    AutoTool.execShellCmd(close);
                    Thread.sleep(1600);
                    AutoTool.execShellCmd(close);
                    Thread.sleep(isNewApi ? 1200 : 1000);
                    break;
                }
                if (check(failCount, 10)) break;
            }
            AutoTool.execShellCmdClose();
            Thread.sleep(isNewApi ? 1200 : 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void timeLimitRewardNew(TaskModel taskModel, UserInfo userInfo) {

        List<PointModel> shuYanModel = Util.getRenShuYanModel();
        try {
            sleep(1000);
            Util.getCapBitmapNew();
            int ff = SPUtils.getInt("etXsPosition");
            PointModel pointModel1 = shuYanModel.get(ff);
            if (!Util.checkColor(bitmap, pointModel1)) return;
            final PointModel pointModel = CmdData.get(TIME_LIMIT_REWARD1);
            final PointModel close = CmdData.get(TIME_LIMIT_REWARD1_CLOSE);
            final List<PointModel> getPoints = CmdData.getXianShi();
            count = 0;
            AutoTool.execShellCmd(pointModel1);
            Thread.sleep(800);
            while (true) {
                if (isDestory) return;
                ImageParse.getSyncData(new ImageParse.Call() {
                    @Override
                    public void call(List<Result.ItemsBean> result) {
                        if (result.size() == 0) return;
                        try {
                            for (Result.ItemsBean bean : result) {
                                if (bean.getItemstring().contains("进入活动") || "活动".equals(bean.getItemstring())) {
                                    while (true) {
                                        if (isDestory) return;
                                        pointModel.setY(bean.getItemcoord().getY());
                                        LogUtils.logd(pointModel.toString());
                                        boolean checkColor = Util.checkColor(bitmap, pointModel, 12, 80);
                                        if (checkColor) {
                                            AutoTool.execShellCmd(CmdData.clickInt(bean.getItemcoord().getX(), bean.getItemcoord().getY()));
                                            Thread.sleep(1000);
                                            for (PointModel getPoint : getPoints) {
                                                AutoTool.execShellCmd(getPoint);
                                                Thread.sleep(1000);
                                            }
                                            AutoTool.execShellCmd(close);
                                            Thread.sleep(800);
                                            Util.getCapBitmap();
                                        } else {
                                            break;
                                        }
                                    }

                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                if (count == 1) {
                    AutoTool.execShellCmd(close);
                    Thread.sleep(800);
                    return;
                }
                count++;
                AutoTool.execShellCmd(CmdData.swipeY(1000, 300));
                Thread.sleep(1200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNewApi;

    public static void email(TaskModel task, UserInfo userInfo) {
//        PointModel pointModel1 = shuYanModel.get(ff);
        AutoTool.execShellCmdChuFu();
        final PointModel emailRed = CmdData.get(EMAIL_RED);
        PointModel netPoint = CmdData.get(NET_CLOSE);
        PointModel close = CmdData.get(EMAIL_DIALOG_CLOSE);
        PointModel emailOk = CmdData.get(EMAIL_OK);
        List<PointModel> emails = CmdData.getEmail();
        try {
            Thread.sleep(isNewApi ? 1500 : 800);
            Util.getCapBitmapNew();
            while (true) {
                if (isDestory) return;
                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
                if (!Util.checkColor(emailRed)) break;
                AutoTool.execShellCmd(emailRed);
                Thread.sleep(isNewApi ? 1200 : 1000);
                Util.getCapBitmapNew();
                for (int i = 0; i < emails.size(); i++) {
                    PointModel email = emails.get(i);
                    if (!Util.checkColor(email)) {
                        AutoTool.execShellCmd(email);
                        Thread.sleep(1000);
                        AutoTool.execShellCmd(emailOk);
                        Thread.sleep(isNewApi ? 1000 : 800);
                        AutoTool.execShellCmd(close);
                        Thread.sleep(isNewApi ? 1000 : 800);
                    }
                }
                AutoTool.execShellCmd(close);
                Thread.sleep(isNewApi ? 1500 : 500);
                Util.getCapBitmapNew();
            }
            AutoTool.execShellCmdClose();
            Thread.sleep(isNewApi ? 1200 : 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sunFang(TaskModel task, UserInfo userInfo) {


    }

    public static PointModel netPoint;

    public static void suiJiZhaoHuan(TaskModel task, UserInfo userInfo) {
        if (Util.checkTime(userInfo, KEY_ZHAO_HUAN, ACache.TIME_HOUR * 2, true)) {
            return;
        }
        resetFail();
        netPoint = CmdData.get(NET_CLOSE);
        PointModel huangYan = CmdData.get(SHI_JI_ZHAO_HUAN);
        PointModel suiJI = CmdData.get(SHI_JI_ZHAO_HUAN_OK);
        PointModel suiJIZiSi = CmdData.get(SHI_JI_ZHAO_HUAN_ZI_SI);
        PointModel oneKeyZh = CmdData.get(SHI_JI_ONE_KEY_ZH);


        try {
            AutoTool.execShellCmd(huangYan);
            sleep(1600);

            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (checkExp(bitmap, netPoint, "当前网络异常")) continue;//检查网络环境

                if (Util.checkColor(bitmap, suiJI)) {
                    AutoTool.execShellCmdClose();
                    sleep(1200);
                    break;
                } else if (Util.checkColor(oneKeyZh)) {
                    AutoTool.execShellCmd(oneKeyZh);
                    sleep(1200);

                    AutoTool.execShellCmd(suiJI);
                    sleep(2400);
                    AutoTool.execShellCmdClose();
                    sleep(1000);
                    AutoTool.execShellCmdClose();
                    sleep(1000);
                    break;
                } else if (Util.checkColor(bitmap, suiJIZiSi)) {
                    AutoTool.execShellCmd(suiJIZiSi);
                    Thread.sleep(1500);
                } else {
                    AutoTool.execShellCmd(suiJI);
                    Thread.sleep(3000);
                    AutoTool.execShellCmd(suiJIZiSi);
                    Thread.sleep(600);
                }
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void lianMengGaoJian(TaskModel task, UserInfo userInfo) {
        try {
            if (Util.checkTime(userInfo, KEY_LIAN_MENG, ACache.TIME_DAY)) {
                return;
            }
            PointModel lianMeng = CmdData.get(LIAN_MENG);
            PointModel lianMengSearch = CmdData.get(LIAN_MENG_CHA_XUN);
            PointModel lianMengSearchInput = CmdData.get(LIAN_MENG_SEARCH_INPUT);
            PointModel lianMengSearchInputSearch = CmdData.get(LIAN_MENG_SEARCH_SEARCH);
            PointModel lianMengJoin = CmdData.get(LIAN_MENG_JOIN);
            PointModel lianMengManager = CmdData.get(LIAN_MENG_MANAGER);
            PointModel lianMengMeiRiJianShe = CmdData.get(LIAN_MENG_MEI_RI_JIAN_SHE);
            PointModel lianMengMeiRiJianSheGaoJian = CmdData.get(LIAN_MENG_MEI_RI_JIAN_SHE_GAO_JI);
            PointModel lianMengMeiRiJianSheGaoJianClose = CmdData.get(LIAN_MENG_MEI_RI_JIAN_SHE_CLOSE);
            PointModel lianMengMember = CmdData.get(LIAN_MENG_MEMBER);
            PointModel lianMengMemberExit = CmdData.get(LIAN_MENG_MEMBER_EXIT);
            PointModel lianMengMemberExitOk = CmdData.get(LIAN_MENG_MEMBER_EXIT_OK);
            PointModel netPoint = CmdData.get(NET_CLOSE);
            AutoTool.execShellCmdChuFu();
            Thread.sleep(BaseApplication.densityDpi == 480 ? 1200 : 600);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 100, 100));
            Thread.sleep(600);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 100, 100));
            Thread.sleep(1600);
            AutoTool.execShellCmd(lianMeng);
            Thread.sleep(1000);
            int count = 0;
            while (true) {
                if (isDestory) return;
                Util.getCapBitmap();
                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
                if (Util.checkColor(lianMengSearch)) {
                    AutoTool.execShellCmd(lianMengSearch);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengSearchInput);
                    Thread.sleep(200);
                    AutoTool.execShellCmd(CmdData.inputTextUserInfoName + "177212");
                    Thread.sleep(800);
                    AutoTool.execShellCmd(lianMengSearchInputSearch);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengJoin);
                    Thread.sleep(2000);
                    AutoTool.execShellCmd(lianMengMeiRiJianSheGaoJianClose);
                    Thread.sleep(1000);
                } else if (Util.checkColor(bitmap, lianMengManager)) {
                    count = 0;
                    AutoTool.execShellCmd(lianMengManager);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengMeiRiJianShe);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengMeiRiJianSheGaoJian);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(netPoint);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengMeiRiJianSheGaoJianClose);
                    Thread.sleep(1000);
                } else if (Util.checkColor(bitmap, lianMengMember)) {
                    AutoTool.execShellCmd(lianMengMember);
                    Thread.sleep(isNewApi ? 1400 : 1000);
                    AutoTool.execShellCmd(lianMengMemberExit);
                    Thread.sleep(isNewApi ? 2000 : 1200);
                    AutoTool.execShellCmd(lianMengMemberExitOk);
                    break;
                } else {
                    if (count == 3) {
                        return;
                    }
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void lianMengFuBen(TaskModel task, UserInfo userInfo) {
        try {
            if (Util.checkTime(userInfo, KEY_LIAN_MENG_FU_BEN, ACache.TIME_DAY)) {
                return;
            }
            PointModel lianMeng = CmdData.get(LIAN_MENG);
            PointModel lianMengSearch = CmdData.get(LIAN_MENG_CHA_XUN);
            PointModel lianMengSearchInput = CmdData.get(LIAN_MENG_SEARCH_INPUT);
            PointModel lianMengSearchInputSearch = CmdData.get(LIAN_MENG_SEARCH_SEARCH);
            PointModel lianMengJoin = CmdData.get(LIAN_MENG_JOIN);
            final PointModel lianMengManager = CmdData.get(LIAN_MENG_MANAGER);

            PointModel lianMengFuBen = CmdData.get(LIAN_MENG_FU_BEN);
            final PointModel lianMengFuBenKill = CmdData.get(LIAN_MENG_FU_BEN_KILL);
            final PointModel lianMengFuBenClose = CmdData.get(LIAN_MENG_FU_BEN_CLOSE);
            PointModel lianMengGongXian = CmdData.get(LIAN_MENG_GONG_XIAN);
            PointModel lianMengDuiHuanShuJi = CmdData.get(LIAN_MENG_DUI_HUAN);

            PointModel lianMengMember = CmdData.get(LIAN_MENG_MEMBER);
            PointModel lianMengMemberExit = CmdData.get(LIAN_MENG_MEMBER_EXIT);
            PointModel lianMengMemberExitOk = CmdData.get(LIAN_MENG_MEMBER_EXIT_OK);
            PointModel netPoint = CmdData.get(NET_CLOSE);
            AutoTool.execShellCmdClose();
            Thread.sleep(600);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 100, 100));
            Thread.sleep(600);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 100, 100));
            Thread.sleep(1600);
            AutoTool.execShellCmd(lianMeng);
            Thread.sleep(1000);
            int count = 0;
            while (true) {
                if (isDestory) return;
                Util.getCapBitmap();
                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
                if (Util.checkColor(lianMengSearch)) { //搜索入盟
                    AutoTool.execShellCmd(lianMengSearch);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengSearchInput);
                    Thread.sleep(200);
                    AutoTool.execShellCmd(CmdData.inputTextUserInfoName + "177212");
                    Thread.sleep(800);
                    AutoTool.execShellCmd(lianMengSearchInputSearch);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengJoin);
                    Thread.sleep(2000);
                    AutoTool.execShellCmd(lianMengFuBenKill);
                    Thread.sleep(1000);
                } else if (Util.checkColor(bitmap, lianMengManager)) { //联盟管理
                    count = 0;
                    AutoTool.execShellCmd(lianMengManager);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengFuBen);
                    Thread.sleep(1000);
                    Util.getCapBitmap();
                    ImageParse.getSyncData(new ImageParse.Call() {
                        @Override
                        public void call(List<Result.ItemsBean> result) {
                            if (result.size() == 0) {
                                isEnd = false;
                                return;
                            }
                            for (Result.ItemsBean bean : result) {
                                if (isDestory) {
                                    isEnd = true;
                                    return;
                                }
                                String str = bean.getItemstring();
                                if (str.length() < 5 && ("进入副本".equals(str) || str.contains("进入副本"))) {
                                    PointModel pointModel = new PointModel("", "");
                                    pointModel.setX(bean.getItemcoord().getX() + 20);
                                    pointModel.setY(bean.getItemcoord().getY());
                                    pointModel.setName("进入副本");
                                    AutoTool.execShellCmd(pointModel);
                                    try {
                                        Thread.sleep(1200);
                                        Util.getCapBitmap();
                                        if (Util.checkColor(lianMengFuBenKill)) {
                                            for (int i = 0; i < 5; i++) {
                                                AutoTool.execShellCmd(lianMengFuBenKill);
                                                Thread.sleep(2500);
                                                AutoTool.execShellCmd(lianMengFuBenKill);
                                                Thread.sleep(1000);
                                            }
                                            Thread.sleep(2000);
                                        }
                                        AutoTool.execShellCmdClose();
                                        isEnd = false;
                                        return;
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    Thread.sleep(800);
                    AutoTool.execShellCmd(lianMengFuBenClose);
                    Thread.sleep(800);

                } else if (Util.checkColor(bitmap, lianMengGongXian)) { //领取贡献
                    AutoTool.execShellCmd(lianMengGongXian);
                    Thread.sleep(600);
                    AutoTool.execShellCmd(lianMengDuiHuanShuJi);
                    Thread.sleep(600);
                    AutoTool.execShellCmd(lianMengDuiHuanShuJi);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengFuBenClose);
                    Thread.sleep(2200);
                    AutoTool.execShellCmd(lianMengMember);
                    Thread.sleep(1000);
                    AutoTool.execShellCmd(lianMengMemberExit);
                    Thread.sleep(1200);
                    AutoTool.execShellCmd(lianMengMemberExitOk);
                    break;
                } else {
                    if (count == 3) {
                        return;
                    }
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int failCount = 0;
    public static boolean needContinue;
    private static boolean hasTask;
    public static PointModel dialogClose3;

    public static void resetFail() {
        failCount = 0;
        needContinue = false;
    }

    public static void fiveTask(TaskModel task, UserInfo userInfo) {
        resetFail();
        netPoint = CmdData.get(NET_CLOSE);
        PointModel tabTask = CmdData.get(TAB_TASK);
        PointModel dialogClose = CmdData.get(TASK_DIALOG_CLOSE);
        PointModel dialogClose140 = CmdData.get(TASK_DIALOG_CLOSE_140);
        PointModel welFare = CmdData.get(TASK_RIGHT_START_GET);  //福利
        PointModel welFareGet = CmdData.get(TASK_RIGHT_END_GET);   //福利领取
        List<PointModel> topModels = Util.getRenWuTopModel();
        List<PointModel> rightModels = Util.getRenWuRightModel();

        try {
            Util.getCapBitmapNew();

            boolean hasWelFare = Util.checkColor(welFare);
            boolean hasTaskFare = Util.checkColor(tabTask);
            if (hasWelFare) {
                int spaceWidth = BaseApplication.getScreenWidth() / 10;
                AutoTool.execShellCmd(welFare);
                Thread.sleep(800);
                Bitmap bitmap2 = Util.getCapBitmapNew();
                if (Util.checkColor(bitmap2, welFareGet)) {
                    AutoTool.execShellCmd(welFareGet);
                } else {
                    for (int i = 1; i <= 6; i++) {
                        AutoTool.execShellCmd(CmdData.clickInt(spaceWidth * i, welFareGet.getY()));
                        Thread.sleep(400);
                    }
                }
                Thread.sleep(200);
                AutoTool.execShellCmdClose();
                Thread.sleep(isNewApi ? 1000 : 800);
            }
            if (!hasTaskFare) return;
            AutoTool.execShellCmd(tabTask);
            Thread.sleep(isNewApi ? 1500 : 1200);
            int count = 0;
            while (true) {
                if (isDestory) return;
                Util.getCapBitmap();
                if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
                for (int i = 0; i < rightModels.size(); i++) {
                    if (isDestory) return;
                    if (!Util.checkColor(bitmap, rightModels.get(i))) {
                        AutoTool.execShellCmd(rightModels.get(i));
                        Thread.sleep(1200);
                        if (i == rightModels.size() - 1) {
                            if (count == 2) {
                                hasTask = false;
                            } else {
                                hasTask = true;
                                AutoTool.execShellCmdClose();
                                Thread.sleep(1200);
                                AutoTool.execShellCmd(tabTask);
                                Thread.sleep(isNewApi ? 1500 : 400);
                                count++;
                            }
                            break;
                        }
                    } else {
                        hasTask = false;
                        break;
                    }
                }
                if (hasTask) {
                    hasTask = false;
                    continue;
                }

                break;
            }

            Thread.sleep(200);
            for (int i = 0; i < topModels.size(); i++) {
                AutoTool.execShellCmd(topModels.get(i));
                Thread.sleep(1200);
                if (i < topModels.size() - 1) {
                    AutoTool.execShellCmd(dialogClose);
                    Thread.sleep(800);
                }
            }

            AutoTool.execShellCmd(dialogClose140);
            Thread.sleep(600);
            AutoTool.execShellCmdClose();
            Thread.sleep(600);
            hasTask = false;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public static void fourTask1(TaskModel task, UserInfo userInfo) {//俸禄

        if (Util.checkTime(userInfo, KEY_WORK_FL, Util.getSaveTime(), true)) {
            return;
        }
        resetFail();
        PointModel huangGong = CmdData.get(HUANG_GONG);
        PointModel getFengLu = CmdData.get(HUANG_GONG_GET);
        PointModel wang = CmdData.get(HUANG_GONG_WANG);
        PointModel huangGongClose = CmdData.get(HUANG_GONG_CLOSE);
        try {

            AutoTool.execShellCmdChuFu();
            sleep(800);

            if (BaseApplication.getScreenWidth() == 720) {
                AutoTool.execShellCmd(CmdData.swipeRight(BaseApplication.getScreenWidth()));
                Thread.sleep(800);
            }
            AutoTool.execShellCmd(huangGong);
            sleep(800);

            AutoTool.execShellCmd(wang);
            sleep(800);

            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (checkExp(bitmap, netPoint, "当前网络异常")) continue;//检查网络环境
                if (Util.checkColor(bitmap, getFengLu)) {
                    AutoTool.execShellCmd(huangGongClose);
                    sleep(800);
                    if (BaseApplication.densityDpi == 480) {
                        AutoTool.execShellCmd(huangGongClose);
                        sleep(1200);
                        AutoTool.execShellCmdChuFu();
                        sleep(1000);
                    }
                    threeTask(task, userInfo);
                    break;
                } else {
                    AutoTool.execShellCmd(getFengLu);
                    sleep(400);
                }
                if (check(failCount, 6)) break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void threeTask(TaskModel task, UserInfo userInfo) {

        LogUtils.logd("threeTask" + task.getName());
//        if (Util.checkTime(userInfo, KEY_WORK_MB, Util.getSaveTime(), true)) {
//            return;
//        }
        resetFail();
        PointModel huangGongClose = CmdData.get(HUANG_GONG_CLOSE);
        PointModel paiHang = CmdData.get(PAI_HANG_BANG);
        PointModel moBai = CmdData.get(BANG_DAN_GET);
        PointModel bangDanSelf = CmdData.get(BANG_DAN_SELF);
        PointModel bangDanKuaFu = CmdData.get(BANG_DAN_CROSS);
        PointModel bandDanGuanKa = CmdData.get(BANG_DAN_GUAN_KA);
        PointModel bandDanQinMi = CmdData.get(BANG_DAN_QIN_MI);


        try {
            AutoTool.execShellCmdChuFu();
            sleep(1500);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 50, 50));
            Thread.sleep(1200);
            AutoTool.execShellCmd(CmdData.swipe(300, 600));
            sleep(1000);
            AutoTool.execShellCmd(paiHang);
            Thread.sleep(800);
            boolean isKuaFu = SPUtils.getBoolean(KEY_MO_BAI_KUA_FU);
            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (checkExp(bitmap, netPoint, "当前网络异常")) continue;//检查网络环境
                if (Util.checkColor(bitmap, bangDanSelf)) {
                    AutoTool.execShellCmd(bangDanSelf);
                    sleep(240);
                    break;
                } else {
                    sleep(240);
                }
                if (check(failCount, 20)) break;
            }
            int count = 0;
            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (checkExp(bitmap, netPoint, "当前网络异常")) continue;//检查网络环境
                if (Util.checkColor(moBai)) {
                    count++;
                    if (count == 1 || count == 6) {
                        AutoTool.execShellCmd(bandDanGuanKa);
                    } else if (count == 2) {
                        AutoTool.execShellCmd(bandDanQinMi);
                    } else if (count == 3) {
                        AutoTool.execShellCmd(huangGongClose);
                        sleep(1000);
                        if (isKuaFu) {
                            AutoTool.execShellCmd(bangDanKuaFu);
                            sleep(800);
                            count = 5;
                        } else break;
                    } else if (count == 7) {
                        AutoTool.execShellCmd(huangGongClose);
                        sleep(800);
                        break;
                    }
                    Thread.sleep(isNewApi ? 1600 : 1000);
                } else {
                    AutoTool.execShellCmd(moBai);
                    sleep(2500);
                    AutoTool.execShellCmd(moBai);
                    sleep(1000);
                }
            }
            if (isKuaFu) {
                Thread.sleep(600);
            } else {
                AutoTool.execShellCmdClose();
                sleep(1000);
                AutoTool.execShellCmdChuFu();
                sleep(600);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean check(int failCount, int maxCount) {
        boolean check = failCount > maxCount;
        needContinue = check;
        TaskUtil.failCount++;
        return check;
    }

//    private void fourTask(TaskModel task, UserInfo userInfo) {//俸禄
//        if ((System.currentTimeMillis() - userInfo.getLastRefreshFengLuTime()) < 0) {
//            return;
//        }
////        send(task.getName());
//        userInfo.setLastRefreshFengLuTime(System.currentTimeMillis() + task.getSpaceTime() * 1000 + 1000);
//        resetFail();
//        PointModel huangGong = CmdData.get(HUANG_GONG);
//        PointModel getFengLu = CmdData.get(HUANG_GONG_GET);
//        PointModel wang = CmdData.get(HUANG_GONG_WANG);
//        try {
//
//            AutoTool.execShellCmd(CmdData.screenClose);
//            Thread.sleep(800);
//
//            if (BaseApplication.getScreenWidth() == 720) {
//                AutoTool.execShellCmd(CmdData.swipeRight(BaseApplication.getScreenWidth()));
//                Thread.sleep(800);
//            }
//            AutoTool.execShellCmd(huangGong);
//            Thread.sleep(isNewApi ? 1200 : 800);
//
//            AutoTool.execShellCmd(wang);
//            Thread.sleep(800);
//
//            Bitmap bitmap;
//            while (true) {
//                if (isDestory) return;
//                bitmap = Util.getCapBitmapNew();
//                if (checkExp(bitmap, netPoint, "当前网络异常")) continue;//检查网络环境
//                if (Util.checkColor(bitmap, getFengLu)) {
////                    long time =
//                    AutoTool.execShellCmd(CmdData.screenClose);
//                    Thread.sleep(isNewApi ? 1000 : 800);
//                    AutoTool.execShellCmd(CmdData.screenClose);
//                    Thread.sleep(isNewApi ? 1000 : 800);
//                    AutoTool.execShellCmd(CmdData.screenClose); //回府
//                    Thread.sleep(isNewApi ? 1200 : 800);
//                    break;
//                } else {
//                    AutoTool.execShellCmd(getFengLu);
//                    Thread.sleep(240);
//                }
//                if (check(failCount, 6)) break;
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    private static int currentUserInfo = 0;

//    public static void register(TaskModel task, UserInfo userInfo) {
//        try {
//
//            List<UserInfo> userInfos = Util.getUserInfo();
//            currentUserInfo = 0;
//            PointModel loginClose = CmdData.get(LOGIN_DIALOG_CLOSE);
//            PointModel realyNameOk = CmdData.get(REALY_NAME_OK);
//            PointModel registerOk = CmdData.get(REGISTER_OK);
//            PointModel createUser = CmdData.get(CREATE_USER);
//            PointModel startGame = CmdData.get(START_GAME);
//            PointModel register = CmdData.get(REGISTER_CLICK);
//            PointModel registerOther = CmdData.get(REGISTER_OTHER);
//            netPoint = CmdData.get(NET_CLOSE);
//            dialogClose3 = CmdData.get(DIALOG_CLOSE3);        //道具对话框关闭按钮
//            resetFail();
//            while (!NetWorkUtils.isNetConnected(BaseApplication.getAppContext())) {//检查网络
//                if (isDestory) return;
//                if (check(failCount, 30)) {
////                    mHandler.sendEmptyMessageDelayed(RESET_TASK, 60000);
//                    return;
//                }
//                Thread.sleep(2000);
//            }
//            while (true) {
//                if (currentUserInfo == userInfos.size()) {
//                    LogUtils.logd("注册结束");
//                    isDestory = true;
//                    return;
//                }
////                SuUtil.kill();       //退出游戏
//                Thread.sleep(1000);
//                LaunchApp.launchapp(BaseApplication.getAppContext(), LaunchApp.JPZMG_PACKAGE_NAME);    //启动游戏
//                Thread.sleep(2400);
//
//                while (true) {   //检查准备输入账号的环境
//                    if (isDestory) return;
//                    Util.getCapBitmap();
//                    if (Util.checkColor(loginClose) || check(failCount, 8)) break;
//                    Thread.sleep(600);
//                }
//                resetFail();
//                AutoTool.execShellCmd(register);
//                Thread.sleep(1000);
//                SPUtils.setInt(CURRENT_USER_INFO, currentUserInfo);
//                if (needContinue) continue;
//                userInfo = userInfos.get(currentUserInfo % userInfos.size());
//                currentUserInfo++;
//                int length = 11;
//
//                AutoTool.execShellCmd(length + 1, 112);
//                Thread.sleep(500);
//                AutoTool.execShellCmd(CmdData.inputTextUserInfoName + userInfo.getName()); //输入账号
//                Thread.sleep(1000);
//                AutoTool.execShellCmd("input keyevent 20");
//                Thread.sleep(isNewApi ? 400 : 200);
//                AutoTool.execShellCmd(6, 112);
//                Thread.sleep(isNewApi ? 1000 : 500);
//                AutoTool.execShellCmd(CmdData.inputTextUserInfoName + userInfo.getPwd());
//                Thread.sleep(600);
//                AutoTool.execShellCmd(registerOk); //点击注册
//                Thread.sleep(800);
//                AutoTool.execShellCmd(realyNameOk); //确定
//                Thread.sleep(1200);
//                AutoTool.execShellCmd("input keyevent 20");
//                Thread.sleep(800);
//                AutoTool.execShellCmd("input keyevent 19");
//                Thread.sleep(800);
//                AutoTool.execShellCmd(CmdData.inputTextUserInfoName + "wuzuqing");
//                Thread.sleep(800);
//                AutoTool.execShellCmd("input keyevent 20");
//                Thread.sleep(200);
//                AutoTool.execShellCmd(CmdData.inputTextUserInfoName + "45092319900211537X");
//                Thread.sleep(800);
//                AutoTool.execShellCmd(registerOther);
//                Thread.sleep(800);
//
//                while (true) {
//                    if (isDestory) return;
//                    Util.getCapBitmap();
//                    if (Util.checkColor(bitmap, startGame)) {
//                        AutoTool.execShellCmd(startGame); //进入游戏
//                        Thread.sleep(800);
//                        break;
//                    }
//                    check(failCount, 5);
//                    if (needContinue) break;
//                }
//                if (needContinue) continue;
//                AutoTool.execShellCmd(createUser); //创建角色
//                Thread.sleep(2400);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public static void oneTaskOld1(TaskModel task) throws InterruptedException { //2
        List<PointModel> pointModels = task.getData();
        int count = 0;
        PointModel huaAn = CmdData.get(HUA_AN);
        AutoTool.execShellCmd(huaAn);                               //点击华安进入收菜界面
        Thread.sleep(1200);
        resetFail();
        Util.getCapBitmapNew();
        int maxCount = SPUtils.getInt("count");
        while (true) {
            if (isDestory || count == maxCount) break;

            if (checkExp(bitmap, netPoint, "当前网络异常")) continue;//检查网络环境

            if (isDestory) return;
            if (check(failCount, 4)) break;
            for (int i = 0; i < pointModels.size(); i++) {
                if (isDestory) return;
                PointModel pointModel = pointModels.get(i);
                AutoTool.execShellCmd(pointModel);
                Thread.sleep(isNewApi ? 600 : 500);
            }
            count++;
        }
        Thread.sleep(isNewApi ? 1200 : 1000);
        AutoTool.execShellCmdClose();
        Thread.sleep(isNewApi ? 1200 : 1000);
    }

    public static boolean checkPage(List<OrcModel> pageData, String currentPage) {
        return TextUtils.equals(currentPage, OrcConfig.pageName);
    }

    private static final String TAG = "TaskUtil";

    public static void oneTask(TaskModel task) throws InterruptedException { //2
        int count = 0;
        List<OrcModel> pageData;
        PointModel zhengShou = CmdData.get(ZHENG_SHOU);
        isEnd = false;
        resetFail();
        while (!isDestory) {
            pageData = Util.getBitmapAndPageData();

            if (checkExp(netPoint, "当前网络异常")) continue;//检查网络环境
            if (checkExp(dialogClose3, "关闭道具框")) continue;//检查网络环境
            if (isDestory) return;
            if (check(failCount, 20)) break;
            if (checkPage(pageData, "府内")) {
                AutoTool.execShellCmd(pageData.get(1).getRect());
                Thread.sleep(BaseApplication.densityDpi == 480 ? 1200 : 1200);
                continue;
            } else if (!checkPage(pageData, "经营资产")) {

                continue;
            } else if (checkPage(pageData, "道具使用")) {
                AutoTool.execShellCmd(pageData.get(1).getRect());
                Thread.sleep(600);
                continue;
            }
            count = 0;
//            if (Util.checkColor(zhengShou)) {
//                AutoTool.execShellCmd(zhengShou);
//                Thread.sleep(1200);
//                AutoTool.execShellCmdClose();
//                Thread.sleep(BaseApplication.densityDpi == 480 ? 1800 : 800);
//                return;
//            }
            for (OrcModel orcModel : pageData) {
                if (TextUtils.equals("经营", orcModel.getResult())) {
                    AutoTool.execShellCmd(orcModel.getRect());
                    count++;
                }
            }

            AutoTool.execShellCmd(zhengShou);
            Thread.sleep(300);


            isEnd = count == 0;
            if (isEnd) {
                AutoTool.execShellCmdClose();
                Thread.sleep(BaseApplication.densityDpi == 480 ? 1800 : 800);
                return;
            }
            LogUtils.logd(" count" + count);
        }
    }


    public static void recycle() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    private static boolean laoFangMax;

    public static void laoFang(TaskModel task, UserInfo userInfo) {
        if (Util.checkTime(userInfo, KEY_LAO_FANG, Util.getSaveTime(), false)) {
            return;
        }
        resetFail();
        PointModel lfPointModel = CmdData.get(LAO_FANG_MAP);
        PointModel peoplePointModel = CmdData.get(LAO_FANG_PEOPLE);
        int maxCount = 5;
        try {
            AutoTool.execShellCmdChuFu();
            Thread.sleep(isNewApi ? 1600 : 800);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 100, 100));
            Thread.sleep(600);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 100, 100));
            Thread.sleep(isNewApi ? 1200 : 800);
            AutoTool.execShellCmd(lfPointModel);
            sleep(1200);
            AutoTool.execShellCmd(lfPointModel);
            sleep(1200);
            Util.getCapBitmapNew();
            laoFangMax = false;
            ImageParse.getSyncData(new ImageParse.Call() {
                @Override
                public void call(List<Result.ItemsBean> result) {
                    if (result == null || result.size() == 0) return;
                    try {
                        for (Result.ItemsBean itemsBean : result) {
                            if (itemsBean.getItemstring().contains("键教育")) {
                                AutoTool.execShellCmdXy(itemsBean.getItemcoord().getX() + itemsBean.getItemcoord().getWidth() + 30, itemsBean.getItemcoord().getY() + 10);
                                Thread.sleep(200);
                            } else if (itemsBean.getItemstring().contains("当前名望")) {
                                String str = itemsBean.getItemstring().replace("当前名望:", "").trim();
                                String[] split = str.split("/");
                                if (split.length == 2) {
                                    if (Integer.parseInt(split[0]) > (Integer.parseInt(split[1]) / 2 + 120)) {
                                        laoFangMax = true;
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            if (laoFangMax) maxCount = 12;
            for (int i = 0; i < maxCount; i++) {
                AutoTool.execShellCmd(peoplePointModel);
                sleep(3500);
                AutoTool.execShellCmd(peoplePointModel);
                sleep(1200);
            }
            AutoTool.execShellCmdClose();
            Thread.sleep(isNewApi ? 1200 : 800);
            AutoTool.execShellCmdChuFu();
            Thread.sleep(isNewApi ? 1200 : 800);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void yaMen(TaskModel task, UserInfo userInfo) {

        final PointModel mkMap = CmdData.get(YA_MEN_MAP);
        final PointModel ymMk = CmdData.get(YA_MEN_MEN_KE);
        final PointModel ymZz = CmdData.get(YA_MEN_ZHUN_ZOU);
        PointModel ymLs = CmdData.get(YA_MEN_LIN_SHI);//临时属性
        PointModel ymLsClose = CmdData.get(YA_MEN_LIN_SHI_CLOSE);//临时属性关闭对话框
        PointModel ymChooseYing = CmdData.get(YA_MEN_CHOOSE_YING);
        PointModel ymChooseSkip = CmdData.get(YA_MEN_CHOOSE_SKIP);
        PointModel ymChooseYingLs = CmdData.get(YA_MEN_CHOOSE_YING_LIAN_SHENG);
        PointModel ymChooseYingLsOne = CmdData.get(YA_MEN_CHOOSE_YING_LIAN_SHENG_ONE);
        PointModel ymEnd = CmdData.get(YA_MEN_END);

//        if (Util.checkTime(userInfo, YA_MEN_MAP, ACache.TIME_HOUR, true)) {
//            return;
//        }
        resetFail();
        try {
            AutoTool.execShellCmdChuFu();
            sleep(800);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 100, 100));
            Thread.sleep(600);
            AutoTool.execShellCmd(CmdData.swipe(BaseApplication.getScreenWidth() - 100, 100));
            Thread.sleep(BaseApplication.densityDpi == 480 ? 3000 : isNewApi ? 1200 : 800);
            Util.getCapBitmapNew();
            if (Util.checkColor(mkMap)) {
                AutoTool.execShellCmd(mkMap);
            } else {
                AutoTool.execShellCmdClose();
                return;
            }

            Thread.sleep(BaseApplication.densityDpi == 480 ? 3000 : 1200);
            Util.getCapBitmapNew();
            laoFangMax = false;
            ImageParse.getSyncData(new ImageParse.Call() {
                @Override
                public void call(List<Result.ItemsBean> result) {
                    if (result == null || result.size() == 0) return;
                    try {
                        for (Result.ItemsBean itemsBean : result) {
                            if (itemsBean.getItemstring().contains("大人门下") || itemsBean.getItemstring().contains("前来参见")) {
                                AutoTool.execShellCmd(ymMk);
                                sleep(1500);
                                AutoTool.execShellCmd(ymZz);
                                sleep(1500);
                                return;
                            } else if (itemsBean.getItemstring().contains("门客缉捕中")) {
                                AutoTool.execShellCmd(ymMk);
                                Thread.sleep(1200);
                            } else if (itemsBean.getItemstring().contains("退堂休息中")) {
                                laoFangMax = true;
                                return;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (laoFangMax) {
                AutoTool.execShellCmdChuFu();
                sleep(1500);
                AutoTool.execShellCmdChuFu();
                sleep(1000);
                return;
            }
            netPoint = CmdData.get(Constant.NET_CLOSE);
            resetFail();
            Thread.sleep(1200);
            boolean isFirst = true;

            int count = 0;
            while (true) {
                if (isDestory) return;
                Util.getCapBitmapNew();
                if (checkExp(bitmap, netPoint, "")) {
                    Thread.sleep(200);
                    continue;
                }
                if (Util.checkColor(ymLs)) {//未购买临时属性提示对话框
                    AutoTool.execShellCmd(ymLs);
                    sleep(1000);
                } else if (Util.checkColor(ymLsClose)) {//临时属性对话框
                    AutoTool.execShellCmd(ymLsClose);
                    sleep(1000);
                } else if (Util.checkColor(ymEnd)) {
                    AutoTool.execShellCmd(ymEnd);
                    sleep(1000);
                    AutoTool.execShellCmdChuFu();
                    sleep(1500);
                    AutoTool.execShellCmdChuFu();
                    sleep(1000);
                    break;
                } else if (Util.checkColor(ymChooseYingLsOne)) {
                    AutoTool.execShellCmd(ymChooseYingLsOne);
                    sleep(2000);
                    AutoTool.execShellCmd(ymChooseYingLsOne);
                    sleep(1000);
                } else if (Util.checkColor(ymChooseYing)) {
                    AutoTool.execShellCmd(ymChooseSkip);
                    sleep(1200);
                } else if (Util.checkColor(ymChooseYingLs)) {
                    if (isDestory) return;
                    Util.getCapBitmapNew();
                    PointModel pointModel = chooseMenKe();
                    if (count > 32) {
                        AutoTool.execShellCmd(getFirstMenKe());
                    } else {
                        AutoTool.execShellCmd(pointModel);
                    }

                    TaskUtil.failCount = 0;
                    count++;

                    if (isFirst) {
                        sleep(800);
                        AutoTool.execShellCmd(ymLs);
                        sleep(1000);
                        isFirst = false;
                    }
                    sleep(1200);
                    AutoTool.execShellCmd(ymChooseSkip);
                    sleep(1200);
                    AutoTool.execShellCmd(ymChooseSkip);
                    sleep(1200);
                } else {
                    if (check(failCount, 8)) break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sleep(long time) throws InterruptedException {
        Thread.sleep(BaseApplication.densityDpi == 480 ? time + 800 : time);
    }

    private static String[] colors = {"#373636" //白
            , "#384D34"//绿
            , "#374A64"//蓝
            , "#5C3472"//紫
            , "#922F2F"//红
            , "#E5BA65"//黄
    };
    private static String[] colors1 = {"#363636" //白 00
            , "#456141"//绿 00
            , "#475C7B"//蓝 00 4B6182
            , "#8751A5"//紫 00  814C9E
            , "#BB3F3B"//红 00 8D381E  772E24  BF4441
            , "#EFBE5A"//黄  F0C570  B27105
    };

    private static PointModel getFirstMenKe() {
        PointModel pointModel = new PointModel("MEN_KEY", "门客1");
        int leftX = BaseApplication.densityDpi == 480 ? 318 : 308;
        pointModel.setY(364);
        pointModel.setX(leftX);
        return pointModel;
    }

    private static PointModel chooseMenKe() {

        int length = colors.length;
        int leftX = BaseApplication.densityDpi == 480 ? 318 : 308;
        int rightX = BaseApplication.densityDpi == 480 ? 960 : 1006;
        int midX = leftX + (rightX - leftX) / 2;
        int y = 364;

        int flag1 = 5;
        int flag2 = 5;
        int flag3 = 5;
        PointModel pointModel = new PointModel("MEN_KEY", "门客");
        pointModel.setY(y);

        pointModel.setX(leftX);
        flag1 = checkIndex(pointModel, 1);

        pointModel.setX(midX);
        flag2 = checkIndex(pointModel, 2);

        pointModel.setX(rightX);
        flag3 = checkIndex(pointModel, 3);

        int flag = flag3 < flag2 ? 3 : 2;
        flag = (flag == 3 ? flag3 : flag2) < flag1 ? flag : 1;
        LogUtils.logd("flag1:" + flag1 + " flag2:" + flag2 + " flag3:" + flag3 + "  flag:" + flag);
        if (flag == 1) {
            pointModel.setX(leftX);
        } else if (flag == 2) {
            pointModel.setX(midX);
        }
        return pointModel;
    }

    public static int checkIndex(PointModel pointModel, int index) {
        boolean isBig = BaseApplication.densityDpi == 480;
        int flag3 = 5;
        for (int i = 0; i < colors.length; i++) {
            pointModel.setName("门客" + index + " " + i);
            pointModel.setNormalColor(isBig ? colors1[i] : colors[i]);
            if (Util.checkColor(pointModel, isBig ? 7 : 5, isBig ? 2 : 1)) {
                flag3 = i;
                break;
            }
        }
        return flag3;
    }
}
