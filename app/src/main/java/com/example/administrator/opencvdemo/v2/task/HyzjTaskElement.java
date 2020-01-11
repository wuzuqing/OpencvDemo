package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;

/**
 * 红颜知己
 */
public class HyzjTaskElement extends AbsTaskElement {
    public HyzjTaskElement(TaskModel taskModel) {
        super(taskModel);
    }

    @Override
    protected boolean doTask() {
        return false;
    }
}
