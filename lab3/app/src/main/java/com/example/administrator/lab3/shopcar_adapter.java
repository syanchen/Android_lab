package com.example.administrator.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class shopcar_adapter extends BaseAdapter {
    private Context context;
    private List<Items> mDatas;

    public shopcar_adapter(Context context, List<Items> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        if(mDatas!=null){
            return mDatas.size();
        }
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null) {
            return mDatas.get(position);
        }
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        public TextView textview1,textview2,textview3;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v;
        ViewHolder holder;
        if(view==null){
            //通过inflate的方法加载布局，
            v= LayoutInflater.from(context).inflate(R.layout.shopcar_item,null);//LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化,作用类似于findViewById()
            holder = new ViewHolder();//去掉shopadapter
            holder.textview1 = (TextView) v.findViewById(R.id.iname);
            holder.textview2 = (TextView) v.findViewById(R.id.iprice);////写错id导致界面加载失败
            holder.textview3 = (TextView) v.findViewById(R.id.logo);
            v.setTag(holder);
        }
        else{
            v = view;
            holder = (ViewHolder) v.getTag();
        }
        holder.textview1.setText(mDatas.get(position).getName());
        holder.textview2.setText(mDatas.get(position).getPrice());
        holder.textview3.setText(mDatas.get(position).getName().substring(0,1));//用subsequence就没有大小写转换
        return v;
    }
}
