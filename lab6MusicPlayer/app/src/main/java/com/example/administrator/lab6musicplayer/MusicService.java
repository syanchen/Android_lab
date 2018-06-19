package com.example.administrator.lab6musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer = new MediaPlayer();

    public MusicService() {
        try {
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() +"/bluetooth/melt.mp3");//绑定播放的歌曲
            mediaPlayer.prepare();//进入就绪状态
            mediaPlayer.setLooping(true);//设置循环播放
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final IBinder binder = new MyBinder();
    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch(code){
                case 101:
                    playOrPause();
                    break;
                case 102:
                    stop();
                    break;
                case 103://refresh UI, return time
                    reply.writeInt(mediaPlayer.getDuration());
                    break;
                case 104://as above
                    reply.writeInt(mediaPlayer.getCurrentPosition());
                    break;
                case 105://drag the process bar
                    mediaPlayer.seekTo(data.readInt());
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

    private void playOrPause(){
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else mediaPlayer.start();
    }

    private void stop(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() +"/bluetooth/melt.mp3");
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


}
