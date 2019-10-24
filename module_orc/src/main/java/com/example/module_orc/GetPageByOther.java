package com.example.module_orc;

import android.util.Log;

import org.opencv.core.Rect;

import java.util.List;

import static com.example.module_orc.ignore.DaojuIgnoreRect.daojuClose;
import static com.example.module_orc.ignore.DengluIgnoreRect.loginGame;
import static com.example.module_orc.ignore.DengluIgnoreRect.loginGame1;
import static com.example.module_orc.ignore.GameGonggaoIgnoreRect.gameNoice;
import static com.example.module_orc.ignore.HuanggongIgnoreRect.bottom;
import static com.example.module_orc.ignore.StartGameIgnoreRect.startGame;

class GetPageByOther {
    private static final String TAG = "GetPageByOther";

    public static String getPage(List<Rect> rects) {
    //  {298, 184, 59x37}

        Rect mainPage1 = new Rect(158, 58, 32, 20);
        Rect startGame1 = new Rect(121, 16, 119, 38);
        Rect mainPage2 = new Rect(102, 592, 78, 44);
        String page = "";
        int flag = 0;
        for (Rect rect : rects) {
            Log.e(TAG, "ignoreRect: " + rect.toString());
            if (flag == 1) {
                // 出府
                if (rect.width == 98 && rect.height == 150) {
                    page = "府内";
                    break;
                }
            }
            if (equals(startGame,rect)) {
                return "进入游戏";
            } else if (equals(gameNoice,rect)) {
                return "游戏公告";
            } else if (equals(loginGame,rect) || equals(loginGame1,rect)) {
                return "登录";
            } else if (equals(mainPage1,rect)) {
                flag = 1;
                page = "府外";
            } else if (equals(mainPage2,rect)) {
                return getMainPage(rects);
            } else if (equals(bottom,rect)) {
                return "皇宫";
            }else if (equals(daojuClose,rect)){
                return "道具使用";
            }else if (equals(startGame1,rect)){
                return "本服榜单";
            }

        }
        return page;
    }

    private static boolean equals(Rect startGame, Rect rect) {
        return startGame.x == rect.x && startGame.width == rect.width ;
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
