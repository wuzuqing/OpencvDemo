package com.example.module_orc;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.example.module_orc.model.TitleItem;
import com.example.module_orc.util.GsonUtils;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.opencv.imgproc.Imgproc.TM_CCORR_NORMED;

public class OrcConfig {

    /**
     * 压缩图片大小
     */
    public static Size sScreenSize = new Size(180, 320);
    public static Size compressScreenSize = new Size(180, 320);
    public static Size defaultScreenSize = new Size(360, 640);
    public static Rect titleMidRect = new Rect(new double[]{62, 2, 54, 30});
    public static Rect titleMidRectSmall = new Rect(new double[]{72, 14, 40, 13});
    // 112, 13, 125, 50
    public static Rect titleMidRectCurrent = new Rect(new double[]{112, 13, 125, 50});
    public static Rect titleMidCropRect = new Rect(new double[]{0, 0, 180, 60});

//    public static Size compressScreenSize = new Size(540, 960);
//    public static Rect titleMidRect = new Rect(new double[]{180, 10, 180, 90});
//    public static Rect titleMidCropRect = new Rect(new double[]{0, 0, 540, 180});
//    public static Size compressScreenSize = new Size(270, 480);
//    public static Rect titleMidRect =new Rect(new double[]{78,2,110,45});
//    public static Rect titleMidCropRect =new Rect(new double[]{0,0,270,85});

    public static int thresh = 135;
    public static int threshType = Imgproc.THRESH_BINARY_INV;
    public static int width = 14;

    public static int baseIgnoreHeight = 12;
    public static int baseIgnoreX = 1;
    public static int topColorXishu = 1;
    public static int method = TM_CCORR_NORMED;

    private static int topColorX = 102;
    private static int topColorWidth = 826;
    private static int topColorY = 42;
    private static int topColorHeight = 107;

    public static Map<String, TitleItem> mTitleItems = new HashMap<>();



