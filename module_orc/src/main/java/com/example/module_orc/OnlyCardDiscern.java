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
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OnlyCardDiscern implements Runnable {
    private Bitmap bitmap1;
    protected String langName;
    protected int thresh = 135;
    private long start;
    private IDiscernCallback callback;
    protected Size mSize = new Size(1080 / 3, 1920 / 3);
    private String page;
    private int halfWidth = 1080 / 4;

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
        if(bitmap1==null){
            return;
        }
//        bitmap1 = OrcConfig.changeToColor(bitmap1);
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
        Imgproc.threshold(dst, dst, OrcConfig.thresh, 255, OrcConfig.threshType);
//        //        //膨胀
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, new Size(OrcConfig.width, 1));
        Imgproc.erode(dst, dst, erodeElement);
//
//        //寻找符合坐标
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
//
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
            }
            else if (ignoreRect(rect)) {
                continue;
            }
            rects.add(rect);
            Imgproc.rectangle(src, rect, new Scalar(0, 255, 0), 1, 8, 0);
        }

        int newW = 0, newH = 0;
        if (callback != null) {
            try {
//                Rect rect = rects.get(0);
////                if (rect.x > 4 && rect.y > 8 && rect.x < halfWidth) {
////                    rect.set(new double[]{rect.x - 4, rect.y - 8, rect.width + 8, rect.height + 16});
////                }
//                dst = new Mat(threshold, rect);
//                Bitmap bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
//                Utils.matToBitmap(dst, bitmap);
//                String format = String.format("crop/full/%s",  page);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 70, new FileOutputStream(new File(Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), format)));
//                String text = OrcHelper.getInstance().orcText(bitmap, "zwp");
//                Log.d(TAG, "orcText: " + text);
            } catch (Exception e) {
                e.printStackTrace();
            }
            OrcModel orcModel = new OrcModel();
//                        Bitmap bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
//                        Utils.matToBitmap(dst, bitmap);
            Bitmap bitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(src, bitmap);
            orcModel.setBitmap(bitmap);
            newW = bitmap.getWidth();
            newH = bitmap.getHeight();
            callback.call(Collections.singletonList(orcModel));
        }
        Log.d(TAG, "discern: usedTime" + (System.currentTimeMillis() - start) + " newW:" + newW + " newH:" + newH);
    }

    private List<Rect> megerRect(List<MatOfPoint> contoursList) {
        List<Rect> list = new ArrayList<>();
        Set<Integer> ingoraSet = new HashSet<>();
        Rect currentPoint = null;
        Rect temp;
        int nextIndex = 0;
        int maxSize = contoursList.size();
        int left = 0, right = 0, top = 0, bottom = 0;
        for (int i = 0; i < maxSize; i++) {
            // 如果已经被合并,则跳过
            currentPoint = Imgproc.boundingRect(contoursList.get(i));
            if (ingoraSet.contains(i) || currentPoint.x == 0 || currentPoint.y == 0 || currentPoint.height < 8) {
                continue;
            }
            //如果是最后一个则结束
            if (i == maxSize - 1) {
                list.add(currentPoint);
                break;
            }
            nextIndex = i;
            left = currentPoint.x;
            top = currentPoint.y;
            right = left + currentPoint.width;
            bottom = top + currentPoint.height;
            while (true) {
                nextIndex++;
                if (nextIndex == maxSize) {
                    break;
                }
                temp = Imgproc.boundingRect(contoursList.get(nextIndex));
                if (bottom < temp.y) {
                    break;
                } else if (checkInCurrentPoint(left, top, right, bottom, temp)) {
                    // 两个矩形部分重叠
                    left = Math.min(left, temp.x);
                    // top = Math.min(top, temp.y);
                    right = Math.max(right, temp.x + temp.width);
                    // bottom = Math.max(bottom, temp.y + temp.height);
                    ingoraSet.add(nextIndex);
                } else {
                    break;
                }
            }
            list.add(currentPoint);
        }
        return list;
    }

    private boolean checkInCurrentPoint(int r1L, int r1T, int r1R, int r1B, Rect rect) {
        int r2L = rect.x, r2T = rect.y, r2R = r2L + rect.width, r2B = r2T + rect.height;
        return !(r1L > r2R || r1T > r2B || r2L > r1R || r2T > r1B);
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
                rect.x < OrcConfig.baseIgnoreX
//                || rect.y < 35
                        || rect.height < OrcConfig.baseIgnoreHeight
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
