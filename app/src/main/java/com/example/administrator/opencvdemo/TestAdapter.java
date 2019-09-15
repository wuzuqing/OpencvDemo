package com.example.administrator.opencvdemo;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.module_orc.OrcModel;

import java.util.List;

/**
 * 作者：士元
 * 时间：2019/5/21 16:22
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<OrcModel> mDatas;

    public List<OrcModel> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<OrcModel> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    private OnItemClickListener<OrcModel> mOrcModelOnItemClickListener;

    public void setOrcModelOnItemClickListener(OnItemClickListener<OrcModel> orcModelOnItemClickListener) {
        mOrcModelOnItemClickListener = orcModelOnItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_img,viewGroup,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrcModelOnItemClickListener!=null){
                    int position = viewHolder.getLayoutPosition();
                    mOrcModelOnItemClickListener.onItemClick(viewHolder,mDatas.get(position),position);
                }
            }
        });
        return viewHolder;
    }
    public  interface OnItemClickListener<T>{
        void onItemClick(ViewHolder holder, T data,int position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.setBitmap(mDatas.get(i).getBitmap());
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView vView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vView = itemView.findViewById(R.id.img);
        }
        public void setBitmap(Bitmap bitmap){
            vView.setImageBitmap(bitmap);
        }
    }
}
