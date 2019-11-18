package com.example.administrator.opencvdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.model.UserInfo;
import com.example.administrator.opencvdemo.util.Constant;
import com.example.administrator.opencvdemo.util.ToastUitl;
import com.example.administrator.opencvdemo.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/23 19:11
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/23$
 * @updateDes 账号管理
 */

public class AccountManagerActivity extends NoAnimatorActivity implements Constant {
    private EditText etPex;
    private EditText etStart;
    private EditText etEnd;
    private EditText etPwd;
    private EditText etTotal;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_account_manager);
        etPex = findViewById(R.id.et_pex);
        etStart = findViewById(R.id.et_start);
        etEnd = findViewById(R.id.et_end);
        etPwd = findViewById(R.id.et_pwd);
        etTotal = findViewById(R.id.et_total);
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountManagerActivity.this.onClick();
            }
        });
    }

    private void onClick() {
        String start = etStart.getText().toString();
        String end = etEnd.getText().toString();
        String pex = etPex.getText().toString();
        String pwd = etPwd.getText().toString();
        String total = etTotal.getText().toString();
        if (!TextUtils.isEmpty(total)){
            Util.saveUserInfo(total);
            return;
        }
        if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end) || TextUtils.isEmpty(pex) || TextUtils.isEmpty(pwd)) {
            ToastUitl.showShort("请输入参数");
            return;
        }
        int startInt = Integer.valueOf(start);
        int endInt = Integer.valueOf(end);
        if (startInt > endInt) {
            ToastUitl.showShort("开始不能小于结束");
            return;
        }
        List<UserInfo> newUserInfos = new ArrayList<>();
        UserInfo userInfo;
        for (int i = startInt; i <= endInt; i++) {
            userInfo = new UserInfo(String.format("%s%d", pex, i), pwd);
            newUserInfos.add(userInfo);
        }
        Util.saveUserInfo(newUserInfos);
        saveParams(null);
    }


    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideSoftKeyboard(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void exit(View view) {
        Util.exit(this);
    }

    public void saveParams(View view) {
        hideSoftKeyboard(etPex);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0, 0);
    }

    public void help(View view) {
    }
}
