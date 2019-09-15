package com.example.administrator.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.module_orc.IDiscernCallback;
import com.example.module_orc.OpenCVHelper;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;
import com.example.module_orc.WorkMode;

import java.util.List;


public class MainActivity1 extends AppCompatActivity {

    private ImageView img, ivCrop;
    private Button btn;
    private int[] resIds = {R.mipmap.chufu, R.mipmap.chuligongwu, R.mipmap.zisi, R.mipmap.zican, R.mipmap.hongyanzhiji,R.mipmap.wzq1};
    private String[] resNames = {"main","clgw", "wdzs", "jyzc", "hyzj","sfz"};
//    private int[] resIds = {R.mipmap.wzq1,R.mipmap.wzq, R.mipmap.wxb, R.mipmap.yl, R.mipmap.wzq1};

    private int currentIndex = 0;
    private TextView vTvResult;
    private EditText vEtLangName;

    private RecyclerView vRecyclerView;
    private TestAdapter vTestAdapter;
    OrcModel orcModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btn = (Button) findViewById(R.id.btn);
        this.img = (ImageView) findViewById(R.id.img);
        this.vTvResult = findViewById(R.id.resultText);
        this.ivCrop = findViewById(R.id.img_crop);
        this.vEtLangName = findViewById(R.id.lengName);
        this.vRecyclerView = findViewById(R.id.rcv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resIds[currentIndex % resIds.length]);
                String langName = vEtLangName.getText().toString().trim();
                OrcHelper.getInstance().executeCallAsync(WorkMode.ONLY_BITMAP, bitmap, langName, resNames[currentIndex % resIds.length], new IDiscernCallback() {
                    @Override
                    public void call(final List<OrcModel> result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                vTestAdapter.setmDatas(result);
//                                vTvResult.setText(result.toString());
//                                Log.d(TAG, "executeCallAsync: " + result.toString());
                                try {
                                    orcModel = result.get(0);
                                    img.setImageBitmap(orcModel.getBitmap());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orcModel != null) {
                    BitmapPreviewFragment.show(getSelf(), orcModel.getBitmap());
                }
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
//                startActivity(new Intent(MainActivity1.this,ScanActivity.class));
            }
        });
        img.setImageResource(resIds[currentIndex % resIds.length]);
        vTestAdapter = new TestAdapter();

        vRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        vRecyclerView.setAdapter(vTestAdapter);
        vTestAdapter.setOrcModelOnItemClickListener(new TestAdapter.OnItemClickListener<OrcModel>() {
            @Override
            public void onItemClick(TestAdapter.ViewHolder holder, OrcModel data, int position) {
                BitmapPreviewFragment.show(getSelf(), data.getBitmap());
            }
        });
    }


    private static final String TAG = "MainActivity";

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVHelper.init(this);
    }

    private FragmentActivity getSelf() {
        return this;
    }
}
