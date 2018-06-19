package com.example.administrator.myapplication_2;

import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.administrator.myapplication_2.R.id.textInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnCheckedChangeListener {
   
    public ImageView mImage;
    public RadioGroup radioGroup;
    public RadioButton rbtn1;
    public RadioButton rbtn2;
    public TextInputLayout user;
    public TextInputLayout password;
    public Button btn1;
    public Button btn2;
    int arg;
    public EditText userEdit;
    public EditText passwordEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImage = (ImageView) findViewById(R.id.imageView);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        rbtn1=(RadioButton)findViewById(R.id.radiobutton1);
        rbtn2=(RadioButton)findViewById(R.id.radiobutton2);
        user=(TextInputLayout)findViewById(textInputLayout);
        password=(TextInputLayout)findViewById(R.id.textInputLayout2);
        btn1=(Button)findViewById(R.id.buttonA);
        btn2=(Button)findViewById(R.id.buttonB);

        userEdit=user.getEditText();
        passwordEdit = password.getEditText();//要先从约束布局里面得到text才能对其内容进行监听

        mImage.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);/////有问题导致app直接闪退
        userEdit.setOnClickListener(this);/////之前的监控对象是user
        passwordEdit.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageView:
                Dialog();
                break;
            case R.id.radiogroup:
                onCheckedChanged(radioGroup,arg);
                break;
            case R.id.textInputLayout:
                TextInputLayout();
                break;
            case R.id.textInputLayout2:
                TextInputLayout();
                break;
            case R.id.buttonA:
                TextInputLayout();
                break;
            case R.id.buttonB:
                Button_use();
                break;
        }
    }

    void Dialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("上传图片");
        final String[] items = new String[]{"拍摄", "从相册选择"};
        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "您选择了" +items[which], Toast.LENGTH_SHORT).show();
                    }
                }
        );
        alertDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "您选择了[取消]", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.create().show();
    }

    void TextInputLayout(){
        String number = userEdit.getText().toString();
        String Password = passwordEdit.getText().toString();
        int flag=0;

        if(TextUtils.isEmpty(number)){
            user.setError("学号不能为空");
        }
        else{
            flag++;
            user.setErrorEnabled(false);
        }
        if(TextUtils.isEmpty(Password)){
            password.setError("密码不能为空");
        }
        else{
            flag++;
            password.setErrorEnabled(false);
        }

        ///////////////////////////////////拓展功能
        if(passwordEdit.getText().length()<6 && !TextUtils.isEmpty(Password)){
            Toast.makeText(getApplicationContext(), "输入密码长度最好不少于6个字符", Toast.LENGTH_SHORT).show();
        }
        //////////////////////////////////////////////////////

        if(flag==2) {
            if (number.equals("123456")  && Password.equals("6666")) {                                       //只有equais函数才有用
                Snackbar snackbar=Snackbar.make(radioGroup, " 登录成功", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {}
                }).show();
            }
            else{
                Snackbar snackbar=Snackbar.make(radioGroup, "学号或密码错误", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {}
            });
                ///////////////////////////////////拓展功能
                View view = snackbar.getView();
                TextView text = (TextView) view.findViewById(R.id.snackbar_text);//得到snackbar的文字部分的对象
                Drawable d = ContextCompat.getDrawable(this, R.drawable.warning);//warning.jpg
                d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());//左，上，右，下
                text.setCompoundDrawables(d, null, null, null);   // 给TextView左边添加图标
                snackbar.show();
                /////////////////////////////////////////////////////////////////////////
            }
        }
    }

    void Button_use(){
        int arg1= radioGroup.getCheckedRadioButtonId();
        if (arg1 == rbtn1.getId()) {
            Snackbar.make(radioGroup, "学生注册功能尚未启用", Snackbar.LENGTH_INDEFINITE).setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            }).show();
        }
        else if(arg1 == rbtn2.getId()){
            Toast.makeText(getApplicationContext(), "教职工注册功能尚未启用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int arg1) {
        if (arg1 == rbtn1.getId()) {
            Snackbar.make(radioGroup, "您选择了学生", Snackbar.LENGTH_INDEFINITE).setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
        else if(arg1 == rbtn2.getId()){
            Snackbar.make(radioGroup, "您选择了教职工", Snackbar.LENGTH_INDEFINITE).setAction("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
    }
}


