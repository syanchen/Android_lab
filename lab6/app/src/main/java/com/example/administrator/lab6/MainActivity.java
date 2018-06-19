package com.example.administrator.lab6;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //
    Button play,stop,quit;
    TextView mstatus,mplay,mfinish;
    SeekBar seekBar;
    MusicService musicService;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private ImageView imageview;
    private boolean flag=true;
    private boolean flag2 = false;
    ObjectAnimator animator;

    ////////////////////activity启动时绑定service
    public void connection(){
        Intent intent=new Intent(this,MusicService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService=((MusicService.MyBinder)service).getService();
            mfinish.setText(time.format(musicService.mediaPlayer.getDuration()));
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mConnection=null;
        }
    };

    //更新 seekbar 的进度,在类中定义简单日期格式，用来显示播放的时间，用 time.format 来格式所需要的数据，用来监听进度条的滑动变化
    public Handler handler=new Handler();
    public Runnable runnable=new Runnable() {
        @Override
        public void run() {
            mplay.setText(time.format(musicService.mediaPlayer.getCurrentPosition()));
            seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
            seekBar.setMax(musicService.mediaPlayer.getDuration());
            mfinish.setText(time.format(musicService.mediaPlayer.getDuration()));
            handler.postDelayed(runnable, 200);//通知handler启动runnable
        }
    };

    public void findview(){
        play=(Button)findViewById(R.id.PLAY);
        stop=(Button)findViewById(R.id.Stop);
        quit=(Button)findViewById(R.id.Quit);
        mplay=(TextView)findViewById(R.id.MusicTime);
        mstatus=(TextView)findViewById(R.id.MusicStatus);
        mfinish=(TextView)findViewById(R.id.MusicTotal);
        seekBar=(SeekBar)findViewById(R.id.MusicSeekBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();
        connection();
        bindButton();

        imageview = (ImageView) findViewById(R.id.Image);
        animator = ObjectAnimator.ofFloat(imageview, "rotation", 0f, 360.0f);//对象，属性名，
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser == true) {
                    musicService.mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void bindButton(){
        /** 为按钮添加监听 ，三个按钮的监听器都是MainActivity*/
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        quit.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.PLAY://点击play_pause按钮事件
                if(flag){
                        musicService.playOrPause();
                        animator.start();
                        mstatus.setText("Playing");//当音乐在播放时，我们设置state的内容为Playing
                        play.setText("PAUSE");//设置button的内容为Puase
                        flag=false;
                }
                else{
                    if(musicService.mediaPlayer.isPlaying()){
                        musicService.playOrPause();//调用musicService中的playORpause函数，暂停或开始音乐播放
                        mstatus.setText("Paused");//当音乐暂停时，设置state内容为Paused
                        play.setText("PLAY");//button内容为Play
                        animator.pause();
                    }
                    else{
                        musicService.playOrPause();
                        mstatus.setText("Playing");//当音乐在播放时，我们设置state的内容为Playing
                        play.setText("PAUSE");//设置button的内容为Puase
                        animator.resume();
                    }
                }
                if (flag2 == false) {
                    handler.post(runnable);
                    flag2 = true;
                }
                break;
            case R.id.Stop://点击停止
                musicService.stop();//关闭音乐
                play.setText("PLAY");//设置开始和暂停按钮的内容为Play
                mstatus.setText("Stop");//设置state为Stop
                animator.end();
                flag=true;
                break;
            case R.id.Quit://点击退出
                handler.removeCallbacks(runnable);//回调mRunnable接口
                unbindService(mConnection);//解除后台服务的绑定
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
