package com.dehe.lab8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/12/11.
 */

public class AddItem extends AppCompatActivity {
    private static final String DB_NAME="Contacts.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        final myDB mydb=new myDB(AddItem.this,DB_NAME,null,1);
        mydb.getWritableDatabase();
        final TextView addname=(TextView)findViewById(R.id.addname);
        final TextView addbirth=(TextView)findViewById(R.id.addbirth);
        final TextView addgift=(TextView)findViewById(R.id.addgift);
        final Button additem=(Button)findViewById(R.id.additem);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=addname.getText().toString();
                String birth=addbirth.getText().toString();
                String gift=addgift.getText().toString();
                String res=mydb.getBirth(name);
                if(name.equals("")){
                    Toast.makeText(AddItem.this,"名字不能为空，请完善",Toast.LENGTH_SHORT).show();
                }
                else if(!res.equals("无记录")){
                    Toast.makeText(AddItem.this,"名字重复啦，请检查",Toast.LENGTH_SHORT).show();
                }
                else{
                    mydb.insert(name,birth,gift);
                    finish();
                }
            }
        });
    }
}
