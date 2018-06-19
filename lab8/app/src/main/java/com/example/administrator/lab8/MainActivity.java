package com.example.administrator.lab8;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype.Fadein;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button add;
    private ListView listView;
    private TextView name_change;
    private EditText birth_change;
    private EditText gift_change;
    private TextView tele_num;
    private myDB ContactsmyDB;
    private AlertDialog.Builder change_alertdialog;
    private SimpleAdapter Contact_Adapter;
    List<Map<String, Object>> ContactsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add=(Button)findViewById(R.id.add);
        add.setOnClickListener(this);

        ContactsmyDB=new myDB(MainActivity.this);
        ContactsList=ContactsmyDB.generateList();

        listView=(ListView)findViewById(R.id.BirthList);
        Contact_Adapter=new SimpleAdapter(MainActivity.this, ContactsList, R.layout.item,
                new String[]{"name", "birth", "gift"},
                new int[]{R.id.nameLV, R.id.birthLV, R.id.giftLV});
        listView.setAdapter(Contact_Adapter);

        change_alertdialog = new AlertDialog.Builder(this);
        change_alertdialog.setTitle("大吉大利，一起吃鸡");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                View view_in = factor.inflate(R.layout.dialog_change, null);
                change_alertdialog.setView(view_in);

                final int temp_id = (int)ContactsList.get(position).get("id");
                Map<String, Object> current = ContactsmyDB.query(temp_id);
                final String curr_name = (String)current.get("name");
                final String curr_birht = (String)current.get("birth");
                final String curr_gift = (String)current.get("gift");

                name_change = (TextView) view_in.findViewById(R.id.change_name);
                birth_change = (EditText) view_in.findViewById(R.id.change_birth);
                gift_change = (EditText) view_in.findViewById(R.id.change_gift);
                tele_num = (TextView) view_in.findViewById(R.id.tele_num);

                name_change.setText(curr_name);
                tele_num.setText(getTele(curr_name));
                birth_change.setText(curr_birht);
                gift_change.setText(curr_gift);

                change_alertdialog.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String curr_birth = birth_change.getText().toString();
                        String curr_gift = gift_change.getText().toString();
                        ContactsmyDB.update(temp_id, curr_name, curr_birth, curr_gift);//数据库更新
                        Map<String, Object> temp = ContactsList.get(position);
                        temp.put("birth", curr_birth);
                        temp.put("gift", curr_gift);
                        ContactsList.set(position, temp);
                        Contact_Adapter.notifyDataSetChanged();//页面更新
                    }
                }).setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定删除？")
                        .setCancelText("不，朕再想想")
                        .setConfirmText("确定删除")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("取消")
                                        .setConfirmText("留你一命")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("成功删除!")
                                        .setConfirmText("完成")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                int temp_id;
                                do{
                                    temp_id = (int)ContactsList.get(position).get("id");
                                }while(position!=0);
                                ContactsmyDB.delete(temp_id);
                                ContactsList.remove(position);
                                Contact_Adapter.notifyDataSetChanged();
                            }
                        })
                        .show();
                return true;
            }
        });
    }

    public String getTele(String name){
        String Number = "";
        int isHas=0;
        Cursor cursor=getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,
                ContactsContract.PhoneLookup.DISPLAY_NAME+"=?",new String[]{name},null);
        //判断某条联系人信息中否有电话号码
        assert cursor != null;
        if(cursor.moveToFirst()){
            isHas=Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
        }
        if(isHas==1){
            int ContactID=cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactID,
                    null, null);
            assert phone != null;
            while(phone.moveToNext()) {//逐位提取号码并进行string型转换
                Number+=phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
            }
            phone.close();
        }else{
            Number = "无";
        }
        cursor.close();
        return Number;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                Add();
                break;
        }
    }

    public void Add(){
        Intent intent=new Intent(this,addActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, Object> new_item = new HashMap<>();
        if(requestCode ==1 && resultCode==1){
            new_item.put("id", data.getIntExtra("id", -1));
            new_item.put("name", data.getStringExtra("name"));
            new_item.put("birth", data.getStringExtra("birth"));
            new_item.put("gift", data.getStringExtra("gift"));
            ContactsList.add(new_item);
            Contact_Adapter.notifyDataSetChanged();
        }
    }



}
