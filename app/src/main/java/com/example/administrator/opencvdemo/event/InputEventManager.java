package com.example.administrator.opencvdemo.event;

import android.text.TextUtils;
import com.example.administrator.opencvdemo.model.PointModel;
import org.opencv.core.Rect;

/**
 * 作者：士元
 * 时间：2020/1/9 0009 9:47
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class InputEventManager implements IInputEvent,IInputClickHeavyLoad {

    private static InputEventManager instance = new InputEventManager();

    public static InputEventManager getInstance() {
        return instance;
    }

    private IInputEvent clickEvent;

    public void setClickEvent(IInputEvent clickEvent) {
        this.clickEvent = clickEvent;
    }

    @Override
    public void click(int x, int y) {
        if (clickEvent != null) {
            clickEvent.click(x, y);
        }
    }

    @Override
    public void keyBack() {
        if (clickEvent != null) {
            clickEvent.keyBack();
        }
    }

    @Override
    public void click(PointModel model) {
        if (model!=null){
            click(model.getX(),model.getY());
        }
    }

    @Override
    public void click(Rect rect) {
        if (rect!=null){
            click(rect.x , rect.y);
        }
    }

    @Override
    public void clickMid(Rect rect) {
        if (rect!=null){
            click(rect.x + rect.width / 2, rect.y + rect.height / 2);
        }
    }

    @Override
    public void swipe(int fromX, int fromY, int toX, int toY) {
        if (clickEvent != null) {
            clickEvent.swipe(fromX, fromY, toX, toY);
        }
    }

    @Override
    public void input(String text) {
        if (clickEvent != null && !TextUtils.isEmpty(text)) {
            clickEvent.input(text);
        }
    }

    @Override
    public void screenshots() {
        if (clickEvent!=null){
            clickEvent.screenshots();
        }
    }
}
