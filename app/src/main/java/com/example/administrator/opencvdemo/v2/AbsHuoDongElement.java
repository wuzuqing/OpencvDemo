package com.example.administrator.opencvdemo.v2;

import com.example.administrator.opencvdemo.model.TaskModel;

/**
 * 作者：士元
 * 时间：2020/1/16 0016 12:12
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public abstract class AbsHuoDongElement extends AbsTaskElement {
    protected int row; //行
    protected int col; //列

    public AbsHuoDongElement(TaskModel taskModel) {
        super(taskModel);
    }
}
