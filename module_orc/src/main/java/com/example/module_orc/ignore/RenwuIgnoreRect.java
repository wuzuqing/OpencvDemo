package com.example.module_orc.ignore;

import org.opencv.core.Rect;

/**
 * 作者：士元
 * 时间：2019/9/16 18:07
 * 邮箱：wuzuqing@linghit.com
 * 说明：任务
 */
public class RenwuIgnoreRect implements IIgnoreRect {
    @Override
    public boolean ignoreRect(Rect rect) {
        return false;
    }
}
