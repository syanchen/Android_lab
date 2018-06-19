package com.example.administrator.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/22.
 */

public class ListviewAdapter extends BaseAdapter{

    private Context context;
    List<String> mDatas;

    public ListviewAdapter(Context context, List<String> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDatas != null) {
            return mDatas.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        public TextView textview;
    }

    public View getView(int position, View view, ViewGroup viewGroup){
        View v;
        ViewHolder holder;
        if(view==null){
            //通过inflate的方法加载布局，
            v= LayoutInflater.from(context).inflate(R.layout.second_layout_listview,null);
            holder = new ViewHolder();
            holder.textview = (TextView) v.findViewById(R.id.func);/////注意这里的textview是下面那四项文字的，先前写成了second_layout点击商品直接崩溃
            v.setTag(holder);
        }
        else{
            v = view;
            holder = (ViewHolder) v.getTag();
        }
        holder.textview.setText(mDatas.get(position));
        return v;
    }

}
