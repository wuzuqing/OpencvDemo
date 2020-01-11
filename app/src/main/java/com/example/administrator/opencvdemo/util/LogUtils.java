package com.example.administrator.opencvdemo.util;

import android.util.Log;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/22 14:31
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/22$
 * @updateDes ${TODO}
 */

public class LogUtils {
    private static final String TAG = "LogUtils";
    private static boolean DEBUG = true;

    public static void logd(String str) {
        if (DEBUG) {
            str = str.trim();
            int index = 0;
            int maxLength = 3500;
            int strLength = str.trim().length();


            if (str.length() < maxLength) {
                Log.i(TAG, str.trim());
            } else {
                int temp;
                while (index < strLength) {
                    temp = index + maxLength;
                    Log.i(TAG, str.substring(index, temp > strLength ? strLength : temp));
                    index += maxLength;
                }
//                Log.d(TAG, str.substring(0, 3500));
//                Log.d(TAG, str.substring(3500, str.length()));
            }
//            else if (str.length()<10500){
//                Log.d(TAG, str.substring(0, 3500));
//                Log.d(TAG, str.substring(3500, 7000));
//                Log.d(TAG, str.substring(7000,  str.length()));
//            }else{
//                Log.d(TAG, str.substring(0, 3500));
//                Log.d(TAG, str.substring(3500, 7000));
//                Log.d(TAG, str.substring(7000, 10500));
//                Log.d(TAG, str.substring(10500, str.length()));
//            }
        }
    }

    public static void logdAndToast(String text) {
        if (DEBUG) {
            LogUtils.logd(text);
        }
    }
}
