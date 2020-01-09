package com.example.administrator.opencvdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.dialog.SelectPropDialog;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.util.PointManagerV2;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.JsonUtils;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.v2.TaskState;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/26 13:10
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/26$
 * @updateDes ${TODO}
 */

public class AssetsPointSettingActivity extends NoAnimatorActivity implements Constant {

    private TextView tvContent;
    private PointModel mPointModel;
    private SelectPropDialog mSelectPropDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_setting);
        Button save = (Button) findViewById(R.id.save);
        this.tvContent = (TextView) findViewById(R.id.tv_content);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PointManagerV2.saveCoordinate();
                TaskState.get().init(BaseApplication.getAppContext());
                finish();
            }
        });

        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectPropDialog == null) {
                    mSelectPropDialog = new SelectPropDialog(AssetsPointSettingActivity.this);
                    mSelectPropDialog.setCall(new SelectPropDialog.Call<PointModel>() {
                        @Override
                        public void call(PointModel objects) {
                            mPointModel = objects;
                            tvContent.setText(mPointModel.getName() + " \nx:" + mPointModel.getX() + " y:" + mPointModel.getY());
                            if (!TextUtils.isEmpty(mPointModel.getNormalColor())) {
                                tvContent.setTextColor(Color.parseColor(mPointModel.getNormalColor()));
                            }
                        }
                    });
                }
                mSelectPropDialog.setData(PointManagerV2.coordinateList);
                mSelectPropDialog.show();
            }
        });
    }



    private String color;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                HandlerUtil.async(new Runnable() {
                    @Override
                    public void run() {
                        final int x = (int) event.getRawX();
                        final int y = (int) event.getRawY();
                        Util.getCapBitmapNew();
                        color = Util.getColor( x, y);
                        LogUtils.logd("color:" + color + " x:" + x + " Y:" + y);
                        if (TextUtils.isEmpty(color)) return;
                        if (mPointModel!=null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mPointModel.setX(x);
                                    mPointModel.setY(y);
                                    tvContent.setTextColor(Color.parseColor(color));
                                    mPointModel.setNormalColor(color);
                                    tvContent.setText(mPointModel.getName() + " \nx:" + mPointModel.getX() + " y:" + mPointModel.getY());
                                }
                            });
                        }
                    }
                });
                break;
        }
        return super.onTouchEvent(event);
    }

}
