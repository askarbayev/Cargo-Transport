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

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CargoTrans.db";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 5);
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
    public boolean signIn(String username, String password, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery("select * from user where status = "+status+" and username like '"+username+"' and password like '"+password+"'", null);
        res.moveToFirst();
        int id = -1;
        while(res.isAfterLast() == false){
            id = res.getInt(res.getColumnIndex("id"));
            res.moveToNext();
        }
        if (id != -1){
            if (statusUpdate(id)){
                return true;
            }
        }
        return false;

    }

    public boolean statusUpdate(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 1);
        db.update("user", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

}

