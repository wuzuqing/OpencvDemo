package com.example.administrator.opencvdemo.event;

/**
 * 作者：士元
 * 时间：2020/1/9 0009 9:36
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public interface IInputEvent {
    void click(int x, int y);

    void keyBack();

    void swipe(int fromX, int fromY, int toX, int toY);

    void input(String text);

    void screenshots();
}
