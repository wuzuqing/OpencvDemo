package com.example.module_orc;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * 基类，
 */
public abstract class AbsDiscern implements Runnable {

    private static final String TAG = "AbsDiscern";
    protected Size mSize = new Size(640, 400);
    protected int thresh = 165;

    private Bitmap bitmap1;
    protected String langName;
    private IDiscernCallback callback;
    private long start;

    public AbsDiscern(Bitmap bitmap1, String langName, IDiscernCallback callback) {
        this.bitmap1 = bitmap1;
        this.langName = langName;
        this.callback = callback;
        initParams();
    }

    protected  void initParams(){}


    @Override
    public void run() {
        start = System.currentTimeMillis();
        Mat src = new Mat();
        Mat dst = new Mat();
        Mat hierarchy = new Mat();
        //bit to mat
        Utils.bitmapToMat(bitmap1, src);
        //归一化
        Imgproc.resize(src, src, mSize);
        //灰度化
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGRA2GRAY);
        //二值化
        Imgproc.threshold(dst, dst, thresh, 255, Imgproc.THRESH_BINARY);
        //膨胀
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(25, 10));
        Imgproc.erode(dst, dst, erodeElement);

        //寻找符合坐标
        List<MatOfPoint> contoursList = new ArrayList<>();
        Imgproc.findContours(dst, contoursList, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
        //外包矩形区域
        List<Rect> rects = new ArrayList<>();
        for (int i = 0; i < contoursList.size(); i++) {
            Rect rect = Imgproc.boundingRect(contoursList.get(i));
            //排除无效区域
            if (ignoreRect(rect)) {
                continue;
            }
            rects.add(rect);
        }

        final List<OrcModel> orcModels = new ArrayList<>(rects.size());
        for (Rect rect : rects) {
            dst = new Mat(src, rect);
            Bitmap bitmap = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(dst, bitmap);
            OrcModel model = createOrdModel(rect, bitmap);
            orcModels.add(model);
        }

        src.release();
        dst.release();
        hierarchy.release();
        callback.call(orcModels);
        Log.d(TAG, "discern: usedTime" + (System.currentTimeMillis() - start));
    }

    protected OrcModel createOrdModel(Rect rect, Bitmap bitmap) {
        return new OrcModel(rect, OrcHelper.getInstance().orcText(bitmap, langName), bitmap);
    }

    /**
     * 忽略符合规则的区域
     *
     * @param rect 区域
     * @return true 忽略 false 不忽略
     */
    protected abstract boolean ignoreRect(Rect rect);
}
