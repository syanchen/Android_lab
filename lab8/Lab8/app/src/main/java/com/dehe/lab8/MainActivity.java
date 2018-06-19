package com.dehe.lab8;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button add;
    private List<String> detail;
    private ListView lv;
    private  myDB mydb;
    private static final String DB_NAME="Contacts.db";
    private ItemAdapter itemAdapter;
    private int before;
    private int latter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
    }
    private void bind(){
        mydb=new myDB(MainActivity.this,DB_NAME,null,1);
        detail=new ArrayList<>() ;
        mydb.getWritableDatabase();
        final Cursor cursor=mydb.query();
        while(cursor.moveToNext()){
            detail.add(cursor.getString(0));
        }
        mydb.close();
        add=(Button)findViewById(R.id.add);
        lv=(ListView)findViewById(R.id.item);
        itemAdapter=new ItemAdapter(MainActivity.this,detail);
        lv.setAdapter(itemAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView,View view, final int i,long l){
                LayoutInflater factor=LayoutInflater.from(MainActivity.this);
                final View view_in=factor.inflate(R.layout.dialoglayout,null);
                final AlertDialog.Builder alter=new AlertDialog.Builder(MainActivity.this);
                alter.setView(view_in);
                final TextView dialogname2=view_in.findViewById(R.id.dialogname2);
                final TextView dialogbirth2=view_in.findViewById(R.id.dialogbirth2);
                final TextView dialoggift2=view_in.findViewById(R.id.dialoggift2);
                final TextView dialogphone2=view_in.findViewById(R.id.dialogphone2);
                alter.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newBirth=dialogbirth2.getText().toString();
                        String newGift=dialoggift2.getText().toString();
                        mydb.update(dialogname2.getText().toString(),newBirth,newGift);
                        itemAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialogname2.setText(detail.get(i));
                dialogbirth2.setText(mydb.getBirth(detail.get(i)));
                dialoggift2.setText(mydb.getGift(detail.get(i)));
                String Number=queryContactPhoneNumber(detail.get(i));
               if("".equals(Number))
                   dialogphone2.setText("无");
                else dialogphone2.setText(Number);
                alter.show();
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("是否删除？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mydb.delete(detail.get(i));
                        itemAdapter.notifyDataSetChanged();
                        detail.remove(i);
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return true;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddItem.class);
                        Cursor cc=mydb.query();
                        before=cc.getCount();
                        startActivity(intent);
            }
        });
    }
    private String queryContactPhoneNumber(String target) {
        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                cols, null, null, null);
        String Number="";
        Cursor phone=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" =\'"+target+"\'",null,null);
        while (phone.moveToNext()){
            Number+=phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))+" ";
        }
        return Number;
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        Cursor cc=mydb.query();
        latter=cc.getCount();
        if(latter!=before){
            cc.moveToLast();
            detail.add(cc.getString(0));
        }
        itemAdapter.notifyDataSetChanged();
    }


    private class ItemAdapter extends BaseAdapter {
        private Context context;
        private List<String> detail;
        public ItemAdapter(Context context,List<String> detail) {
            this.context = context;
            this.detail=detail;
        }
        @Override
        public int getCount() {
            if (detail != null) {
                return detail.size();
            } else return 0;
        }
        @Override
        public Object getItem(int i) {
            if (detail == null) {
                return null;
            } else {
                return detail.get(i);
            }
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            View convertView;
            ViewHolder holder;
            if (view == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
                holder = new ViewHolder();
                holder.tv=convertView.findViewById(R.id.nameitem);
                holder.tv2=convertView.findViewById(R.id.birthitem);
                holder.tv3=convertView.findViewById(R.id.giftitem);
                convertView.setTag(holder);
            } else {
                convertView = view;
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(detail.get(position));
            holder.tv2.setText(mydb.getBirth(detail.get(position)));
            holder.tv3.setText(mydb.getGift(detail.get(position)));
            return convertView;
        }
        private class ViewHolder {
            public TextView tv;
            public TextView tv2;
            public TextView tv3;
        }
    }
}

