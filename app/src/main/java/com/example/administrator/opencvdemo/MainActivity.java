package com.example.administrator.opencvdemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.opencvdemo.floatservice.RequestPermissionsActivity;
import com.example.administrator.opencvdemo.model.PointModel;
import com.example.administrator.opencvdemo.model.Result;
import com.example.administrator.opencvdemo.notroot.ServiceHelper;
import com.example.administrator.opencvdemo.util.HandlerUtil;
import com.example.administrator.opencvdemo.util.LogUtils;
import com.example.administrator.opencvdemo.util.SPUtils;
import com.example.administrator.opencvdemo.util.ScreenCapture;
import com.example.administrator.opencvdemo.util.Util;
import com.example.administrator.opencvdemo.youtu.ImageParse;
import com.example.module_orc.IDiscernCallback;
import com.example.module_orc.OpenCVHelper;
import com.example.module_orc.OrcConfig;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.module_orc.WorkMode.ONLY_BITMAP;

public class MainActivity extends AppCompatActivity {

    private ImageView img, ivCrop;
    private Button btn;
    private int[] resIds = { R.mipmap.chufu, R.mipmap.chuligongwu, R.mipmap.zisi, R.mipmap.zican, R.mipmap.hongyanzhiji, R.mipmap.wzq1};
    private String[] resNames = {"main", "clgw", "wdzs", "jyzc", "hyzj", "sfz"};
    //    private int[] resIds = {R.mipmap.wzq1,R.mipmap.wzq, R.mipmap.wxb, R.mipmap.yl, R.mipmap.wzq1};

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

                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {
                        OrcHelper.getInstance().compress(OrcHelper.getInstance().rootDir);
                    }
                });
            }
        });
        findViewById(R.id.showFloatView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ServiceHelper.getInstance().goAccess()){
                    startActivity(new Intent(getSelf(), RequestPermissionsActivity.class));
                    finish();
                }
            }
        });
        File imagePath = OrcHelper.getInstance().rootDir;
        Log.d(TAG, "onCreate: ");
        File[] files = imagePath.listFiles();
        fileList = new ArrayList<>();
        if (files!=null){
            for (File file : files) {
                if (file.isFile() && ( file.getName().endsWith(".jpg") ||  file.getName().endsWith(".png"))) {
                    fileList.add(file);
                }
            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                if (fileList.isEmpty()) {
                    bitmap = BitmapFactory.decodeResource(getResources(), resIds[currentIndex % resIds.length]);
                } else {
                    bitmap = BitmapFactory.decodeFile(fileList.get(currentIndex % fileList.size()).getAbsolutePath());
                }
                if (bitmap == null) {
                    return;
                }
                final String langName = vEtLangName.getText().toString().trim();
                String pageName = fileList.isEmpty() ? resNames[currentIndex % resIds.length] : fileList.get(currentIndex % fileList.size()).getName();
                String string = vEtThresh.getText().toString();
                if (!TextUtils.isEmpty(string)) {
                    OrcConfig.method = Integer.valueOf(string);
                }
                final Bitmap finalBitmap = bitmap;
                HandlerUtil.async(new Runnable() {
                    @Override
                    public void run() {
                        ImageParse.getSyncData(finalBitmap,new ImageParse.Call() {
                            @Override
                            public void call(List<Result.ItemsBean> result) {
                                if (result == null || result.size() == 0) return;
                                try {
                                    List<PointModel> jyzcModel = new ArrayList<>();
                                    int index = 0;
                                    for (Result.ItemsBean itemsBean : result) {
                                        LogUtils.logd("JyzcTaskElement:"+itemsBean.getItemstring());
                                        if (TextUtils.equals(itemsBean.getItemstring(),"经营")){
                                            PointModel model = new PointModel(String.valueOf(index), "经营");
                                            model.setX(itemsBean.getItemcoord().getX()+itemsBean.getItemcoord().getWidth()/2);
                                            model.setY(itemsBean.getItemcoord().getY()+itemsBean.getItemcoord().getHeight()/2);
                                            model.setNormalColor(Util.getColor(ScreenCapture.get().getCurrentBitmap(),model.getX(),model.getY()));
                                            jyzcModel.add(model);
                                            index++;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                SPUtils.setBoolean("jpzc_init",false);
                            }
                        });
                    }
                });

                OrcHelper.getInstance().executeCallAsync(ONLY_BITMAP, bitmap, langName, pageName, new IDiscernCallback() {
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
                                    Toast.makeText(MainActivity.this, "" + orcModel.getResult(), Toast.LENGTH_SHORT).show();
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
                if (!fileList.isEmpty()) {
                    Glide.with(MainActivity.this).load(fileList.get(currentIndex % fileList.size())).into(img);
                    return;
                }
                img.setImageResource(resIds[currentIndex % resIds.length]);
            }
        });
        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if (!fileList.isEmpty()) {
                    Glide.with(MainActivity.this).load(fileList.get(currentIndex % fileList.size())).into(img);
                    return;
                }

                img.setImageResource(resIds[currentIndex % resIds.length]);
                //                startActivity(new Intent(MainActivity.this,ScanActivity.class));
            }
        });
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = OrcConfig.topColorXishu;
        Bitmap bitmap = null;
        if (fileList.isEmpty()) {
            bitmap = BitmapFactory.decodeResource(getResources(), resIds[currentIndex % resIds.length], options);
            img.setImageBitmap(bitmap);
        } else {
            Glide.with(MainActivity.this).load(fileList.get(currentIndex % fileList.size())).into(img);
        }

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
