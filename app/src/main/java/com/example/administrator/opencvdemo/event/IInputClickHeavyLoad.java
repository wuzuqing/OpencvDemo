package com.example.administrator.opencvdemo.event;

import com.example.administrator.opencvdemo.model.PointModel;
import org.opencv.core.Rect;

/**
 * 作者：士元
 * 时间：2020/1/9 0009 10:47
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public interface IInputClickHeavyLoad {

    void click(int x, int y);

    void click(PointModel model);

    void click(Rect rect);

    void clickMid(Rect rect);
}
