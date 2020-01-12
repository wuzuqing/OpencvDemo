package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;


/**
 * 小榜
 */
public class XiaoBangTaskElement extends AbsTaskElement {
    private int row; //行
    private int col; //列
    private int width = 1028;
    private int oneWidth = width / 6;

    private int firstY = 386;
    private int secondY = 558;

    public XiaoBangTaskElement(TaskModel taskModel) {
        super(taskModel);
        // x,y      ,w,h
        // 30,386  ,1028,  42
        // 26,558  ,1030,  44
    }

    @Override
    protected boolean doTaskBefore() {
        row = SPUtils.getInt("etXsPosition", 5);
        col = 1;
        return super.doTaskBefore();
    }

    private PointModel defaultPoint;

    private PointModel getPoint(int row, int col) {
        if (defaultPoint != null) {
            return defaultPoint;
        }
        defaultPoint = new PointModel("XB", "小榜");

        int y = col == 1 ? firstY : secondY;
        int x = row * oneWidth + oneWidth / 2 + 30;
        defaultPoint.setX(x);
        defaultPoint.setY(y);
        return defaultPoint;
    }

    @Override
    protected boolean doTask() throws Exception {
        pageData = Util.getBitmapAndPageData();

        if (checkExp(netPoint, "当前网络异常")) return false;//检查网络环境

        if (checkPage("府内")) {

            getPoint(row, col);
            //第一排 第二排
            click(defaultPoint);
            Thread.sleep(800);

            PointModel shangPu = PointManagerV2.get(Constant.HUO_DONG_SHANG_PU);
            PointModel liBao1 = PointManagerV2.get(Constant.HUO_DONG_LI_BAO_1);
            PointModel liBao2 = PointManagerV2.get(Constant.HUO_DONG_LI_BAO_2);
            PointModel close = PointManagerV2.get(EMAIL_DIALOG_CLOSE);
            PointModel huangGongClose = PointManagerV2.get(HUANG_GONG_CLOSE);
            //是否已购买
            if (false) {
                click(shangPu);
                do {
                    for (int i = 0; i < 10; i++) {
                        Util.sleep(600);
                        click(liBao1);
                    }
                    Util.sleep(600);
                    Util.getCapBitmapNew();
                } while (checkExp(netPoint, "当前网络异常"));

                do {
                    for (int i = 0; i < 20; i++) {
                        Util.sleep(600);
                        click(liBao2);
                    }
                    Util.sleep(600);
                    Util.getCapBitmapNew();
                } while (checkExp(netPoint, "当前网络异常"));
                click(close);
                Util.sleep(600);
                //只是购买
                if (false) {
                    click(huangGongClose);
                    return true;
                }
            }

            PointModel jinRu = PointManagerV2.get(Constant.HUO_DONG_JIN_RU);
            PointModel propCount = PointManagerV2.get(Constant.HUO_DONG_PROP);
            PointModel shiYong = PointManagerV2.get(Constant.HUO_DONG_SHI_YONG);
            click(jinRu);

            Util.sleep(600);
            while (true) {
                Util.getCapBitmapNew();
                if (checkExp(netPoint, "当前网络异常")) {
                    continue;
                } else if (Util.checkColor(propCount)) {
                    click(huangGongClose);
                    break;
                }
                click(shiYong);
                Util.sleep(600);
            }
            Util.sleep(600);
            click(huangGongClose);
            return true;
        } else if (checkPage("府外")) {
            PointManagerV2.execShellCmdChuFuV2();
            Thread.sleep(800);
            return false;
        }
        return true;
    }
}
