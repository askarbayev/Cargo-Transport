package com.example.askarbayev1.cargotrans;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

public class AvailableOrders extends AppCompatActivity {
    Database db;
    TruckOrdersAdapter adapter;
    int truck_user_id;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_available_orders);
        Intent intent = getIntent();
        status = intent.getIntExtra("status", -1);
        truck_user_id = intent.getIntExtra("user_id", -1);
        main(truck_user_id, status);
        getView();
    }

    public void getView() {
        db = new Database(getApplicationContext());
        ListView orderListView = findViewById(R.id.orderListView);
        List<Order> orderList = db.getAllOrders();
        adapter = new TruckOrdersAdapter(getApplicationContext(),orderList, truck_user_id);
        orderListView.setAdapter(adapter);

    }
    private void main(final int user_id, final int status) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_item2);
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
                                Intent intent1 = new Intent(getApplicationContext(), AvailableOrders.class);
                                intent1.putExtra("user_id", user_id);
                                intent1.putExtra("status", status);
                                startActivity(intent1);
                                break;
                            case R.id.action_item3:
                                Intent intent2 = new Intent(getApplicationContext(), TruckOwnerChart.class);
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
