package com.example.administrator.opencvdemo.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;
import android.util.LongSparseArray;
import android.util.SparseArray;
import com.example.administrator.opencvdemo.model.MenKe;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.youtu.FileUtil;

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
        mapValues.put("#363636", 0);
        mapValues.put("#344931", 1);
        mapValues.put("#34455D", 2);
        mapValues.put("#5D3573", 3);
        mapValues.put("#5E3574", 3);
        mapValues.put("#5E6575", 3);
        mapValues.put("#5E3675", 3);
        mapValues.put("#8F2E2E", 4);
        mapValues.put("#C1861C", 5);
        mapValues.put("#C2871C", 5);
        mapValues.put("#C1861B", 5);
        mapValues.put("#E48A13", 6);
        mapValues.put("#E48A19", 6);
        mapValues.put("#E48919", 6);
        mapValues.put("#E38818", 6);
        mapValues.put("#E58B1A", 6);
        leftPkModel = new PointModel("YA_MEN_PK_LEFT_MEN_KE", "左边的门客");
        midPkModel = new PointModel("YA_MEN_PK_LEFT_MEN_KE", "左边的门客");
        rightPkModel = new PointModel("YA_MEN_PK_LEFT_MEN_KE", "左边的门客");
        leftPkModel.setX(52 + 250);
        leftPkModel.setY(358);

        midPkModel.setX(403 + 250);
        midPkModel.setY(358);

        rightPkModel.setX(755 + 250);
        rightPkModel.setY(358);

        menKeMap = new LongSparseArray<>();

        menKeMap.put(-69845441944L,new MenKe("曹雪芹",-69845441944L,10)); // -69829723435
        menKeMap.put(-75070154256L,new MenKe("干将",-75070154256L,10));
        menKeMap.put(-85834120346L,new MenKe("林则徐",-85834120346L,10));

        menKeMap.put(-76976664483L,new MenKe("张雪岩",-76976664483L,10));
        menKeMap.put(-89233258131L,new MenKe("箫剑",-89233258131L,10)); // -89191547847
        menKeMap.put(-79042291862L,new MenKe("魏微",-79042291862L,10));

        menKeMap.put(-33287875214L,new MenKe("李白",-33287875214L,10));
        menKeMap.put(-77578457458L,new MenKe("郑板桥",-77578457458L,10)); // -65916666693
        menKeMap.put(-72618918673L,new MenKe("欧阳修",-72618918673L,10));

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
        int leftX = 52, midX = 403, rightX = 755;
        int y = 328, width = 288, height = 335;
        Bitmap src = Util.getBitmap();
        if (src==null){
            return 1;
        }
        int getColorY = y + 30;
        // Bitmap bitmap1 = Bitmap.createBitmap(src, leftX, y, width, height);
        // Bitmap bitmap2 = Bitmap.createBitmap(src, midX, y, width, height);
        // Bitmap bitmap3 = Bitmap.createBitmap(src, rightX, y, width, height);
        // String cacheDir = FileUtil.getAppCacheDir();
        // try {
        //     bitmap1.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(new File(cacheDir,"/1.png")));
        //     bitmap2.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(new File(cacheDir,"/2.png")));
        //     bitmap3.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(new File(cacheDir,"/3.png")));
        // } catch (FileNotFoundException e) {
        //     e.printStackTrace();
        // }
        //
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
        int leftColorValue = getColorValue(src, leftX, y,timeMillis);
        int midColorValue = getColorValue(src, midX, y,timeMillis);
        int rightColorValue = getColorValue(src, rightX, y,timeMillis);

        if (pkIndex == 0) {
            pkValue = leftValue;
            hasEqs = leftValue == midValue || leftValue == rightValue;
        } else if (pkIndex == 1) {
            pkValue = midValue;
            hasEqs = leftValue == midValue || midValue == rightValue;
        } else if (pkIndex == 2) {
            pkValue = rightValue;
            hasEqs = leftValue == rightValue || midValue == rightValue;
        }
        if (pkValue < 4) {
            return pkIndex;
        }
        if (!hasEqs) {
            return pkIndex;
        }
        if (pkValue==99){
            return -1;
        }



        // //
        // // 80x80
        //
        // Bitmap bitmap2 = Bitmap.createBitmap(src, midX, y, 80, 40);
        // Bitmap bitmap3 = Bitmap.createBitmap(src, rightX, y, 80, 40);

        return pkIndex;
    }

    private LongSparseArray<MenKe> menKeMap;
    private static int getColorValue(Bitmap src,int left,int top,  long timeMillis){

        // 288, height = 335;
        String cacheDir = FileUtil.getAppCacheDir();
        int tempW = 288-8,tempH=315;
        Bitmap bitmap1 = Bitmap.createBitmap(src, left+4, top, tempW, tempH);
        // Bitmap bitmap1 = Bitmap.createBitmap(src, left+(288-tempW)/2, top-tempW-10+335, tempW, tempH);
        try {
            // int width = bitmap1.getWidth();
            // int height = bitmap1.getHeight();
            // long totalValue = 0;
            // for (int i = 0; i < width; i++) {
            //     for (int j = 0; j < height; j++) {
            //         int pixel = bitmap1.getPixel(i, j);
            //         totalValue+=pixel;
            //     }
            // }
            // LogUtils.logd("left:"+left +" totalValue:"+totalValue);

            bitmap1.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(new File(cacheDir,"/"+timeMillis+"_"+left+".png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public PointModel getPkModel() {
        int index = getPkIndex();
        LogUtils.logd("pkIndex :" + index);
        if (index == 1) {
            return midPkModel;
        } else if (index == 2) {
            return rightPkModel;
        }else if (index==-1){
            return null;
        }
        return leftPkModel;
    }

    private static String color(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }
}
