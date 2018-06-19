package com.example.administrator.lab6musicplayer;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView music_img;
    private TextView status;
    private Button play;
    private Button stop;
    private Button quit;
    private MusicService musicService;
    private SeekBar seekbar;
    private TextView music_time,total_time;
    private ObjectAnimator animator;
    //private int flag = 0;
    private boolean flag=false;
    private IBinder iBinder;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");                         //定义时间格式

    private void connection(){
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    public ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBinder = service;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    public Handler handler = new Handler();
    //更新 seekbar 的进度,在类中定义简单日期格式，用来显示播放的时间，用 time.format 来格式所需要的数据，用来监听进度条的滑动变化
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            total_time.setText(time.format(GetDuration_Position(103)));
            music_time.setText(time.format(GetDuration_Position(104)));           //获得当前播放进度,并格式化为时间格式
            seekbar.setMax(GetDuration_Position(103));                            //设置进度条最大数值
            seekbar.setProgress(GetDuration_Position(104));                        //设置进度条当前进度为音乐当前播放的位置
            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {   //实现改变拖动条后设置当前音乐播放进度为相应时间
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        Player_SeekBar(seekbar.getProgress()); //将当前音频播放位置设置为进度条的进度
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            handler.postDelayed(runnable, 200);//通知handler启动runnable
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicService  = new MusicService();
        findview();   //绑定控件
        BindButton();
        connection();
        setAnimator();  //设置并开启动画
    }

    public void findview(){
        music_img = (ImageView) findViewById(R.id.Img);
        status = (TextView) findViewById(R.id.status);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        quit = (Button) findViewById(R.id.quit);
        music_time = (TextView) findViewById(R.id.music_time);
        total_time = (TextView) findViewById(R.id.total_time);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
    }

    void BindButton(){
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        quit.setOnClickListener(this);
    }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.play:
                    MusicStart_Stop(101);
                    if(flag) {
                        status.setText("Pause");
                        play.setText("Play");
                        animator.pause();
                        flag=false;
                    }
                    else{
                        status.setText("Playing");
                        play.setText("Pause");
                        animator.resume();
                        flag = true;
                    }
                    handler.post(runnable);
                    break;
                case R.id.stop:
                    MusicStart_Stop(102); //新增
                    status.setText("Stopped");
                    play.setText("Play");
                    animator.end();
                    flag=false;
                    setAnimator();//缺少下次再点击play就没有动画效果
                    break;
                case R.id.quit:
                    unbindService(mConnection);//退出程序要解绑服务
                    try{
                        MainActivity.this.finish();//结束当前的时间
                        System.exit(0);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }

    private void MusicStart_Stop(int code){ //控制音乐播放或停止 101为播放/暂停 102为停止
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try{
            iBinder.transact(code,data,reply,0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int GetDuration_Position(int code){ //获得音乐的时长以及当前播放时间 103 为时长 104为当前时间
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try{
            iBinder.transact(code,data,reply,0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return reply.readInt();
    }

    private void Player_SeekBar(int position){ //设置播放器从给定时间点的音乐位置播放
        int code = 105;
        Parcel data = Parcel.obtain();
        data.writeInt(position);
        Parcel reply = Parcel.obtain();
        try{
            iBinder.transact(code,data,reply,0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setAnimator(){
        //第一个参数为 view对象，第二个参数为 动画改变的类型，第三，第四个参数依次是开始透明度和结束透明度。
        animator = new ObjectAnimator().ofFloat(music_img,"rotation",0,360);
        animator.setDuration(10000);//设置动画时间
        animator.setRepeatCount(-1);//设置动画重复次数，这里-1代表无限
        animator.setInterpolator(new LinearInterpolator());//设置动画插入器，匀速
        animator.start();  //先开始然后再暂停的原因是因为需要在第一次按下play按钮的时候开始动画,但之后的都需要设置为resume继续动画
        animator.pause();
    }

    //////点击返回键后台更新播放器UI
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}