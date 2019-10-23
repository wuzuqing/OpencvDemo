package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;

public class ShuyuanTaskElement extends AbsTaskElement {
    public ShuyuanTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() {
        return false;
    }
}
