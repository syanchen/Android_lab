package com.example.administrator.lab3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    private final String STATICACTION="STATIC";
    private final String DYNAMICACTION="DYNAMIC";
    private String name;
    private String info;
    private String price;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);          //实例化RemoteView,其对应相应的Widget布局
        Intent i=new Intent(context,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget,pi);//设置点击事件
        ComponentName me=new ComponentName(context,NewAppWidget.class);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(me, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context,Intent intent){
        super.onReceive(context,intent);
        if(intent.getAction().equals(STATICACTION)){
            name=intent.getStringExtra("name");
            info=intent.getStringExtra("info");
            price=intent.getStringExtra("price");
            int image=Image.getIm(name);

            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);//对界面进行刷新
            Intent i=new Intent(context,Detail.class);
            i.addCategory(Intent.CATEGORY_DEFAULT);                                   //使用addCategory()方法为Intent对象添加Category属性,匹配了默认的Category

            PendingIntent pi=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setTextViewText(R.id.itemname,name+"仅售"+price+"!");
            contentView.setImageViewResource(R.id.itemmage,image);
            contentView.setOnClickPendingIntent(R.id.widget,pi);                                              //到底这个widget是点击的部件

            i.putExtra("Name",name);
            i.putExtra("Price",price);
            i.putExtra("Info",info);

            ComponentName me =new ComponentName(context,NewAppWidget.class);//receiver
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me,contentView);
        }
        else if(intent.getAction().equals(DYNAMICACTION)){
            name=intent.getStringExtra("name");
            RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            int image=Image.getIm(name);

            Intent i=new Intent(context,MainActivity.class);
            i.addCategory(Intent.CATEGORY_DEFAULT);

            PendingIntent pi=PendingIntent.getActivity(context,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            contentView.setTextViewText(R.id.itemname,name+"已添加到购物车");
            contentView.setImageViewResource(R.id.itemmage,image);
            contentView.setOnClickPendingIntent(R.id.widget,pi);
            ComponentName me =new ComponentName(context,NewAppWidget.class);
            AppWidgetManager appWidgetManager=AppWidgetManager.getInstance(context);
            appWidgetManager.updateAppWidget(me,contentView);
        }

    }
}

