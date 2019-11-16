package com.example.administrator.opencvdemo.model;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/25 15:16
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/25$
 * @updateDes ${TODO}
 */

public class PointModel {
    private String key;
    private String name;
    private int x;
    private int y;
    private String normalColor;
    private boolean isReset;
    private int subY;

    @Override
    public String toString() {
        return "PointModel{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", normalColor='" + normalColor + '\'' +
                ", subY=" + subY +
                ", subColor='" + subColor + '\'' +
                '}';
    }

    private String subColor;
    public boolean isReset() {
        return isReset;
    }

    public void setReset(boolean reset) {
        isReset = reset;
    }


    public PointModel(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(String normalColor) {
        this.normalColor = normalColor;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void reset(PointModel model) {
        if (model!=null && model!=this){
            this.x = model.x;
            this.y = model.y;
            this.key = model.key;
            this.name = model.name;
            this.normalColor = model.normalColor;
        }
    }

    public void setSubColor(String subColor) {
        this.subColor = subColor;
    }

    public int getSubY() {
        return subY;
    }

    public String getSubColor() {
        return subColor;
    }

    public void setSubY(int subY) {
        this.subY = subY;
    }
}
