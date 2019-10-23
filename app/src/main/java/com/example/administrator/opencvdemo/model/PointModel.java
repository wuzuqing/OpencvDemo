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
    @Override
    public String toString() {
        return "PointModel{" + "key='" + key + '\'' + ", name='" + name + '\'' + ", x=" + x + ", y=" + y + ", normalColor='" +
                normalColor + '\'' + '}';
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

}
