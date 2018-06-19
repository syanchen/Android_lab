package com.example.administrator.lab8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/12.
 */

public class addActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText name;
    private EditText birth;
    private EditText gift;
    private Button btn_add;
    private myDB mydb;
    private int new_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        name=(EditText)findViewById(R.id.add_name);
        birth=(EditText)findViewById(R.id.add_birth);
        gift=(EditText)findViewById(R.id.add_gift);
        btn_add=(Button)findViewById(R.id.add2);

        name.setOnClickListener(this);
        birth.setOnClickListener(this);
        gift.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add2:
                Add_Info();
                break;
        }
    }

    public void Add_Info(){
        mydb=new myDB(addActivity.this);
        String add_name=name.getText().toString();
        String add_birth=birth.getText().toString();
        String add_gift=gift.getText().toString();

        if(TextUtils.isEmpty(add_name)){
            Toast.makeText(getApplicationContext(), "名字不能为空", Toast.LENGTH_SHORT).show();
        }
        else{
            if(mydb.IsNameExist(add_name)){
                Toast.makeText(getApplication(), "人物已存在", Toast.LENGTH_SHORT).show();
            }
            else{
                new_id = mydb.insert(add_name, add_birth, add_gift);
                Intent intent = new Intent(addActivity.this, MainActivity.class);
                intent.putExtra("id", new_id);
                intent.putExtra("name", add_name);
                intent.putExtra("birth", add_birth);
                intent.putExtra("gift", add_gift);
                setResult(1, intent);
                finish();
            }
        }

    }
}
