package com.example.module_orc;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.module_orc.ignore.IIgnoreRect;
import com.example.module_orc.ignore.IgnoreRectHelper;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OnlyCardDiscern implements Runnable {
    private Bitmap bitmap1;
    protected String langName;
    protected String fileName;
    private long start;
    private IDiscernCallback callback;
    protected Size mSize = new Size(360, 640);
    public static final boolean DEBUG = false;
    public OnlyCardDiscern(Bitmap bitmap1, String langName, IDiscernCallback callback) {
        this.bitmap1 = bitmap1;
        this.langName = langName;
        this.callback = callback;
    }


    public OnlyCardDiscern(Bitmap bitmap1, String langName, String page, IDiscernCallback callback) {
        this.bitmap1 = bitmap1;
        this.langName = langName;
        this.callback = callback;
    }

    public OnlyCardDiscern(String fileName, String langName, String page, IDiscernCallback callback) {
        this.fileName = fileName;
        this.langName = langName;
        this.callback = callback;
    }
    private static final String TAG = "LogUtils";
    @Override
    public void run() {
        start = System.currentTimeMillis();
        Mat src = new Mat();
        Mat dst = new Mat();
        Mat threshold;
        // 初始化

        OrcConfig.offsetHeight = (bitmap1.getHeight() - 1920 -OrcHelper.getInstance().getNavigationBarHeight() ) / 2;
        if (  OrcConfig.offsetHeight <0){
            OrcConfig.offsetHeight = 0;
        }
        if (bitmap1.getHeight()>1920) {
            Log.d(TAG, "run: " + bitmap1.getHeight() + " offsetHeight:" + OrcConfig.offsetHeight);
            bitmap1 = Bitmap.createBitmap(bitmap1,0, OrcConfig.offsetHeight,1080,1920);
        }
        Utils.bitmapToMat(bitmap1, src);
        //归一化
        if (bitmap1.getWidth() > mSize.width) {
            Imgproc.resize(src, src, mSize);
        }
        //灰度化
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        //二值化
        Imgproc.threshold(dst, dst, OrcConfig.thresh, 255, OrcConfig.threshType);
        threshold = dst.clone();
       Mat   threshold1 = dst.clone();
        //      //膨胀
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MARKER_CROSS, new Size(OrcConfig.width, 1));
        Imgproc.erode(dst, dst, erodeElement);
        //       //寻找符合坐标
        List<MatOfPoint> contoursList = new ArrayList<>();
        Imgproc.findContours(dst, contoursList, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        //外包矩形区域
        Collections.sort(contoursList, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint o1, MatOfPoint o2) {
                Rect rect1 = Imgproc.boundingRect(o1);
                Rect rect2 = Imgproc.boundingRect(o2);
                return rect1.y - rect2.y;
            }
        });
        List<Rect> rects = new ArrayList<>();
        for (int i = 0; i < contoursList.size(); i++) {
            Rect rect = Imgproc.boundingRect(contoursList.get(i));
            //排除无效区域
            if (ignoreRect(rect)) {
                continue;
            }
            rects.add(rect);
            if (DEBUG) {
                Imgproc.rectangle(src, rect, new Scalar(0, 255, 0), 1, 8, 0);
            }
        }
        String pageName = "1";
        Bitmap bitmap = null;
        Rect rect = null;
        try {
            int index = 0;
            while (true) {
                rect = rects.get(index);
                if (rect.x > 105 && rect.x < 180) {
                    break;
                }
                if (index == rects.size() - 1) {
                    break;
                }
                index++;
            }

            dst = new Mat(threshold, rect);
            bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(dst, bitmap);
            pageName = OrcHelper.getInstance().orcText(bitmap, "zwp");
            pageName = parseName(pageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IIgnoreRect ignoreRect = IgnoreRectHelper.getInstance().getIgnoreRect(pageName);
//        if (ignoreRect == null) {
//            Mat resize = new Mat();
//            Imgproc.resize(threshold, resize, OrcConfig.compressScreenSize);
//            Mat crop = new Mat(resize, OrcConfig.titleMidRect);
//            String sign = OrcConfig.getSign(crop);
//            pageName = Dictionary.getSignTitle(sign);
//            ignoreRect = IgnoreRectHelper.getInstance().getIgnoreRect(pageName);
//        }
        if (ignoreRect == null) {
            pageName = GetPageByOther.getPage(rects);
            ignoreRect = IgnoreRectHelper.getInstance().getIgnoreRect(pageName);
        }
//        Log.e(TAG, "run: pageName:" + pageName + " ignoreRect:" + ignoreRect);
        Mat result = src;
        OrcConfig.pageName = pageName;
        List<OrcModel> orcModels = new ArrayList<>();
        if (ignoreRect != null) {
            orcModels = ignoreRect.ignoreRect(rects);
            if (DEBUG) {
                Imgcodecs.imwrite(OrcHelper.getInstance().getTargetFile("/some/threshold.jpg").getAbsolutePath(), threshold);
            }
            for (OrcModel model : orcModels) {
                try {
                    dst = new Mat(threshold, model.getRect());
                    if (DEBUG) {
                        Imgproc.rectangle(src, model.getRect(), new Scalar(0, 0, 255), 1, 8, 0);
                    }
                    Imgcodecs.imwrite(OrcHelper.getInstance().getTargetFile("/some/" + model.getRect().toString() + ".jpg").getAbsolutePath(), dst);
                    bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(dst, bitmap);
                    model.setBitmap(bitmap);
                    String value = OrcHelper.getInstance().orcText(bitmap, "small");
                    Rect modelRect = model.getRect().clone();
                    modelRect.x *=3;
                    modelRect.y *=3;
                    modelRect.y += OrcConfig.offsetHeight;
                    modelRect.width *=3;
                    modelRect.height *=3;
                    model.setRect(modelRect);
                    model.setResult(value);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        if (callback != null) {
            if (DEBUG) {
                OrcModel orcModel = new OrcModel();
                bitmap = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(result, bitmap);
                orcModel.setBitmap(bitmap);
                orcModel.setResult(pageName);
                orcModels.add(0, orcModel);
            }
            callback.call(orcModels);
        }
        Log.i("LogUtils", "discern: usedTime" + (System.currentTimeMillis() - start) +" pageName:"+pageName + " \nvalue:" +orcModels.toString());
    }

    private String parseName(String pageName) {
        switch (pageName) {
            case "红颜知已商":
                return "红颜知已";
            case "通红商":
                return "通商";
            case "我的子嗣商":
                return "我的子嗣";
            case "单排行榜":
                return "排行榜";
            case "单子就":
            case "单知":
                return "皇宫俸禄";
            case "的颜":
                return "内阁";
            case "榜单服榜单":
                return "本服榜单";
        }
        return pageName;
    }

    private boolean ignoreRect(Rect rect) {
        if (
                rect.x < OrcConfig.baseIgnoreX
                        || rect.height < OrcConfig.baseIgnoreHeight
                ) {
            return true;
        }
        return false;
    }

}
