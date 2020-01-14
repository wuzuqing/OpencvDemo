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
        private String totalPoints;
        private String firstPoints;
        private String secondPoints;
        private String threePoints;
        private String fourPoints;

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getTotal_points() {
            return totalPoints;
        }

        public String getTotalPoints() {
            return totalPoints;
        }

        public void setTotalPoints(String totalPoints) {
            this.totalPoints = totalPoints;
        }

        public String getFirstPoints() {
            return firstPoints;
        }

        public void setFirstPoints(String firstPoints) {
            this.firstPoints = firstPoints;
        }

        public String getSecondPoints() {
            return secondPoints;
        }

        public void setSecondPoints(String secondPoints) {
            this.secondPoints = secondPoints;
        }

        public String getThreePoints() {
            return threePoints;
        }

        public void setThreePoints(String threePoints) {
            this.threePoints = threePoints;
        }

        public String getFourPoints() {
            return fourPoints;
        }

        public void setFourPoints(String fourPoints) {
            this.fourPoints = fourPoints;
        }
    }
}
