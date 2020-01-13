package com.example.administrator.opencvdemo.event;

import com.example.administrator.opencvdemo.notroot.EventHelper;
import com.example.module_orc.OrcConfig;

/**
 * 作者：士元
 * 时间：2020/1/9 0009 9:38
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class SimpleV24InputEventImpl implements IInputEvent {
    @Override
    public void click(int x, int y) {
        EventHelper.click(x, y);
    }

    @Override
    public void keyBack() {
        EventHelper.keyBack();
    }

    @Override
    public void swipe(int fromX, int fromY, int toX, int toY) {
        EventHelper.swipe(fromX, fromY, toX, toY);
    }

    @Override
    public void input(String text,boolean isPwd) {
        EventHelper.inputText(text,isPwd);
    }

    @Override
    public void screenshots() {

    }

    @Override
    public void killApp() {
        try {
            InputEventManager.getInstance().keyBack();
            Thread.sleep(800);
            InputEventManager.getInstance().click(899, 1117 + OrcConfig.offsetHeight);
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
