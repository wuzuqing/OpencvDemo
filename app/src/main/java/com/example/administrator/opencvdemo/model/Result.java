package com.example.administrator.opencvdemo.model;

import java.util.List;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2018/1/17 19:03
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2018/1/17$
 * @updateDes ${TODO}
 */

public class Result {


    private String session_id;
    private float angle;
    private int errorcode;
    private String errormsg;
    private List<ItemsBean> items;

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {

        private String itemstring;
        private ItemcoordBean itemcoord;
//        private List<WordsBean> words;

        public String getItemstring() {
            return itemstring;
        }

        public void setItemstring(String itemstring) {
            this.itemstring = itemstring;
        }

        public ItemcoordBean getItemcoord() {
            return itemcoord;
        }

        public void setItemcoord(ItemcoordBean itemcoord) {
            this.itemcoord = itemcoord;
        }

        @Override
        public String toString() {
            return "ItemsBean{" + "itemstring='" + itemstring + '\'' + ", itemcoord=" + itemcoord + '}';
        }

        public static class ItemcoordBean {
            @Override
            public String toString() {
                return "ItemcoordBean{" + "x=" + x + ", width=" + width + ", y=" + y + ", height=" + height + '}';
            }

            /**
             * x : 25
             * width : 47
             * y : 20
             * height : 25
             */


            private int x;
            private int width;
            private int y;
            private int height;

            public int getX() {
                return x;
            }

            public void setX(int x) {
                this.x = x;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getY() {
                return y;
            }

            public void setY(int y) {
                this.y = y;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

        }

    }

    @Override
    public String toString() {
        return "Result{" + "session_id='" + session_id + '\'' + ", angle=" + angle + ", errorcode=" + errorcode + ", errormsg='" +
                errormsg + '\'' + ", items=" + items + '}';
    }
}
