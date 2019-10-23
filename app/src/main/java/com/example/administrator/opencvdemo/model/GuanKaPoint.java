package com.example.administrator.opencvdemo.model;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2018/2/27 22:02
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2018/2/27$
 * @updateDes ${TODO}
 */

public class GuanKaPoint {

    private Long id;
    private int x;
    private int y;
    private String color;

    public GuanKaPoint(Long id, int x, int y, String color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public GuanKaPoint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "GuanKaPoint{" + "id=" + id + ", x=" + x + ", y=" + y + ", color='" + color + '\'' + '}';
    }
}
