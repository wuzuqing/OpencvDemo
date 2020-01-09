package com.example.administrator.opencvdemo.event;

import com.example.administrator.opencvdemo.notroot.EventHelper;

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
    public void input(String text) {
        EventHelper.inputUserInfo(text);
    }

    @Override
    public void screenshots() {

    }
}
