package com.example.administrator.opencvdemo.model;

import com.example.administrator.opencvdemo.util.http.HttpBaseModel;

import java.util.List;

public class TaskRecordModel   extends HttpBaseModel {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
        private int id;
        private String account;
        private String pwd;
        private String area;
        private String finish_data;
        private String finish_task;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getFinish_data() {
            return finish_data;
        }

        public void setFinish_data(String finish_data) {
            this.finish_data = finish_data;
        }

        public String getFinish_task() {
            return finish_task;
        }

        public void setFinish_task(String finish_task) {
            this.finish_task = finish_task;
        }
    }
}
