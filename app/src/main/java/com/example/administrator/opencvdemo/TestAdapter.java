package com.example.administrator.opencvdemo;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 作者：士元
 * 时间：2019/5/21 16:22
 * 邮箱：wuzuqing@linghit.com
 * 说明：
 */
public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<Model> mDatas;

    public List<Model> getmDatas() {
        return mDatas;
    }

    public void setmDatas(List<Model> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_img,viewGroup,false);

        return new ViewHolder(view);
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
