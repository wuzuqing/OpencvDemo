package com.example.administrator.opencvdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.module_orc.BaseCallBack1;
import com.example.module_orc.IDiscernCallback;
import com.example.module_orc.OpenCVHelper;
import com.example.module_orc.OrcHelper;
import com.example.module_orc.OrcModel;
import com.example.module_orc.WorkMode;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class MainActivity1 extends AppCompatActivity {

    private ImageView img, ivCrop;
    private Button btn;
    private int[] resIds = {R.mipmap.chufu, R.mipmap.chuligongwu, R.mipmap.zisi, R.mipmap.zican, R.mipmap.hongyanzhiji, R.mipmap.wzq1};
    private String[] resNames = {"main", "clgw", "wdzs", "jyzc", "hyzj", "sfz"};
//    private int[] resIds = {R.mipmap.wzq1,R.mipmap.wzq, R.mipmap.wxb, R.mipmap.yl, R.mipmap.wzq1};

    private int currentIndex = 0;
    private TextView vTvResult;
    private EditText vEtLangName;

    private RecyclerView vRecyclerView;
    private TestAdapter vTestAdapter;
    OrcModel orcModel;
    File[] listFiles;

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
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        File imagePath = new File(directory, "/image");
        listFiles = imagePath.listFiles();
        Log.d(TAG, "onCreate: " + Arrays.toString(listFiles));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = null;
                if (listFiles == null) {

                    bitmap = BitmapFactory.decodeResource(getResources(), resIds[currentIndex % resIds.length]);
                } else {
                    bitmap = BitmapFactory.decodeFile(listFiles[currentIndex % listFiles.length].getAbsolutePath());
                }
                if (bitmap == null) {
                    return;
                }
                final String langName = vEtLangName.getText().toString().trim();
//                 String pageName = listFiles == null ? resNames[currentIndex % resIds.length] : listFiles[currentIndex % listFiles.length].getName();
//                 OrcHelper.getInstance().executeCallAsync(WorkMode.ONLY_BITMAP, bitmap, langName, pageName, new IDiscernCallback() {
//                     @Override
//                     public void call(final List<OrcModel> result) {
//                         runOnUiThread(new Runnable() {
//                             @Override
//                             public void run() {
// //                                vTestAdapter.setmDatas(result);
// //                                vTvResult.setText(result.toString());
// //                                Log.d(TAG, "executeCallAsync: " + result.toString());
//                                 try {
//                                     orcModel = result.get(0);
//                                     img.setImageBitmap(orcModel.getBitmap());
//                                 } catch (Exception e) {
//                                     e.printStackTrace();
//                                 }
//                             }
//                         });
//                     }
//                 });
//                final String langName = "zwp";
               OrcHelper.getInstance().fileToBitmap(new BaseCallBack1<Bitmap>() {
                   @Override
                   public void call(final Bitmap bitmap, final String name) {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               OrcHelper.getInstance().executeCallAsync(WorkMode.ONLY_BITMAP, bitmap, langName, name, new IDiscernCallback() {
                                   @Override
                                   public void call(final List<OrcModel> result) {

                                   }
                               });
                           }
                       });
                   }
               },listFiles);
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
                if (listFiles != null) {
                    Glide.with(MainActivity1.this).load(listFiles[currentIndex % listFiles.length]).into(img);
                    return;
                }
                img.setImageResource(resIds[currentIndex % resIds.length]);
            }
        });
        findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if (listFiles != null) {
                    Glide.with(MainActivity1.this).load(listFiles[currentIndex % listFiles.length]).into(img);
                    return;
                }
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
