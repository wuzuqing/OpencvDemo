package com.example.module_orc.model;

import com.example.module_orc.OrcHelper;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 作者：士元
 * 时间：2019/9/28 0028 14:31
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class TitleItem {
    private Mat mat;
    private Point point;
    private String name;
    private String filePath;
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    private static final String TAG = "TitleItem";
    public Mat getMat() {
        if (mat == null && filePath!=null) {
            String realPath = OrcHelper.getInstance().rootDir.getAbsolutePath() + "/mid/" + filePath;
            try {
                mat = Imgcodecs.imread(realPath);
//                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2GRAY);
//                Imgproc.threshold(mat, mat, OrcConfig.thresh, 255, OrcConfig.threshType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mat;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public String toString() {
        return "TitleItem{" +
            "point=" + point +
            ", name='" + name + '\'' +
            ", filePath='" + filePath + '\'' +
            '}';
    }
}
