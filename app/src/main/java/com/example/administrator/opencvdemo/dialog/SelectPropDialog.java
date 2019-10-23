package com.example.administrator.opencvdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.opencvdemo.BaseApplication;
import com.example.administrator.opencvdemo.R;
import com.example.administrator.opencvdemo.model.PointModel;

import java.util.List;

/**
 * @author 吴祖清
 * @version $Rev$
 * @createTime 2017/12/26 13:21
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate 2017/12/26$
 * @updateDes ${TODO}
 */

public class SelectPropDialog extends Dialog {
    private CommonAblistViewAdapter<PointModel> mAdapter;

    public SelectPropDialog(@NonNull Context context) {
        super(context);
        init(context);
    }


    private void init(@NonNull final Context context) {
        ListView listView = new ListView(context);
        mAdapter = new CommonAblistViewAdapter<PointModel>(context, R.layout.item_text) {
            @Override
            public void convert(ViewHolderHelper holder, PointModel pointModel, int position) {
                holder.setText(R.id.text, pointModel.getName());
            }
        };
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCall != null) {
                    mCall.call(mAdapter.get(position));
                }
                dismiss();
            }
        });
        setContentView(listView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) BaseApplication.getRatioY(0.6f)));
    }

    public void setData(List<PointModel> models) {
        mAdapter.replaceAll(models);
    }

    private Call mCall;

    public void setCall(Call call) {
        mCall = call;
    }

    public abstract static class Call<T> {
        public abstract void call(T ts);

        public void call(T... ts) {
        }
    }
}
