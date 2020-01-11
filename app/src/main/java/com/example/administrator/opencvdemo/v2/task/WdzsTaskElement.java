package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;


/**
 * 我的子嗣
 */
public class WdzsTaskElement extends AbsTaskElement {
    public WdzsTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() {
        return false;
    }
}
