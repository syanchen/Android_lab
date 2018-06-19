package com.example.administrator.lab7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText newp;
    private EditText Conp;
    private Button ok;
    private Button clear;
    String new_password,Confirm_password;
    SharedPreferences preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindView();
        Bind();
        preference=getSharedPreferences("Info", Context.MODE_PRIVATE);
        ReadAccount();
    }

    public void FindView(){
        newp=(EditText) findViewById(R.id.new_password);
        Conp=(EditText)findViewById(R.id.confirm_password);
        ok=(Button) findViewById(R.id.Ok);
        clear=(Button)findViewById(R.id.Clear1);
    }

    void Bind(){
        newp.setOnClickListener(this);
        Conp.setOnClickListener(MainActivity.this);
        ok.setOnClickListener(this);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.Ok:
                Finish();
                break;
            case R.id.Clear1:
                ClearText();
                break;
        }
    }

    public void Finish(){
        new_password=newp.getText().toString();
        Confirm_password=Conp.getText().toString();
        SharedPreferences.Editor editor=preference.edit();
        editor.putString("new_password", new_password);
        editor.putString("Confirm_password", Confirm_password);
        editor.apply();//apply异步，commit同步
        if(TextUtils.isEmpty(Confirm_password) || TextUtils.isEmpty(new_password)){
            Toast.makeText(getApplicationContext(), "Password cannot empty", Toast.LENGTH_SHORT).show();
        }
        else{
            if(new_password.equals(Confirm_password) && !TextUtils.isEmpty(new_password)){
                Intent intent=new Intent(this,file_edit.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void ClearText(){
        newp.setText("");
        Conp.setText("");
    }

    public void ReadAccount(){
        String password1=preference.getString("new_password", "");
        String password2=preference.getString("Confirm_password", "");
        newp.setText(password1);
        Conp.setText(password2);
        if(!password2.equals("")){
            Conp.setVisibility(View.INVISIBLE);
            newp.setHint("Password");
            newp.setText("");
        }
    }

}
