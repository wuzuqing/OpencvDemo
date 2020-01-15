package com.example.administrator.opencvdemo.model;

/**
 * 作者：士元
 * 时间：2020/1/15 0015 14:34
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class MenKe {
    private String name;
    private long colorValue;
    private int score;

    public MenKe(String name, long colorValue, int score) {
        this.name = name;
        this.colorValue = colorValue;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getColorValue() {
        return colorValue;
    }

    public void setColorValue(long colorValue) {
        this.colorValue = colorValue;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
