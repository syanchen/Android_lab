package com.example.administrator.lab3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity {

    private List<Items> Item_list ;
    private List<Items> carItem;
    private CommonAdapter adapter;
    private LinearLayout sec;
    private ListView listview;
    private shopcar_adapter buy_adapter;
    private ImageButton car;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sec=(LinearLayout)findViewById(R.id.secondview);///////////////////////一开始购物车界面不可见
        sec.setVisibility(View.INVISIBLE);

        Item_list  = new ArrayList<Items>();
        carItem = new ArrayList<Items>();

        final String[] Name = new String[]{
                "Enchated Forest","Arla Milk","Devondale Milk",
                "Kindle Oasis", "Waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher",
                "Maltesers","Lindt","Borggreve"
        };

        final String[] Price = new String[]{"¥ 5.00", "¥ 59.00", "¥ 79.00", "¥ 2399.00",
                "¥ 179.00", "¥ 14.00", "¥ 132.59", "¥ 141.43", "¥ 139.43", "¥ 28.90"};
        String[] Info = new String[]{"作者 Johanna Basford", "产地 德国", "产地 澳大利亚",
                "版本 8GB", "重量 2Kg", "产地 英国", "重量 300g", "重量 118g", "重量 249g", "重量 640g"};


            for(int i=0;i<10;i++){
                Item_list.add(new Items(Name[i],Price[i],Info[i]));
            }


///////////////////////////////////////////////////////////////使用适配器放入信息
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CommonAdapter(MainActivity.this,Item_list);
        recyclerView.setAdapter(adapter);

        ////////////////////////////////////////////////////////////////点击商品
        adapter.setOnTtemClickListener(new CommonAdapter.OnItemClickListener() {
            ////////////////////////////////////////////////////跳到对应详情
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, Detail.class);
                intent.putExtra("Name", Item_list.get(position).getName());
                intent.putExtra("Price", Item_list.get(position).getPrice());
                intent.putExtra("Info", Item_list.get(position).getInfo());
                startActivityForResult(intent, 0);
            }
            ///////////////////////////////////////////////////长按删除某个物品
            @Override
                public void onLongClick(int position) {
                    Toast.makeText(MainActivity.this,"移除第"+String.valueOf(position+1)+"个商品",Toast.LENGTH_SHORT).show();//////////////////////////注意是position+1
                Item_list.remove(position);
                    adapter.notifyDataSetChanged();//用于修改完数据后更新界面
                }
        });

        buy_adapter=new shopcar_adapter(MainActivity.this,carItem);
        listview=(ListView)findViewById(R.id.shoppingCar);//直接使用已有适配器即可
        listview.setAdapter(buy_adapter);//只能在外面声明？

        /////////////////////////////////////购物车的按钮被点击，读入此时的商品数据
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Detail.class);//Ssecongview 改成DSetail

                intent.putExtra("Name", carItem.get(position).getName());
                intent.putExtra("Price", carItem.get(position).getPrice());
                intent.putExtra("Info", carItem.get(position).getInfo());
//                startActivity(intent);
                startActivityForResult(intent,0);//传出一个返回值为0
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);//只是this不行，要是MainActivity才可以
                alertDialog.setTitle("移除商品");
                alertDialog.setMessage("从购物车移除"+carItem.get(position).getName()+"?").setPositiveButton("确认",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(carItem.remove(position)!=null){
                            buy_adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "您选择了[确认]", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "您选择了[取消]", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                return true;
            }
        });

        //////////////////////////////////////////////////购物车界面和商品界面的图标不能同时出现,类似于星星之间的转换的做法
        car=(ImageButton)findViewById(R.id.shopcar);
        car.setTag("0");
        car.setOnClickListener(new View.OnClickListener() {//////////////////////////////////处理星星图案被点击时要发生变化
            @Override
            public void onClick(View view) {
                Object flag=car.getTag();///////////////////////////不能为int型
                if(flag=="0"){////////////////////////1改0？？？？？？？？？？？？？？
                    car.setTag("1");
                    car.setImageResource(R.drawable.mainpage);
                    sec.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                else{
                    car.setImageResource(R.drawable.shoplist);
                    sec.setVisibility(View.INVISIBLE);
                    car.setTag("0");
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

//    第一个参数为请求码，即调用startActivityForResult()传递过去的值
//    第二个参数为结果码，结果码用于标识返回数据来自哪个新Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0 && resultCode == 0){//进入了商品详情界面进行了点击，所以仅当两个都为0时表示商品被添加
            Bundle bundle=data.getExtras();
            String mingzi=bundle.getString("Nam");
            String jiage=bundle.getString("Pric");
            String info=bundle.getString("Inf");
            int cnt=bundle.getInt("cnt",0);//没有0
            for(int i=0;i<cnt;i++){
                carItem.add(new Items(mingzi,jiage,info));
            }
            buy_adapter.notifyDataSetChanged();
        }
    }

}









