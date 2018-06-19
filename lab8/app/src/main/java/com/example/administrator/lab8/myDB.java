package com.example.administrator.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.cursorVisible;
import static android.R.attr.id;
import static android.R.attr.version;

/**
 * Created by Administrator on 2017/12/12.
 */

public class myDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;

    public myDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

//    public myDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " +
                TABLE_NAME + " (_id integer primary key , "
                + "name text , " + "birth text , "
                + "gift text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insert(String name, String birth, String gift) {//增
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.insert(TABLE_NAME, null, values);
        Cursor cursor = db.rawQuery("select last_insert_rowid() from"+TABLE_NAME, null);
        int new_id = -1;
        if(cursor.moveToFirst()){
            new_id = cursor.getInt(0);
        }
        db.close();
        return new_id;
    }


    public void update(Integer id, String name, String birth, String gift){//改
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String  whereClause = "_id = ?";
        String[] whereArgs = {id.toString()};
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public Map<String, Object> query(Integer id){//查
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String [] whereArgs = {id.toString()};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"_id", "name", "birth", "gift"}, whereClause, whereArgs, null, null, null);
        ArrayList<Map<String, Object>> resultList = PutData(cursor);
        Map<String, Object> result = resultList.get(0);//get Id
        cursor.close();
        db.close();
        return result;
    }

    public void delete(Integer id){//删
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String [] whereArgs = {id.toString()};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }


    public List<Map<String, Object>> generateList(){//生成列表
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Map<String, Object>> resultList = PutData(c);
        c.close();
        db.close();
        return resultList;
    }

    public boolean IsNameExist(String name){//判断名字是否重复
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = {name};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"name"},
                whereClause, whereArgs, null, null, null);
        boolean result=false;
        if(cursor.getCount()>0){
            result=true;
        }
        db.close();
        cursor.close();
        return result;
    }

    private ArrayList<Map<String, Object>> PutData(Cursor cursor){//数据放入列表
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id1=cursor.getInt(cursor.getColumnIndex("_id"));
                String name1 = cursor.getString(cursor.getColumnIndex("name"));
                String birth1 = cursor.getString(cursor.getColumnIndex("birth"));
                String gift1 = cursor.getString(cursor.getColumnIndex("gift"));
                Map<String, Object> map = new HashMap<>();
                map.put("id", id1);
                map.put("name", name1);
                map.put("birth", birth1);
                map.put("gift", gift1);
                resultList .add(map);
            }while(cursor.moveToNext());//要先判断是否为0，否则是做了空操作
        }
        return resultList;
    }

}
