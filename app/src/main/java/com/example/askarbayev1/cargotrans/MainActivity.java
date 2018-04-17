package com.example.askarbayev1.cargotrans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button individual, truck_owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        individual = (Button) findViewById(R.id.individual);
        truck_owner = (Button) findViewById(R.id.truck_owner);
        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("status", 1);
                startActivity(intent);
            }
        });

        truck_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("status", 2);
                startActivity(intent);
            }
        });
    }
}
