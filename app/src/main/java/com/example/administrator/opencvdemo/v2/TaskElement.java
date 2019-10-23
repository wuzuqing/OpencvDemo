package com.example.administrator.opencvdemo.v2;

import android.os.Handler;

import com.example.administrator.opencvdemo.model.TaskModel;


public interface TaskElement extends Runnable {
    void setTaskModel(TaskModel taskModel);

    TaskModel getTaskModel();

    void bindHandler(Handler handler);

    void printWorkName();
}
