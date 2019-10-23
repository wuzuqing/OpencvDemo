package com.example.administrator.opencvdemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.util.Constant;


/**
 * @author 吴祖清
 * @version 1.0
 * @createDate 2017/12/24 12:42
 * @des ${TODO}
 * @updateAuthor #author
 * @updateDate 2017/12/24
 * @updateDes ${TODO}
 */

public abstract class NoAnimatorActivity extends AppCompatActivity implements Constant {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.bottom_dialog_enter,R.anim.bottom_dialog_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.bottom_dialog_enter,R.anim.bottom_dialog_exit);
    }
}
