package com.example.askarbayev1.cargotrans;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    Database db;
    FloatingActionButton add;
    OrderListAdapter adapter;
    int user_id;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_orders);
        Intent intent = getIntent();
        status = intent.getIntExtra("status", -1);
        user_id = intent.getIntExtra("user_id", -1);
        main(user_id, status);
        initialElements();
        getView(user_id, status);
    }

    private void initialElements() {

        add = findViewById(R.id.fab);
        add.setImageResource(R.drawable.add_circle2);
    }

    public void getView(final int user_id, final int status){
        db = new Database(getApplicationContext());
        ListView orderListView = findViewById(R.id.orderListView);
        List<Order> orderList = db.getUserOrders(user_id);
        adapter = new OrderListAdapter(getApplicationContext(),orderList);
        orderListView.setAdapter(adapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddOrderActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("status", status);
                startActivity(intent);
            }
        });
    }

    private void main(final int user_id, final int status) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_item3);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                Intent intent = new Intent(getApplicationContext(), ContactInfoActivity.class);
                                intent.putExtra("user_id", user_id);
                                intent.putExtra("status", status);
                                startActivity(intent);
                                break;
                            case R.id.action_item2:
                                Intent intent1 = new Intent(getApplicationContext(), AvailableRequestActivity.class);
                                intent1.putExtra("user_id", user_id);
                                intent1.putExtra("status", status);
                                startActivity(intent1);
                                break;
                            case R.id.action_item3:
                                Intent intent2 = new Intent(getApplicationContext(), OrdersActivity.class);
                                intent2.putExtra("user_id", user_id);
                                intent2.putExtra("status", status);
                                startActivity(intent2);
                                break;
                        }
                        return true;
                    }
                });
        //Intent intent1 = new Intent(getApplicationContext(), AvailableRequestActivity.class);
        //intent1.putExtra("user_id", user_id);
        //intent1.putExtra("status", status);

    }
}
