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
import java.util.List;
import java.util.Map;

public class Database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "CargoTrans.db";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, 7);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table user " +
                        "(id INTEGER primary key, first_name TEXT, last_name TEXT, email TEXT, phone TEXT, username TEXT, password TEXT, status INTEGER, sign INTEGER, DATETIME DEFAULT CURRENT_TIMESTAMP)"
        );

        db.execSQL(
                "create table orders " +
                        "(id INTEGER primary key, user_id INTEGER, status INTEGER, " +
                        "location TEXT, destination TEXT, description TEXT, height TEXT, " +
                        "width TEXT, length TEXT, weight TEXT, " +
                        "budget REAL, pickup_date TEXT, dropoff_date TEXT, loc_latitude TEXT," +
                        "loc_longitude TEXT, dest_latitude TEXT, dest_longitude, DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY(user_id) REFERENCES user(id))"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS orders");
        onCreate(db);
    }
    public boolean insertOrder(int user_id, int status, String location, String destination, String description,
                            String height, String width, String length, String weight, double budget, String pickup_date,
                            String dropoff_date, String loc_latitude, String loc_longitude, String dest_latitude,
                            String dest_longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", user_id);
        contentValues.put("status", status);
        contentValues.put("location", location);
        contentValues.put("destination", destination);
        contentValues.put("description", description);
        contentValues.put("height", height);
        contentValues.put("width", width);
        contentValues.put("length", length);
        contentValues.put("weight", weight);
        contentValues.put("budget", budget);
        contentValues.put("pickup_date", pickup_date);
        contentValues.put("dropoff_date", dropoff_date);
        contentValues.put("loc_latitude", loc_latitude);
        contentValues.put("loc_longitude", loc_longitude);
        contentValues.put("dest_latitude", dest_latitude);
        contentValues.put("dest_longitude", dest_longitude);
        db.insert("orders", null, contentValues);
        return true;
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

    public List<Order> getUserOrders(int user_id){
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from orders where user_id=?", new String[]{Integer.toString(user_id)});
        res.moveToFirst();
        while (res.isAfterLast() == false){
            int id = res.getInt(res.getColumnIndex("id"));
            user_id = res.getInt(res.getColumnIndex("user_id"));
            int status = res.getInt(res.getColumnIndex("status"));
            String location = res.getString(res.getColumnIndex("location"));
            String destination = res.getString(res.getColumnIndex("destination"));
            String description = res.getString(res.getColumnIndex("description"));
            String height = res.getString(res.getColumnIndex("height"));
            String width = res.getString(res.getColumnIndex("width"));
            String length = res.getString(res.getColumnIndex("length"));
            String weight = res.getString(res.getColumnIndex("weight"));
            double budget = res.getDouble(res.getColumnIndex("budget"));
            String pickup_date = res.getString(res.getColumnIndex("pickup_date"));
            String dropoff_date = res.getString(res.getColumnIndex("dropoff_date"));
            String loc_latitude = res.getString(res.getColumnIndex("loc_latitude"));
            String loc_longitude = res.getString(res.getColumnIndex("loc_longitude"));
            String dest_latitude = res.getString(res.getColumnIndex("dest_latitude"));
            String dest_longitude = res.getString(res.getColumnIndex("dest_longitude"));
            orders.add(new Order(id, user_id, status, location, destination, description, height, width,
                    length, weight, budget, pickup_date, dropoff_date, loc_latitude, loc_longitude,
                    dest_latitude, dest_longitude));
            res.moveToNext();
        }
        return orders;
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
            info.put("name:  ", fname+" "+lname);
            info.put("email:  ", email);
            info.put("phone:  ", phone);
            info.put("username:  ", username);
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

