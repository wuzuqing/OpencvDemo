package com.example.administrator.opencvdemo.model;

import com.example.administrator.opencvdemo.util.http.HttpBaseModel;

public class PagePointsModel extends HttpBaseModel {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
