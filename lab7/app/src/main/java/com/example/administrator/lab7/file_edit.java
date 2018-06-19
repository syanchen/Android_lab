package com.example.administrator.lab7;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017/12/5.
 */

public class file_edit extends AppCompatActivity implements View.OnClickListener{
    private EditText edit;
    private EditText content;
    private Button save;
    private Button load;
    private Button clear;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_editor);
        FindView();
        Bind();
    }

    public void FindView(){
        edit=(EditText)findViewById(R.id.file_edit);
        content=(EditText)findViewById(R.id.file_content);
        save=(Button)findViewById(R.id.Save);
        load=(Button)findViewById(R.id.Load);
        clear=(Button)findViewById(R.id.Clear2);
        delete=(Button)findViewById(R.id.Delete);
    }

    public void Bind(){
        edit.setOnClickListener(this);
        content.setOnClickListener(this);
        save.setOnClickListener(this);
        load.setOnClickListener(this);
        clear.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.Save:
                Save();
                break;
            case R.id.Clear2:
                ClearText();
                break;
            case R.id.Load:
                Load();
                break;
            case R.id.Delete:
                Delete();
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Save(){
        try  {
            FileOutputStream fileOutputStream = openFileOutput(edit.getText().toString(), MODE_PRIVATE);
            String str = "";
            str = content.getText().toString();
            fileOutputStream.write(str.getBytes());
            Toast.makeText(file_edit.this, "Save successfully.", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "Successfully saved file.");
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException ex) {
            Log.e("TAG", "Fail to save file.");
        }
    }

    public void ClearText(){
        edit.setText("");
        content.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void Load(){
        try{
            FileInputStream FI = openFileInput(edit.getText().toString());
            byte[] cont = new byte[FI.available()];
            FI.read(cont);
            String contents = new String(cont);
            content.setText(contents);
            Toast.makeText(file_edit.this, "Load Successfully", Toast.LENGTH_SHORT).show();
            Log.i("TAG", "Successfully Read File.");
        }catch (IOException e){
            Toast.makeText(file_edit.this, "Fail to Load File", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Fail to Load File.");
        }
    }

    public void Delete(){
        if (file_edit.this.getApplicationContext().deleteFile(edit.getText().toString())) {
            Toast.makeText(file_edit.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(file_edit.this, "Fail to Delete File", Toast.LENGTH_SHORT).show();
        }
    }


}
