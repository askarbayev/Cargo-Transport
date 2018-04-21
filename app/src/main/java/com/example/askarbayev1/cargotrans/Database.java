package com.example.askarbayev1.cargotrans;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CargoTrans.db";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table user " +
                        "(id INTEGER primary key, first_name TEXT, last_name TEXT, email TEXT, phone TEXT, username TEXT, password TEXT, status INTEGER, sign INTEGER, DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
    public boolean register(String first_name, String last_name, String email, String phone, String username, String password, int status, int sign){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", first_name);
        contentValues.put("last_name", last_name);
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("status", status);
        contentValues.put("sign", sign);
        db.insert("user", null, contentValues);
        return true;
    }
    public int signIn(String username, String password, int status){
        Log.d(username, password+" - status - "+status);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from user where status = ? and username =? and password =? ", new String[]{Integer.toString(status), username, password});
        res.moveToFirst();
        int id = -1;
        while(res.isAfterLast() == false){
            id = res.getInt(res.getColumnIndex("id"));
            Log.d("RES ID:", id+"");
            res.moveToNext();
        }
        if (id != -1){
            boolean state_res = statusUpdate(id);
            if (state_res){
                return id;
            }
        }
        return -1;

    }

    public boolean statusUpdate(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sign", 1);
        db.update("user", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public int checkLogin(int status){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from user where sign = "+1+" and status = "+status, null);
        res.moveToFirst();
        int id = -1;
        while(res.isAfterLast() == false){
            id = res.getInt(res.getColumnIndex("id"));
            res.moveToNext();
        }
        return id;
    }
    public Map<String, String> getUserInfo(int user_id, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from user where id=? and status=?", new String[]{Integer.toString(user_id), Integer.toString(status)});
        res.moveToFirst();
        Map<String, String> info = new HashMap<>();
        while (res.isAfterLast() == false){
            String fname = res.getString(res.getColumnIndex("first_name"));
            String lname = res.getString(res.getColumnIndex("last_name"));
            String email = res.getString(res.getColumnIndex("email"));
            String phone = res.getString(res.getColumnIndex("phone"));
            String username = res.getString(res.getColumnIndex("username"));
            String password = res.getString(res.getColumnIndex("password"));
            String status_val = res.getString(res.getColumnIndex("status"));
            String sign = res.getString(res.getColumnIndex("sign"));
            info.put("name", fname+" "+lname);
            info.put("email", email);
            info.put("phone", phone);
            info.put("username", username);
            res.moveToNext();
        }
        return info;
    }
    public void getUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from user", null);
        res.moveToFirst();
        while(res.isAfterLast() == false){
            int id = res.getInt(res.getColumnIndex("id"));
            String fname = res.getString(res.getColumnIndex("first_name"));
            String lname = res.getString(res.getColumnIndex("last_name"));
            String email = res.getString(res.getColumnIndex("email"));
            String phone = res.getString(res.getColumnIndex("phone"));
            String username = res.getString(res.getColumnIndex("username"));
            String password = res.getString(res.getColumnIndex("password"));
            String status = res.getString(res.getColumnIndex("status"));
            String sign = res.getString(res.getColumnIndex("sign"));

            Log.d(""+id, fname+" - "+lname+" - "+email+" - "+phone+" - "+username+" - "+password+" - "+status+" - "+sign);

            res.moveToNext();
        }
    }

}

