package com.example.administrator.opencvdemo.model;

/**
 * 作者：士元
 * 时间：2020/1/15 0015 14:34
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class MenKe {
    private String name;
    private String  color;
    private int score;

    public MenKe(String name, String color, int score) {
        this.name = name;
        this.color = color;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
