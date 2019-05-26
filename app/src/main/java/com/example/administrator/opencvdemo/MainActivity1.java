package com.example.administrator.opencvdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.module_orc.OpenCVHelper;
import com.example.module_orc.OrcHelper;


public class MainActivity1 extends AppCompatActivity {

    private ImageView img, ivCrop;
    private Button btn;
    private int[] resIds = {R.mipmap.wzq1,R.mipmap.wzq, R.mipmap.wxb, R.mipmap.yl, R.mipmap.wzq1};

    private int currentIndex = 0;
    private TextView vTvResult;
    private EditText vEtLangName;

    private RecyclerView vRecyclerView;
    private TestAdapter vTestAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OrcHelper.getInstance().init(this);
        this.btn = (Button) findViewById(R.id.btn);
        this.img = (ImageView) findViewById(R.id.img);
        this.vTvResult = findViewById(R.id.result);
        this.ivCrop = findViewById(R.id.img_crop);
        this.vEtLangName = findViewById(R.id.lengName);
        this.vRecyclerView = findViewById(R.id.rcv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIds[currentIndex % resIds.length]);
//                String langName = vEtLangName.getText().toString().trim();
//                OrcHelper.getInstance().executeCallAsync("id", bitmap, langName, new IDiscernCallback() {
//                    @Override
//                    public void call(final List<OrcModel> result) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                vTestAdapter.setmDatas(result);
//                                Log.d(TAG, "executeCallAsync: " + result.toString());
//                            }
//                        });
//                    }
//                });
                PrivateMessageBarManager.getInstance().show(getWindow().getDecorView(), "1", "", "张三", "至于补间动画的使用，Animation还有如下一些比较实用的方法介绍", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),v.getTag()+"",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.preBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = 0;
                }
                img.setImageResource(resIds[currentIndex % resIds.length]);
            }
        });
        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                img.setImageResource(resIds[currentIndex % resIds.length]);
                startActivity(new Intent(MainActivity1.this,ScanActivity.class));
            }
        });
        img.setImageResource(resIds[currentIndex % resIds.length]);
        vTestAdapter = new TestAdapter();

        vRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        vRecyclerView.setAdapter(vTestAdapter);
    }


    private static final String TAG = "MainActivity";

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVHelper.init(this);
    }
}
