package com.dehe.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lenovo on 2017/12/11.
 */

public class myDB extends SQLiteOpenHelper {
    private static final String DB_NAME="Contacts.db";
    private static final String TABLE_NAME="Contacts";
    private static final int DB_VERSION=1;
    private Context mContext;
    public myDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    public void onCreate(SQLiteDatabase db){
        String CREATE_TABLE="create table "+TABLE_NAME
                +" (name text primary key , "
                +"birth text , "
                +"gift text);";
        db.execSQL(CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
    public void insert(String name,String birth,String gift){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    public void update(String name,String birth,String gift){
        SQLiteDatabase db=getWritableDatabase();
        String whereClause="name=?";
        String[] whereArgs={ name };
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("birth",birth);
        values.put("gift",gift);
        db.update(TABLE_NAME,values,whereClause,whereArgs);
        db.close();
    }
    public  void delete(String name){
        SQLiteDatabase db=getWritableDatabase();
        String whereClause="name=?";
        String[] whereArgs={ name };
        db.delete(TABLE_NAME,whereClause,whereArgs);
        db.close();
    }
    public Cursor query(){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME+";",null);
        return cursor;
    }
    public String getBirth(String name){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME,null,"name=?",new String[]{name},null,null,null);
        String res="无记录";
        while(cursor.moveToNext()){
            res=cursor.getString(1);
        }
        return res;
    }
    public String getGift(String name){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME,null,"name=?",new String[]{name},null,null,null);
        String res="无记录";
        if(cursor.moveToFirst()){
            res=cursor.getString(2);
        }
        cursor.close();
        return res;
    }
}

