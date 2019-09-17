package com.example.module_orc;

import android.graphics.Bitmap;
import android.os.Environment;
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
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OnlyCardDiscern implements Runnable {
    private Bitmap bitmap1;
    protected String langName;
    protected int thresh = 135;
    private long start;
    private IDiscernCallback callback;
    protected Size mSize = new Size(1080 / 2, 1920 / 2);
    private String page;

    public OnlyCardDiscern(Bitmap bitmap1, String langName, IDiscernCallback callback) {
        this.bitmap1 = bitmap1;
        this.langName = langName;
        this.callback = callback;
    }

    public OnlyCardDiscern(Bitmap bitmap1, String langName, String page, IDiscernCallback callback) {
        this.bitmap1 = bitmap1;
        this.langName = langName;
        this.callback = callback;
        this.page = page;
    }

    @Override
    public void run() {
        start = System.currentTimeMillis();
        Mat src = new Mat();
        Mat dst = new Mat();
        Mat hierarchy = new Mat();
        Mat threshold = new Mat();
        //bit to mat
        Utils.bitmapToMat(bitmap1, src);
        //归一化
        Imgproc.resize(src, src, mSize);
        //灰度化
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        //二值化
        Imgproc.threshold(dst, dst, thresh, 255, Imgproc.THRESH_BINARY_INV);
        threshold = dst.clone();
        //        //膨胀
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(14, 1));
        Imgproc.erode(dst, dst, erodeElement);
        //         erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 10));
        //        Imgproc.erode(dst, dst, erodeElement);

        //寻找符合坐标
        List<MatOfPoint> contoursList = new ArrayList<>();
        Imgproc.findContours(dst, contoursList, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
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
        String pageName = "1";
        IIgnoreRect ignoreRect = IgnoreRectHelper.getInstance().getIgnoreRect(pageName);

        for (int i = 0; i < contoursList.size(); i++) {
            Rect rect = Imgproc.boundingRect(contoursList.get(i));
            //排除无效区域
            if (ignoreRect != null) {
                if (ignoreRect.ignoreRect(rect)) {
                    continue;
                }
            } else if (ignoreRect(rect)) {
                continue;
            }
            rects.add(rect);
            Imgproc.rectangle(src, rect, new Scalar(0, 255, 0), 1, 8, 0);
        }

        int newW = 0, newH = 0;
        if (callback != null) {

            //            final List<OrcModel> orcModels = new ArrayList<>(rects.size());
            //            for (Rect rect : rects) {
            //                dst = new Mat(src, rect);
            //                Bitmap bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
            //                Utils.matToBitmap(dst, bitmap);
            //                try {
            //                    String format = String.format("crop/%dx%d_%d,%d.png", rect.width, rect.height, rect.x, rect.y);
            //                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(Environment.getExternalStorageDirectory(),format)));
            //                } catch (FileNotFoundException e) {
            //                    e.printStackTrace();
            //                }
            ////                OrcModel model = createOrdModel(rect, bitmap);
            ////                if(TextUtils.isEmpty(model.getResult())){
            ////                    continue;
            ////                }
            ////                orcModels.add(model);
            //            }
            //            callback.call(orcModels);
//            src = dst;
            try {
                Rect rect = rects.get(0);
                dst = new Mat(threshold, rect);
                Bitmap bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
                Utils.matToBitmap(dst, bitmap);
                String format = String.format("crop/%d,%d_%dx%d_%s", rect.x, rect.y ,rect.width, rect.height,page);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(new File(Environment
                                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),format)));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
            OrcModel orcModel = new OrcModel();
            //            Bitmap bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
            //            Utils.matToBitmap(dst, bitmap);
            Bitmap bitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(src, bitmap);
            orcModel.setBitmap(bitmap);
            newW = bitmap.getWidth();
            newH = bitmap.getHeight();
            callback.call(Collections.singletonList(orcModel));
        }
        Log.d(TAG, "discern: usedTime" + (System.currentTimeMillis() - start) + " newW:" + newW + " newH:" + newH);
    }

    protected OrcModel createOrdModel(Rect rect, Bitmap bitmap) {
        return new OrcModel(rect, OrcHelper.getInstance().orcText(bitmap, langName), bitmap);
    }

    private boolean ignoreRect(Rect rect) {
//        if (rect.height == 11 || rect.height == 22) {
//            Log.d(TAG, "ignoreRect: " + rect.toString());
//            return false;
//        }
        if (
            rect.x < 1
//                || rect.y < 35
                || rect.height < 17
//                || rect.height > 26
            // || (rect.height > rect.width)
        ) {
            return true;
        }
        Log.d(TAG, "ignoreRect: " + rect.toString());
        return false;
    }

    private static final String TAG = "OnlyCardDiscern";
}
