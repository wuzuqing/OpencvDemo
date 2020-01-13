package com.example.administrator.opencvdemo.event;

import java.util.Locale;
import com.example.administrator.opencvdemo.util.AutoTool;
import com.example.administrator.opencvdemo.util.LaunchManager;
import com.example.administrator.opencvdemo.util.PointManagerV2;

/**
 * 作者：士元
 * 时间：2020/1/9 0009 9:37
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class RootInputEventImpl implements IInputEvent {

    @Override
    public void click(int x, int y) {
        AutoTool.execShellCmd(clickInt(x, y));
    }

    @Override
    public void keyBack() {
        AutoTool.keyEvent(4);
    }

    @Override
    public void swipe(int fromX, int fromY, int toX, int toY) {
        AutoTool.execShellCmd(swipeCmd(fromX, fromY, toX, toY));
    }

    @Override
    public void input(String text,boolean isPwd) {
        String inputTextUserInfoName = "input text ";
        AutoTool.execShellCmd(inputTextUserInfoName + text);
    }

    @Override
    public void screenshots() {
        AutoTool.execShellCmd(PointManagerV2.screenCap);
    }

    @Override
    public void killApp() {
        AutoTool.execShellCmd("am force-stop " + LaunchManager.APP_PACKAGE_NAME + " \n");
    }

    private String clickInt(int x, int y) {
        return String.format(Locale.getDefault(), "input tap %d %d", x, y);
    }

    private String swipeCmd(int fx, int fy, int tx, int ty) {
        return String.format(Locale.getDefault(), "input swipe %d %d %d %d", fx, fy, tx, ty);
    }
}
