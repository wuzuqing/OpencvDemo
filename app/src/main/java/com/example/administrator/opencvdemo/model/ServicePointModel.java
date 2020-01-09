package com.example.administrator.opencvdemo.model;

import com.example.administrator.opencvdemo.util.http.HttpBaseModel;

public class ServicePointModel extends HttpBaseModel {
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private String flag;
        private String total_points;
        private String first_points;
        private String second_points;
        private String three_points;
        private String four_points;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getTotal_points() {
            return total_points;
        }

        public void setTotal_points(String total_points) {
            this.total_points = total_points;
        }

        public String getFirst_points() {
            return first_points;
        }

        public void setFirst_points(String first_points) {
            this.first_points = first_points;
        }

        public String getSecond_points() {
            return second_points;
        }

        public void setSecond_points(String second_points) {
            this.second_points = second_points;
        }

        public String getThree_points() {
            return three_points;
        }

        public void setThree_points(String three_points) {
            this.three_points = three_points;
        }

        public String getFour_points() {
            return four_points;
        }

        public void setFour_points(String four_points) {
            this.four_points = four_points;
        }
    }
}
