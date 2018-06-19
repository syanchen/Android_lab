package com.example.administrator.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;

/**
 * Created by Administrator on 2017/10/21.
 */

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.MyViewHolder>{

    private OnItemClickListener mOnItemClickListener=null;//对接口进行初始化
    private List<Items> mDatas;
    private Context context;

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public CommonAdapter(Context context,List<Items> mDatas){
        this.context=context;
        this.mDatas=mDatas;
    }

    //获得某个数据
        public Items getItem(int i){
            if(mDatas==null){
                return null;
            }
            return mDatas.get(i);
        }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_layout, parent,
                false));
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv1,tv2;

        public MyViewHolder(View view)
        {
            super(view);
            tv1 = (TextView) view.findViewById(R.id.Myname);
            tv2 = (TextView) view.findViewById(R.id.Myletter);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv1.setText(mDatas.get(position).getName());
        holder.tv2.setText(mDatas.get(position).getName().substring(0,1));//getFirstLetter
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);//holder.getAdapterPosition()
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    //获得数据项de列表长度
    @Override
    public int getItemCount() {
        if(mDatas==null) return 0;
        else return mDatas.size();
    }

    //定义，实现一个adapter点击接口
    public void setOnTtemClickListener(OnItemClickListener onTtemClickListener){
        this.mOnItemClickListener=onTtemClickListener;
    }
}
