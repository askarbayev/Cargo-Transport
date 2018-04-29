package com.example.askarbayev1.cargotrans;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button individual, truck_owner;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        db = new Database(getApplicationContext());
        individual = (Button) findViewById(R.id.individual);
        truck_owner = (Button) findViewById(R.id.truck_owner);
        db.getUsers();
        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user_id = db.checkLogin(1);
                if (user_id ==- 1){
                    Intent intent =new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("status", 1);
                    startActivity(intent);
                }
                else {
                    Intent intent =new Intent(getApplicationContext(), AvailableRequestActivity.class);
                    intent.putExtra("status", 1);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }

            }
        });

        truck_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("truck_owner", 2+"");
                int user_id = db.checkLogin(2);
                if (user_id == -1){
                    Intent intent =new Intent(getApplicationContext(), LoginActivity.class);
                    intent.putExtra("status", 2);
                    startActivity(intent);
                }
                else {
                    Intent intent =new Intent(getApplicationContext(), AvailableOrders.class);
                    intent.putExtra("status", 2);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            }
        });
    }
}
