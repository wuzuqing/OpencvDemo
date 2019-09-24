package com.example.module_orc;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.opencv.imgproc.Imgproc;

public class OrcConfig {
    public static int thresh = 135;
    public static int threshType = Imgproc.THRESH_BINARY_INV;
    public static int width = 14;

    public static int baseIgnoreHeight = 14;
    public static int baseIgnoreX = 1;





    public interface Zican{

    }

























































































    public static int[] changeToWhiteColor = { // #F4E697  #DF320A
            0xF3E69A
            , 0xDF320A
    };


    public static Bitmap changeToColor(Bitmap mBitmap) {
//        Bitmap mBitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                //获得Bitmap 图片中每一个点的color颜色值
                //将需要填充的颜色值如果不是
                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
                //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000
                //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了
                int color = mBitmap.getPixel(j, i);
                //将颜色值存在一个数组中 方便后面修改
                if (inIng(color)) {
                    mBitmap.setPixel(j, i, Color.WHITE);  //将白色替换成透明色
                }

            }
        }
        return mBitmap;
    }

    private static boolean inIng(int color) {
        for (int c : changeToWhiteColor) {
            if (c == color) {
                return true;
            }
        }
        return false;
    }
}
