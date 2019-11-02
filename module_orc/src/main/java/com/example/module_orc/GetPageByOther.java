package com.example.module_orc;

import org.opencv.core.Rect;

import java.util.List;

import static com.example.module_orc.ignore.DaojuIgnoreRect.daojuClose;
import static com.example.module_orc.ignore.DengluIgnoreRect.loginGame;
import static com.example.module_orc.ignore.DengluIgnoreRect.loginGame1;
import static com.example.module_orc.ignore.GameGonggaoIgnoreRect.gameNoice;
import static com.example.module_orc.ignore.HuanggongIgnoreRect.bottom;
import static com.example.module_orc.ignore.StartGameIgnoreRect.startGame;

class GetPageByOther {
    private static final String TAG = "LogUtils";

    public static String getPage(List<Rect> rects) {
        //  {298, 184, 59x37}

        Rect mainPage1 = new Rect(158, 58, 32, 20);
        Rect startGame1 = new Rect(121, 16, 119, 38);
        Rect mainPage2 = new Rect(102, 592, 78, 44);
        Rect fengLu = new Rect(145, 11, 69, 31);
        String page = "";
        int flag = 0;
        for (Rect rect : rects) {
//            Log.e(TAG, "ignoreRect: " + rect.toString());
            if (flag == 1) {
                // 出府
                if (fuNei(rect)) {
                    page = "府内";
                    break;
                }
            }
            if (equals(startGame, rect)) {
                return "进入游戏";
            } else if (equals(gameNoice, rect)) {
                return "游戏公告";
            } else if (equals(loginGame, rect) || equals(loginGame1, rect)) {
                return "登录";
            } else if (equals(mainPage1, rect)) {
                flag = 1;
                page = "府外";
            } else if (equals(mainPage2, rect)) {
                return getMainPage(rects);
            } else if (equals(bottom, rect)) {
                return "皇宫";
            } else if (equals(daojuClose, rect)) {
                return "道具使用";
            } else if (equals(startGame1, rect)) {
                return "本服榜单";
            }else if (equals(fengLu,rect)){
                return "皇宫俸禄";
            }
        }
        return page;
    }

    private static boolean equals(Rect startGame, Rect rect) {
        return startGame.x == rect.x && startGame.width == rect.width && checkY(startGame, rect);
    }

    private static boolean checkY(Rect startGame, Rect rect) {
        return Math.abs(startGame.y - rect.y) < 40;
    }

    private static String getMainPage(List<Rect> rects) {
        String page = "府外";
        for (Rect rect : rects) {
            if (fuNei(rect)) {
                page = "府内";
                break;
            }
        }
        return page;
    }

    private static boolean fuNei(Rect rect) {
        //       普通                                                                                   //华为mate8
        return (rect.width == 98 && Math.abs(rect.height-151)<4)|| (rect.width == 90 && rect.height == 158)||(rect.y == 315 && rect.width == 27);
    }
}
