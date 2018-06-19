package com.example.administrator.lab3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/22.
 */

public class SsecondActivity extends RecyclerView.Adapter<SsecondActivity.MyViewHolder>{

    int cnt=0;
    private Context context;
    private String IName;
    Activity act;
    private String Price;
    private String Info;
    private List<String> new_s;

    private final String DYNAMICACTION="DYNAMIC";

    public SsecondActivity(String IName,String Price,String Info,Context context,Activity act){
        this.IName=IName;
        this.Price=Price;
        this.Info=Info;
        this.context=context;
        this.act=act;
    }

        class MyViewHolder extends RecyclerView.ViewHolder{

             TextView tv1;
             TextView tv2;
             TextView tv3;
             ImageView Pic;
             ImageButton imback;
             ImageButton star;
             ImageButton addshop;
             ListView ll2;

            public MyViewHolder(View view){
                super(view);
                tv1=(TextView)view.findViewById(R.id.item_name);
                tv2=(TextView)view.findViewById(R.id.price);
                tv3=(TextView)view.findViewById(R.id.weight);
                Pic=(ImageView) view.findViewById(R.id.pic);
                imback=(ImageButton)view.findViewById(R.id.imb);
                star=(ImageButton)view.findViewById(R.id.star);
                addshop=(ImageButton)view.findViewById(R.id.buy);
                ll2=(ListView)view.findViewById(R.id.listview2);
                star.setTag("0");
            }
        }

        @Override
        public SsecondActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.second_layout,parent,false);
            SsecondActivity.MyViewHolder holder=new SsecondActivity.MyViewHolder(view);
            return holder;
        }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv1.setText(IName);
        holder.tv2.setText(Price);
        holder.tv3.setText(Info);
        String[] opera=new String[] {"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};

        new_s= new ArrayList<>();;
        for(int j=0;j<opera.length;j++){
            new_s.add(opera[j]);
        }
/////////////////////////////////////////////////另一个fundescrib.java文件
        ListviewAdapter fun=new ListviewAdapter(context,new_s);
        holder.ll2.setAdapter(fun);

        final Intent intent=act.getIntent();
        act.setResult(1,intent);//REQUEST_OK 1变成0不可以，区别于添加商品到购物车的两个code

        holder.imback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.finish();
            }
        });

        holder.star.setOnClickListener(new View.OnClickListener() {//////////////////////////////////处理星星图案被点击时要发生变化
            @Override
            public void onClick(View view) {
                Object tag=holder.star.getTag();///////////////////////////不能为int型
                if(tag=="1"){
                    holder.star.setTag("0");
                    holder.star.setImageResource(R.drawable.empty_star);
                }
                else{
                    holder.star.setTag("1");
                    holder.star.setImageResource(R.drawable.full_star);
                }
            }
        });

        holder.addshop.setOnClickListener(new View.OnClickListener() {///////////////////////////////处理商品被添加到购物车的情况，要记录商品数量和对应商品信息
            @Override
            public void onClick(View view) {

                ////////////////////////////////////动态广播部分
                Intent intentBroadcast=new Intent(DYNAMICACTION);
                intentBroadcast.putExtra("name",IName);
                context.sendBroadcast(intentBroadcast);
                Toast.makeText(context,"商品已添加到购物车", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new MessageEvent(IName,Price,Info,0));
                ////////////////////////////////////////////////////

               // Toast.makeText(context,"商品已添加到购物车", Toast.LENGTH_SHORT).show();
//                cnt++;
//                intent.putExtra("cnt",cnt);
//                intent.putExtra("Nam",IName);
//                intent.putExtra("Pric",Price);
//                intent.putExtra("Inf",Info);
//
//                act.setResult(0,intent);
            }
        });

        switch (IName){
            case "Enchated Forest":
                holder.Pic.setImageResource(R.drawable.i0);
                break;
            case "Arla Milk":
                holder.Pic.setImageResource(R.drawable.i1);
                break;
            case "Devondale Milk":
                holder.Pic.setImageResource(R.drawable.i2);
                break;
            case "Kindle Oasis":
                holder.Pic.setImageResource(R.drawable.i3);
                break;
            case "Waitrose 早餐麦片":
                holder.Pic.setImageResource(R.drawable.i4);
                break;
            case "Mcvitie's 饼干":
                holder.Pic.setImageResource(R.drawable.i5);
                break;
            case "Ferrero Rocher":
                holder.Pic.setImageResource(R.drawable.i6);
                break;
            case "Maltesers":
                holder.Pic.setImageResource(R.drawable.i7);
                break;
            case "Lindt":
                holder.Pic.setImageResource(R.drawable.i8);
                break;
            case "Borggreve":
                holder.Pic.setImageResource(R.drawable.i9);
                break;
        }
    }

        @Override
        public int getItemCount() {
            return 1;
        }

}
