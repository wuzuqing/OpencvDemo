package com.example.administrator.opencvdemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.opencvdemo.config.BaseConfig;
import com.example.administrator.opencvdemo.floatservice.RequestPermissionsActivity;
import com.example.administrator.opencvdemo.util.ToastUitl;
import com.example.module_orc.IDiscernCallback;
import com.example.module_orc.OpenCVHelper;
import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;
import com.xiaosu.lib.permission.OnRequestPermissionsCallBack;
import com.xiaosu.lib.permission.PermissionCompat;

import java.io.File;
import java.util.List;

import static com.example.module_orc.WorkMode.ONLY_BITMAP;

public class MainActivity extends AppCompatActivity {

    private ImageView img, ivCrop;
    private Button btn;
    private int[] resIds = { R.mipmap.chufu, R.mipmap.chuligongwu, R.mipmap.zisi, R.mipmap.zican, R.mipmap.hongyanzhiji, R.mipmap.wzq1 };
    private String[] resNames = { "main", "clgw", "wdzs", "jyzc", "hyzj", "sfz" };

    private int currentIndex = 0;
    private TextView vTvResult;
    private EditText vEtLangName;
    private EditText vEtThresh;
    private EditText vEtThreshType;
    private EditText vEtWidth;

    private RecyclerView vRecyclerView;
    private TestAdapter vTestAdapter;
    OrcModel orcModel;
    List<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btn = (Button) findViewById(R.id.btn);
        this.img = (ImageView) findViewById(R.id.img);
        this.vTvResult = findViewById(R.id.resultText);
        this.ivCrop = findViewById(R.id.img_crop);
        this.vEtLangName = findViewById(R.id.lengName);
        this.vEtThresh = findViewById(R.id.et_thresh);
        this.vEtThreshType = findViewById(R.id.et_type);
        this.vEtWidth = findViewById(R.id.et_width);
        this.vRecyclerView = findViewById(R.id.rcv);
        findViewById(R.id.compressBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.showFloatView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getSelf(), RequestPermissionsActivity.class), 100);
                 finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.preBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    private static final String TAG = "MainActivity";

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVHelper.init(this);
        reqPermission();
    }


    private FragmentActivity getSelf() {
        return this;
    }

    private boolean permissionSuccess;

    private void reqPermission() {
        if (permissionSuccess) {
            return;
        }
        PermissionCompat.create(this)
            .permissions(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .explain("程序需要此权限，拒绝可能会导致您无法接收订单消息", "我们需要储存权限，拒绝可能会导致应用无法正常运行")
            // .retry(true)
            .callBack(new OnRequestPermissionsCallBack() {
                @Override
                public void onGrant() {
                    // todo 权限授权成功回调
                    permissionSuccess = true;
                    BaseConfig.init();
                }

                @Override
                public void onDenied(String permission, boolean retry) {
                    // todo 权限授权失败回调
                    permissionSuccess = false;
                    ToastUitl.showShort("权限授权失败,无法正常使用小官");
                    //                        finish();
                }
            })
            .build()
            .request();
    }
}
