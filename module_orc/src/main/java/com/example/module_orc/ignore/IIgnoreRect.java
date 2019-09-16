package com.example.module_orc.ignore;

import org.opencv.core.Rect;

/**
 * 作者：士元
 * 时间：2019/9/16 18:03
 * 邮箱：wuzuqing@linghit.com
 * 说明：排除无效区域
 */
public interface IIgnoreRect {

    /**
     * 排除无效区域
     */
    boolean ignoreRect(Rect rect);
}
