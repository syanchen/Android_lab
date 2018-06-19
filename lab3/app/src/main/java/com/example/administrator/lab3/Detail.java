package com.example.administrator.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/10/22.
 */

public class Detail extends AppCompatActivity {////////////////////////缺少后面这一段会使onCreate变灰
    private RecyclerView mRecyclerView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout sec=(LinearLayout)findViewById(R.id.secondview);
        sec.setVisibility(View.INVISIBLE);
        FloatingActionButton flt=(FloatingActionButton)findViewById(R.id.shopcar);
        flt.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();

        final String mname=intent.getStringExtra("Name");//接收到来自mainActivity的商品数据再传到SsecondActivity里面去用
        final String mprice=intent.getStringExtra("Price");
        final String minfo=intent.getStringExtra("Info");

        SsecondActivity mAdapter;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new SsecondActivity(mname,mprice,minfo,Detail.this,Detail.this));//传数据



    }
}
