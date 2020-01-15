package com.example.administrator.opencvdemo.util;

import android.graphics.Bitmap;

import com.example.administrator.opencvdemo.model.MenKe;
import com.example.administrator.opencvdemo.model.PointModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：士元
 * 时间：2020/1/14 0014 9:56
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class MenKeFenShuHelper {
    private static MenKeFenShuHelper instance = new MenKeFenShuHelper();
    private PointModel leftPkModel;
    private PointModel midPkModel;
    private PointModel rightPkModel;

    public static MenKeFenShuHelper getInstance() {
        return instance;
    }

    private Map<String, Integer> mapValues;

    private MenKeFenShuHelper() {
        init();
    }

    private void init() {
        mapValues = new HashMap<>();
        mapValues.put("#4D2210", 99);
        mapValues.put("#481D0D", 99);
        mapValues.put("#363636", 0);
        mapValues.put("#344931", 1);
        mapValues.put("#34455D", 2);
        mapValues.put("#5D3573", 3);
        mapValues.put("#5E3574", 3);
        mapValues.put("#5E6575", 3);
        mapValues.put("#5E3675", 3);
        mapValues.put("#8F2E2E", 4);
        mapValues.put("#C1861C", 5);
        mapValues.put("#C0851C", 5);
        mapValues.put("#C2871C", 5);
        mapValues.put("#C1861B", 5);
        mapValues.put("#E48A13", 6);
        mapValues.put("#E48A19", 6);
        mapValues.put("#E48919", 6);
        mapValues.put("#E38818", 6);
        mapValues.put("#E58B1A", 6);
        mapValues.put("#8E2E2E", 4);
        mapValues.put("#BF841A", 5);
        mapValues.put("#E99F23", 6);
        leftPkModel = new PointModel("YA_MEN_PK_LEFT_MEN_KE", "左边的门客");
        midPkModel = new PointModel("YA_MEN_PK_LEFT_MEN_KE", "左边的门客");
        rightPkModel = new PointModel("YA_MEN_PK_LEFT_MEN_KE", "左边的门客");
        leftPkModel.setX(52 + 250);
        leftPkModel.setY(358);

        midPkModel.setX(403 + 250);
        midPkModel.setY(358);

        rightPkModel.setX(755 + 250);
        rightPkModel.setY(358);

        List<MenKe> menKeList = new ArrayList<>();
        menKeList.add(new MenKe("魏微", "#AC8978", 700));
        menKeList.add(new MenKe("魏微", "#AD8B7B", 700));
        menKeList.add(new MenKe("魏微", "#AC8777", 700));
        menKeList.add(new MenKe("魏微", "#CA977E", 500));
        menKeList.add(new MenKe("魏微", "#CB997F", 500));
        menKeList.add(new MenKe("魏微", "#C8957C", 500));

        menKeList.add(new MenKe("魏忠贤", "#AC8568", 1000));

        menKeList.add(new MenKe("高长恭", "#9C695A", 950));

        menKeList.add(new MenKe("二蛋", "#AB7461", 600));
        menKeList.add(new MenKe("二蛋", "#A8705D", 600));

        menKeList.add(new MenKe("韩信", "#B48775", 960));
        menKeList.add(new MenKe("韩信", "#B58977", 960));
        menKeList.add(new MenKe("韩信", "#B38573", 960));
        menKeList.add(new MenKe("韩信", "#C69E86", 960));
        menKeList.add(new MenKe("韩信", "#C7A28A", 960));
        menKeList.add(new MenKe("韩信", "#C7A088", 960));

        menKeList.add(new MenKe("樊梨花", "#95493E", 890));

        menKeList.add(new MenKe("花木兰", "#C4A090", 880));
        menKeList.add(new MenKe("花木兰", "#C29E8E", 880));
        menKeList.add(new MenKe("花木兰", "#C19C8C", 880));

        menKeList.add(new MenKe("穆桂英", "#D1B4A8", 850));
        menKeList.add(new MenKe("穆桂英", "#CEB2A7", 850));


        menKeList.add(new MenKe("秦良玉", "#D9B7AB", 810));
        menKeList.add(new MenKe("秦良玉", "#D8B6AA", 810));
        menKeList.add(new MenKe("秦良玉", "#DAB9AD", 810));
        menKeList.add(new MenKe("秦良玉", "#D0AFA5", 810));

        menKeList.add(new MenKe("梁红玉", "#CCB0A6", 800));
        menKeList.add(new MenKe("梁红玉", "#CEADA3", 800));
        menKeList.add(new MenKe("梁红玉", "#CEADA3", 800));

        menKeList.add(new MenKe("赵高", "#C69B83", 780));
        menKeList.add(new MenKe("赵高", "#C49981", 780));
        menKeList.add(new MenKe("赵高", "#C79D85", 780));

        menKeList.add(new MenKe("秦桧", "#957064", 770));
        menKeList.add(new MenKe("秦桧", "#9A7569", 770));
        menKeList.add(new MenKe("秦桧", "#987367", 770));

        menKeList.add(new MenKe("李莲英", "#B27C53", 750));
        menKeList.add(new MenKe("李莲英", "#B07951", 750));

        menKeMap = new HashMap<>();

        for (MenKe menKe : menKeList) {
            List<MenKe> menKes = menKeMap.get(menKe.getColor());
            if (menKes == null) {
                menKes = new ArrayList<>();
                menKeMap.put(menKe.getColor(), menKes);
            }
            menKes.add(menKe);
        }
    }

    private int getIntValue(String color) {
        Integer integer = mapValues.get(color);
        if (integer == null) {
            return 7;
        }
        return integer;
    }

    private int getPkIndex() {
        if (mapValues == null) {
            init();
        }
        //pk页面
        Util.getCapBitmapNew();
        // 35    386  727
        int leftX = 52, midX = 403, rightX = 755;
        // int leftX = 35+19, midX = 386+19, rightX = 737+19;
        int y = 328, width = 288, height = 335;
        Bitmap src = Util.getBitmap();
        if (src == null) {
            return 1;
        }
        int getColorY = y + 30;

        String color1 = color(src.getPixel(leftX + 250, getColorY));
        String color2 = color(src.getPixel(midX + 250, getColorY));
        String color3 = color(src.getPixel(rightX + 250, getColorY));

        int leftValue = getIntValue(color1);
        int midValue = getIntValue(color2);
        int rightValue = getIntValue(color3);

        int pkIndex = leftValue > midValue ? 1 : 0;
        pkIndex = pkIndex == 0 ? (leftValue > rightValue ? 2 : 0) : (midValue > rightValue ? 2 : 1);
        int pkValue = 0;
        boolean hasEqs = false;
        //对比门客
        LogUtils.logd("left color:" + color1 + " leftValue:" + leftValue);
        LogUtils.logd("mid color:" + color2 + " midValue:" + midValue);
        LogUtils.logd("right color:" + color3 + " rightValue:" + rightValue);

        long timeMillis = System.currentTimeMillis();
        int leftColorValue = getColorValue(src, leftX, y, timeMillis);
        int midColorValue = getColorValue(src, midX, y, timeMillis);
        int rightColorValue = getColorValue(src, rightX, y, timeMillis);

        LogUtils.logd(" leftColorValue:" + leftColorValue);
        LogUtils.logd(" midColorValue:" + midColorValue);
        LogUtils.logd(" rightColorValue:" + rightColorValue);
        if (pkIndex == 0) {
            pkValue = leftValue;
            hasEqs = leftValue == midValue || leftValue == rightValue;
        } else if (pkIndex == 1) {
            pkValue = midValue;
            hasEqs = midValue == rightValue;
        } else {
            pkValue = rightValue;
            hasEqs = leftValue == rightValue;
        }
        if (pkValue < 4) {
            return pkIndex;
        }
        if (!hasEqs) {
            return pkIndex;
        }
        if (pkValue == 99) {
            return -1;
        }

        // 根据得分计算要pk的门客
        if (leftValue==midValue&& midColorValue  ==rightValue){
            int minValue = Math.min(Math.min(leftColorValue,midColorValue),rightColorValue);
            if (minValue==leftValue){
                pkIndex=0;
            }else if (midValue==midColorValue){
                pkIndex=1;
            }else {
                pkIndex=2;
            }
        }else if (leftValue==midValue){
             pkIndex = leftColorValue>midColorValue?1:0;
        }else if (leftValue==rightValue){
            pkIndex = leftColorValue>rightColorValue?2:0;
        }else {
            pkIndex = midColorValue>rightColorValue?2:1;
        }
        return pkIndex;
    }

    private Map<String, List<MenKe>> menKeMap;

    private static int getColorValue(Bitmap src, int left, int top, long timeMillis) {

        // 288, height = 335;
        int tempW = 288, tempH = 315;
        String color = Util.getColor(src, left + tempW / 2, top + tempH / 2);
        LogUtils.logd("left:"+left + " color:"+color);
        List<MenKe> list = getInstance().menKeMap.get(color);
        if (list == null) {
            return -1;
        }
        int value = 0;
        for (MenKe menKe : list) {
            if (value == 0) {
                value = menKe.getScore();
            } else {
                if (value < menKe.getScore()) {
                    value = menKe.getScore();
                }
            }
        }
        // Bitmap bitmap1 = Bitmap.createBitmap(src, left, top, tempW, tempH);
        // // Bitmap bitmap1 = Bitmap.createBitmap(src, left+(288-tempW)/2, top-tempW-10+335, tempW, tempH);
        // try {
        //     // int width = bitmap1.getWidth();
        //     // int height = bitmap1.getHeight();
        //     // long totalValue = 0;
        //     // for (int i = 0; i < width; i++) {
        //     //     for (int j = 0; j < height; j++) {
        //     //         int pixel = bitmap1.getPixel(i, j);
        //     //         totalValue+=pixel;
        //     //     }
        //     // }
        //     // LogUtils.logd("left:"+left +" totalValue:"+totalValue);
        //    String cacheDir = FileUtil.getAppCacheDir();
        //     bitmap1.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(cacheDir, "/" + timeMillis + "_" + left + ".png")));
        // } catch (FileNotFoundException e) {
        //     e.printStackTrace();
        // }

        return value;
    }

    public PointModel getPkModel() {
        int index = getPkIndex();
        LogUtils.logd("pkIndex :" + index);
        if (index == 1) {
            return midPkModel;
        } else if (index == 2) {
            return rightPkModel;
        } else if (index == -1) {
            return null;
        }
        return leftPkModel;
    }

    private static String color(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
