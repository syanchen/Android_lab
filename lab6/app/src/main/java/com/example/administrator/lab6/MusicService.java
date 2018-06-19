package com.example.administrator.lab6;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

public class MusicService extends Service {
    public MediaPlayer mediaPlayer;

    public MusicService() {
        mediaPlayer=new MediaPlayer();
        try{
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() +"/bluetooth/melt.mp3");//绑定播放的歌曲
            mediaPlayer.prepare();//进入就绪状态
            mediaPlayer.setLooping(true);//设置循环播放
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //public MyBinder binder=new MyBinder();
    private final IBinder binder=new MyBinder();
    public class MyBinder extends Binder {
        MusicService getService(){
            return MusicService.this;//找到后台服务的指针，返回后台服务实例
        }
    }

    public void playOrPause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else{
            mediaPlayer.start();
        }
    }

    public void stop(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            try{
                mediaPlayer.reset();
                mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() +"/bluetooth/melt.mp3");
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();//停止歌曲播放
            mediaPlayer.release();//释放mediaPlayer资源
        }
    }
}
