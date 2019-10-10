package com.example.module_orc;

import android.util.Log;

import org.opencv.core.Rect;

import java.util.List;

import static com.example.module_orc.ignore.DengluIgnoreRect.loginGame;
import static com.example.module_orc.ignore.GameGonggaoIgnoreRect.gameNoice;
import static com.example.module_orc.ignore.StartGameIgnoreRect.startGame;

class GetPageByOther {
    private static final String TAG = "GetPageByOther";

    public static String getPage(List<Rect> rects) {

        Rect mainPage1 = new Rect(158, 58, 32, 20);
        Rect mainPage2 = new Rect(102, 592, 78, 44);
        String page = "";
        int flag = 0;
        for (Rect rect : rects) {
            if (flag == 1) {
                // 出府
                if (rect.width == 98 && rect.height == 150) {
                    page = "府内";
                    break;
                }
            }
            if (startGame.equals(rect)) {
                return "进入游戏";
            } else if (gameNoice.equals(rect)) {
                return "游戏公告";
            } else if (loginGame.equals(rect)) {
                return "登录";
            } else if (mainPage1.equals(rect)) {
                flag = 1;
                page = "府外";
            } else if (mainPage2.equals(rect)) {
                return getMainPage(rects);
            }
            Log.d(TAG, "ignoreRect: " + rect.toString());
        }
        return page;
    }

    private static String getMainPage(List<Rect> rects) {
        String page = "府外";
        for (Rect rect : rects) {
            if (rect.width == 98 && rect.height == 150) {
                page = "府内";
                break;
            }
        }
        return page;
    }
}
