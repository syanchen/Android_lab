package com.example.administrator.lab3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyReceiver extends BroadcastReceiver {

    private final String STATICACTION="STATIC";
    private final String DYNAMICACTION="DYNAMIC";
    private NotificationManager mNotificationManager;
    private String name;
    private String info;
    private String price;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(STATICACTION)){

            name=intent.getStringExtra("name");
            info=intent.getStringExtra("info");
            price=intent.getStringExtra("price");

            int image=Image.getIm(name);
            //第一个参数是包含你要加载的位图资源文件的对象(一般写成 getResources()就ok了);第二个时你需要加载的位图资源的Id。进行缩小
            Bitmap bm= BitmapFactory.decodeResource(context.getResources(),image);

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

                    mBuilder.setContentTitle("新商品热卖")//设置通知栏标题
                            .setContentText(name+"仅售"+price) //设置通知栏显示内容
                            .setAutoCancel(true)
                            .setLargeIcon(bm)
                            .setTicker("您有一条新消息")
                            .setSmallIcon(image);//设置通知小ICON

            Intent mIntent=new Intent(context,Detail.class);//点击之后打开的activity
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            /////////////////////////////修改这里后能成功跳转到商品详情mIntent.putExtra（intent.getExtra()）
            mIntent.putExtra("Name",name);
            mIntent.putExtra("Price",price);
            mIntent.putExtra("Info",info);

            PendingIntent mPendingIntent=PendingIntent.getActivity(context,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(mPendingIntent);

            //绑定Notification,发送通知请求
            Notification notification = mBuilder.build();
            //mNotificationManager.notify(num++,notification)可以点击购物车出现多次马上下单
            mNotificationManager.notify(0,notification);
        }

        else if(intent.getAction().equals(DYNAMICACTION)){
            name=intent.getStringExtra("name");
            int image=Image.getIm(name);
            Bitmap bm= BitmapFactory.decodeResource(context.getResources(),image);

            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            mBuilder.setContentTitle("马上下单")//设置通知栏标题
                    .setContentText(name+"已添加到购物车") //设置通知栏显示内容
                    .setAutoCancel(true)
                    .setLargeIcon(bm)
                    .setTicker("您有一条新消息")
                    .setSmallIcon(image);//设置通知小ICON

            Intent mIntent=new Intent(context,MainActivity.class);//点击之后打开的activity

            PendingIntent mPendingIntent=PendingIntent.getActivity(context,1,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);//将0改成了1
            mBuilder.setContentIntent(mPendingIntent);

            //绑定Notification,发送通知请求
            Notification notification = mBuilder.build();
            mNotificationManager.notify(1,notification);//第一个参数唯一的标识该Notification，第二个参数就是Notification对象
            }
        }
}
