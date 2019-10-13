package com.example.administrator.opencvdemo.util;


import android.util.Log;

import com.example.administrator.opencvdemo.BaseApplication;

import org.opencv.core.Rect;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Locale;

/**
 * 自动化工具类
 *
 * @author 詹子聪
 */
public class AutoTool {


    /**
     * 执行shell命令
     *
     * @param cmd
     */
    public static void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            boolean root = isRoot();

            Process process = Runtime.getRuntime().exec("sh");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd + "\n");
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
            LogUtils.logd("cmd:" + cmd +  "  root: "+ root);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 判断当前手机是否有ROOT权限
     * @return
     */
    public static boolean isRoots(){
        boolean bool = false;

        try{
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())){
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {

        }
        return bool;
    }

    private static boolean usedFloatRatio = false;

//     public static void execShellCmd(PointModel model) {
//         if (usedFloatRatio) {
// //            execShellCmd(CmdData.click(model.getFloatX(), model.getFloatY()));
//         } else {
//             execShellCmd(CmdData.clickInt(model.getX(), model.getY()));
//         }
//
//     }
//
//     public static void execShellCmdXy(int x, int y) {
//         if (usedFloatRatio) {
// //            execShellCmd(CmdData.click(model.getFloatX(), model.getFloatY()));
//         } else {
//             execShellCmd(CmdData.clickInt(x, y));
//         }
//
//     }
//
//     public static void execShellCmdClose() {
//         execShellCmd(CmdData.screenClose);
//     }
//     private static PointModel chuFUPointModel;
//     public static void execShellCmdChuFu() {
//         if (chuFUPointModel==null){
//             chuFUPointModel = CmdData.get("CHU_FU");
//         }
//         execShellCmd(chuFUPointModel);
//     }

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public static boolean isRoot() {
        boolean bool = false;

        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {

        }
        return bool;
    }

    public static boolean getRootAuth() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void execShellCmd(int length, int key) throws InterruptedException {
        for (int i = 0; i < length; i++) {
            Thread.sleep(200);
            AutoTool.execShellCmd("input keyevent " + key); //
        }
    }

    public static String clickFloat(Rect rect) {
        return clickFloat((rect.x+rect.width/2)*1f/360,(rect.y + rect.height/2)*1f/640);
    }

    public static String clickFloat(float x, float y) {
        return String.format(Locale.getDefault(), "input tap %d %d", getXRatio(x), getYRatio(y));
    }

    private static final String TAG = "AutoTool";
    public static int getXRatio(float ratio) {
        Log.d(TAG, "getXRatio: "+ratio + " "+BaseApplication.getScreenWidth());
        return (int) (BaseApplication.getScreenWidth() * ratio);
    }
    public static int getYRatio(float ratio) {
        Log.d(TAG, "getYRatio: "+ratio + " "+BaseApplication.getScreenHeight());
        return (int) (BaseApplication.getScreenHeight() * ratio);
    }
}