    public static void initFirst(){
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                init();
                initTitleItem();
            }
        });
    }

    public static void resetScreenSize(int width,int height){
        sScreenSize.width = width;
        sScreenSize.height = height;
        topColorXishu = (int) (width/defaultScreenSize.width);
    }

    public static String getSign(Mat mat) {
        int cols = mat.cols();
        int rows = mat.rows();
        if (cols == 0 || rows == 0) {
            return "";
        }
        boolean isSingle = mat.get(0, 0).length == 1;
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int value;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                double[] doubles = mat.get(j, i);
                if (doubles != null) {
                    if (isSingle) {
                        value = (int) doubles[0];
                        if (index == 0 && value == 0) {
                            sb.append(j).append(",").append(i).append(";");
                            index++;
                            sb.append(value);
                        } else if (index > 0) {
                            sb.append(value==0?0:1);
                        }
                        if (index > 80) {
                            break;
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private static void initTitleItem() {
        File rootDir = OrcHelper.getInstance().rootDir;
        File midFileDir = new File(rootDir, "/mid");
        Map<String,String> data = new HashMap<>();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            FileInputStream fileInputStream = new FileInputStream( new File(midFileDir, "data.txt"));
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            fileInputStream.close();
            bf.close();
            data = GsonUtils.toMap(stringBuilder.toString());
            Log.d(TAG, "initTitleItem: "+data.values().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (midFileDir.isDirectory()) {
            File[] files = midFileDir.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            TitleItem titleItem;
            for (File file : files) {
                String name = file.getName();
                if (file.isFile() && (name.endsWith(".jpg"))) {
                    String title = Dictionary.getTitle(name);
                    if (TextUtils.isEmpty(title)) {
                        continue;
                    }
                    titleItem = new TitleItem();
                    titleItem.setName(title);
                    titleItem.setFilePath(name);
                    titleItem.setSign(getSignByMap(data,name));
                    Dictionary.putSign(titleItem.getSign(),titleItem.getName());
                    titleItem.setPoint(titleMidRect.tl());
                    mTitleItems.put(name, titleItem);
                }
            }
        }
    }

    private static String getSignByMap(Map<String, String> data,String fileName) {
        Set<String> set = data.keySet();
        for (String key : set) {
            if (TextUtils.equals(fileName,data.get(key))){
                return key;
            }
        }
        return "";
    }

    public static int[] changeToWhiteColor;
    static int r, g, b, startX, maxX, startY, maxY, ir, ig, ib;

    public static void init() {
        changeToWhiteColor = new int[]{ // #F4E697  #DF320A
                Color.parseColor("#fff7f3ad")
                , Color.parseColor("#C2D1E1")
        };
        r = Color.red(changeToWhiteColor[0]);
        g = Color.green(changeToWhiteColor[0]);
        b = Color.blue(changeToWhiteColor[0]);
        ir = Color.red(changeToWhiteColor[1]);
        ig = Color.green(changeToWhiteColor[1]);
        ib = Color.blue(changeToWhiteColor[1]);
        maxX = (topColorX + topColorWidth) / topColorXishu;
        maxY = (topColorY + topColorHeight) / topColorXishu;
        startX = topColorX / topColorXishu;
        startY = topColorY / topColorXishu;
    }

    public static OrcModel append( Rect rect) {
        OrcModel orcModel = new OrcModel();
        orcModel.setRect(rect);
        return orcModel;
    }

    public static OrcModel append( Rect rect,int x, int y, int width, int height) {
        OrcModel orcModel = new OrcModel();
        orcModel.setRect(offset(rect, x, y, width, height));
        return orcModel;
    }
    private static Rect offset(Rect rect, int x, int y, int width, int height) {
        rect.x += x;
        rect.y += y;
        rect.width -= width;
        rect.height -= height;
        return rect;
    }





    public interface BangDan {
        PointF title = new PointF(0.32562444f, 0.03539823f);
        PointF moBai = new PointF(0.7113784f, 0.8308173f);
        PointF tab = new PointF(0.040703055f, 0.17178553f);
    }

    /**
     * 排行榜
     */
    public interface Paihangbang {
        PointF title = new PointF(0.32562444f, 0.03539823f);
        PointF benFu = new PointF(0.4625347f, 0.37948984f);
        PointF kuaFu = new PointF(0.46438482f, 0.669443f);
    }

    /**
     * 处理公务
     */
    public interface Chuligongwu {
        PointF title = new PointF(0.32562444f, 0.03539823f);
    }

    /**
     * 红颜知己
     */
    public interface Hongyanzhiji {
        PointF yijianchuanhuan = new PointF(0.049953748f, 0.95731384f);
        PointF suijichuanhuan = new PointF(0.7419057f, 0.9448204f);
    }

    private static final String TAG = "OrcConfig";

    public static Bitmap changeToColor(Bitmap bitmap) {
        Bitmap mBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        //循环获得bitmap所有像素点
        startX = 90;
        startY = 13;
        maxX = 112;
        maxY = 60;
        for (int i = startY; i < maxY; i++) {
            for (int j = startX; j < maxX; j++) {
//                //获得Bitmap 图片中每一个点的color颜色值
//                //将需要填充的颜色值如果不是
//                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
//                int color = mBitmap.getPixel(j, i);
//                //将颜色值存在一个数组中 方便后面修改
//                if (like(ir, ig, ib, color, 60)) {
//                    mBitmap.setPixel(j, i, Color.BLACK);  //替换成白色
//                } else if (like(r, g, b, color, 140)) {
//                    mBitmap.setPixel(j, i, Color.WHITE);  //替换成白色
//                }
                mBitmap.setPixel(j, i, Color.BLACK);  //替换成白色
            }
        }
        return mBitmap;
    }

    public static String getColorHtml(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }


    private static boolean like(int color1, int color2) {
        return like(Color.red(color1), Color.green(color1), Color.blue(color1), color2, 140);
    }

    private static boolean like(int r, int g, int b, int color2, int offset) {
        //通过HSV比较两个子RGB的色差
        //比较两个RGB的色差
        int absR = r - Color.red(color2);
        int absG = g - Color.green(color2);
        int absB = b - Color.blue(color2);
        return Math.sqrt(absR * absR + absG * absG + absB * absB) < offset;
    }

}