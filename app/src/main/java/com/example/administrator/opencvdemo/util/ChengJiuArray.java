package com.example.administrator.opencvdemo.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2018/1/19 14:05
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2018/1/19$
 * @updateDes ${TODO}
 */

public class ChengJiuArray {
    private static List<Item> chengJius = new ArrayList<>();
  public static void init()  {
        chengJius.add(new Item("登录", 0));
        chengJius.add(new Item("官品", 1));
        chengJius.add(new Item("势力", 0));
        chengJius.add(new Item("门客", 1));
        chengJius.add(new Item("关卡", 0));
        chengJius.add(new Item("经商", 1));
        chengJius.add(new Item("经农", 0));
        chengJius.add(new Item("征兵", 1));
        chengJius.add(new Item("政务", 0));
        chengJius.add(new Item("VIP", 1));
        chengJius.add(new Item("衙门", 0));
        chengJius.add(new Item("书院", 1));
        chengJius.add(new Item("牢房", 0));
        chengJius.add(new Item("红颜", 1));
        chengJius.add(new Item("传唤", 0));
        chengJius.add(new Item("寻访", 1));
        chengJius.add(new Item("子嗣", 0));
        chengJius.add(new Item("联姻", 1));
        chengJius.add(new Item("噶尔丹", 0));
        chengJius.add(new Item("联盟", 1));
        chengJius.add(new Item("讨伐", 0));
        chengJius.add(new Item("宴会", 1));
        chengJius.add(new Item("通", 0));//通宵
    }

    public static class Item {
        public Item() {
        }

        public Item(String name, int position) {
            this.name = name;
            this.position = position;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;
        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public String toString() {
            return "Item{" + "name='" + name + '\'' + ", position=" + position + '}';
        }
    }


    public static Item getItem(String reg) {
        for (Item item : chengJius) {

            if ( reg.length()<5 && ( item.getName().equals(reg) || reg.contains(item.getName()))) return item;
        }
        return null;
    }
}
