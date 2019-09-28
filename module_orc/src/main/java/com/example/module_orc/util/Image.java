package com.example.module_orc.util;

import android.util.Log;

import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcHelper;

import com.example.module_orc.model.TitleItem;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.calcHist;
import static org.opencv.imgproc.Imgproc.compareHist;

/**
 * @Title: Image.java
 * @Package com.xu.opencv
 * @Description: TODO
 * @author: xuhyacinth
 * @date: 2019年7月13日 下午12:10:14
 * @version: V-1.0
 * @Copyright: 2019 xuhyacinth
 */
public class Image {

    public static TitleItem matchPic(Mat g_src) {
        int method = OrcConfig.method;
        Mat g_tem = null;
        final Map<String, TitleItem> mTitleItems = OrcConfig.mTitleItems;
        Set<String> keySet = mTitleItems.keySet();
        for (String key : keySet) {
            TitleItem item = mTitleItems.get(key);

            if (item == null) {
                continue;
            }
            g_tem = item.getMat();
            int result_cols = g_src.cols() - g_tem.cols() + 1;
            int result_rows = g_src.rows() - g_tem.rows() + 1;
            // 3 创建32位模板匹配结果Mat
            Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
            // 4 调用 模板匹配函数
            Imgproc.matchTemplate(g_src, g_tem, result, method);  // 归一化平方差匹配法
            // 5 归一化
            Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
            // 6 获取模板匹配结果
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
            // 7 绘制匹配到的结果
            double x, y;
            if (method == Imgproc.TM_SQDIFF_NORMED || method == Imgproc.TM_SQDIFF) {
                x = mmr.minLoc.x;
                y = mmr.minLoc.y;
            } else {
                x = mmr.maxLoc.x;
                y = mmr.maxLoc.y;
            }
            Log.d(TAG, "matchPic: "+mmr + " item.name"+item.getName());
            if (x == 65 && y == 2) {
                return item;
            }
        }
        return null;
    }

    public static Mat matchPic(Mat g_src, Mat g_tem, int method) {
        //        method = Imgproc.TM_CCOEFF;
        long start = System.currentTimeMillis();
        int result_cols = g_src.cols() - g_tem.cols() + 1;
        int result_rows = g_src.rows() - g_tem.rows() + 1;
        // 3 创建32位模板匹配结果Mat
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        // 4 调用 模板匹配函数
        Imgproc.matchTemplate(g_src, g_tem, result, method);  // 归一化平方差匹配法
        //        Imgproc.matchTemplate(g_src, g_tem, result, Imgproc.TM_CCOEFF_NORMED);  // 归一化相关系数匹配法
        //        Imgproc.matchTemplate(g_src, g_tem, result, Imgproc.TM_CCOEFF);  // 相关系数匹配法：1表示完美的匹配；-1表示最差的匹配。
        //        Imgproc.matchTemplate(g_src, g_tem, result, Imgproc.TM_CCORR);  // 相关匹配法
        //        Imgproc.matchTemplate(g_src, g_tem, result, Imgproc.TM_SQDIFF);  // 平方差匹配法：该方法采用平方差来进行匹配；最好的匹配值为0；匹配越差，匹配值越大
        //        Imgproc.matchTemplate(g_src, g_tem, result, Imgproc.TM_CCORR_NORMED);  // 归一化相关匹配法：
        // 5 归一化
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        // 6 获取模板匹配结果
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        // 7 绘制匹配到的结果
        double x, y;
        if (method == Imgproc.TM_SQDIFF_NORMED || method == Imgproc.TM_SQDIFF) {
            x = mmr.minLoc.x;
            y = mmr.minLoc.y;
        } else {
            x = mmr.maxLoc.x;
            y = mmr.maxLoc.y;
        }

        Imgproc.rectangle(g_src, new Point(x, y),
            new Point(x + g_tem.cols(), y + g_tem.rows()),
            new Scalar(0, 0, 255));
        Log.d(TAG, "templete: " + mmr);
        // 8 显示结果
        File imagePath = OrcHelper.getInstance().rootDir;
        System.out.println("end:" + (System.currentTimeMillis() - start));
        //        Imgcodecs.imwrite(imagePath + "/match" + x + ".png", templete);
        Imgcodecs.imwrite(imagePath + "/match.jpg", g_src);
        return g_src;
    }

    private static final String TAG = "Image";

    /**
     * OpenCV-4.1.0 模板匹配
     * <table border="1" cellpadding="8">
     * <tr><th>输入参数</th><th>参数解释</th></tr>
     * <tr><td align="left">TM_SQDIFF是平方差匹配、TM_SQDIFF_NORMED是标准平方差匹配</td><td>利用平方差来进行匹配,最好匹配为0.匹配越差,匹配值越大。</td></tr>
     * <tr><td align="left">TM_CCORR是相关性匹配、TM_CCORR_NORMED是标准相关性匹配</td><td>采用模板和图像间的乘法操作,数越大表示匹配程度较高, 0表示最坏的匹配效果。</td></tr>
     * <tr><td align="left">TM_CCOEFF是相关性系数匹配、TM_CCOEFF_NORMED是标准相关性系数匹配</td><td>将模版对其均值的相对值与图像对其均值的相关值进行匹配,1表示完美匹配,-1表示糟糕的匹配,0表示没有任何相关性(随机序列)。</td></tr>
     * <tr><td colspan="2">随着从简单的测量(平方差)到更复杂的测量(相关系数),我们可获得越来越准确的匹配(同时也意味着越来越大的计算代价)。</td></tr>
     * <tr><td colspan="2">相关性是越接近1越大越好，平方差是越小越好，所以TM_SQDIFF在使用时和其他的是有所区别的。</td></tr>
     * <tr><td colspan="2">模板匹配结果Mat要是32位的。</td></tr>
     * </table>
     *
     * @return: void
     * @date: 2019年5月7日12:16:55
     */
    public static Mat matchPic(String originalPath, String templatePath, int method) {
        // 1 获取待匹配图片
        Mat templete = Imgcodecs.imread(templatePath);
        // 2 获取匹配模板
        Mat demo = Imgcodecs.imread(originalPath);
        Log.d(TAG, "matchPic: " + originalPath + " templatePath:" + templatePath);
        if (demo.width() > 600) {
            Mat gray = new Mat();
            int xishu = 6;
            Imgproc.cvtColor(demo, gray, Imgproc.COLOR_BGRA2GRAY);
            Imgproc.resize(gray, gray, new Size(gray.width() / xishu, gray.height() / xishu));
            Rect rect = new Rect();
            rect.set(new double[] { 10, 10, 24, 24 });
            Mat crop = new Mat(gray, rect);

            Imgcodecs.imwrite(new File(OrcHelper.getInstance().rootDir, "/b.jpg").getAbsolutePath(), gray);
            Imgcodecs.imwrite(new File(OrcHelper.getInstance().rootDir, "/a.jpg").getAbsolutePath(), crop);
        }
        return matchPic(demo, templete, method);
    }
}
