package com.example.administrator.opencvdemo.v2.task;


import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.TaskModel;
import com.example.administrator.opencvdemo.util.ACache;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.AbsTaskElement;
import com.example.administrator.opencvdemo.v2.TaskState;

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

    private boolean xbBuy,xbUse;

    public XiaoBangTaskElement(TaskModel taskModel) {
        super(taskModel);
        // x,y      ,w,h
        // 30,386  ,1028,  42
        // 26,558  ,1030,  44
    }

    @Override
    protected boolean doTaskBefore() {
        // 0-5
        row = 1;
        xbBuy = SPUtils.getBoolean(Constant.KEY_XB_BUY);
        xbUse = SPUtils.getBoolean(Constant.KEY_XB_USE);
        col = 5;
        return super.doTaskBefore();
    }

    private PointModel defaultPoint;

    private PointModel getPoint(int row, int col) {
        if (defaultPoint != null) {
            return defaultPoint;
        }
        defaultPoint = new PointModel("XB", "小榜");

        int y = row == 1 ? firstY : secondY;
        int x = col * oneWidth + oneWidth / 2 + 30;
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

            boolean xbGet = SPUtils.getBoolean(Constant.KEY_XB_GET);
            if (xbGet){
                PointModel lingQuRuKou = PointManagerV2.get(Constant.XIAO_BANG_JIANGLI_RUKOU);
                PointModel lingQu = PointManagerV2.get(Constant.XIAO_BANG_LING_QU_JIANG_LI);
                PointModel lianMengPaiMing = PointManagerV2.get(Constant.XIAO_BANG_LIANMENG_PAIMENG);
                click(lingQuRuKou);

                Util.sleep(600);
                if (Util.checkColor(lingQu)){
                    click(lingQu);
                    Util.sleep(600);
                    click(lianMengPaiMing);
                    Util.sleep(400);
                }

                click(lianMengPaiMing);
                Util.sleep(600);

                Util.getCapBitmapNew();
                if (Util.checkColor(lingQu)){
                    click(lingQu);
                    Util.sleep(600);
                    click(lianMengPaiMing);
                    Util.sleep(400);
                }

                click(huangGongClose);
                Util.sleep(600);
                click(huangGongClose);
                Util.sleep(600);
                return true;

            }
            //是否已购买
            boolean checkTime = checkTime(KEY_XB_BUY, ACache.getTodayEndTime());
            if (!checkTime) {
                click(shangPu);
                do {
                    for (int i = 0; i < 10; i++) {
                        if (!TaskState.isWorking){
                            return true;
                        }
                        Util.sleep(600);
                        click(liBao1);
                    }
                    Util.sleep(600);
                    if (!TaskState.isWorking){
                        return true;
                    }
                    Util.getCapBitmapNew();
                } while (checkExp(netPoint, "当前网络异常"));

                do {
                    for (int i = 0; i < 20; i++) {
                        if (!TaskState.isWorking){
                            return true;
                        }
                        Util.sleep(600);
                        click(liBao2);
                    }
                    if (!TaskState.isWorking){
                        return true;
                    }
                    Util.sleep(600);
                    Util.getCapBitmapNew();
                } while (checkExp(netPoint, "当前网络异常"));
                click(close);
                Util.sleep(600);
                Util.saveLastRefreshTime(KEY_WORK_FL, ACache.getTodayEndTime());
                //只是购买
                if (!xbUse) {
                    click(huangGongClose);
                    return true;
                }
            }

            PointModel jinRu = PointManagerV2.get(Constant.HUO_DONG_JIN_RU);
            PointModel propCount = PointManagerV2.get(Constant.HUO_DONG_PROP);
            PointModel shiYong = PointManagerV2.get(Constant.HUO_DONG_SHI_YONG);
            click(jinRu);

            Util.sleep(600);
            while (TaskState.isWorking) {
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
