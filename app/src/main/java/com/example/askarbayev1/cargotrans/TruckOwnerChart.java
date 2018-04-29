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

public class TruckOwnerChart extends AppCompatActivity {
    int truck_user_id;
    int status;
    Database db;
    ChartAdpater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_owner_chart);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        status = intent.getIntExtra("status", -1);
        truck_user_id = intent.getIntExtra("user_id", -1);
        main(truck_user_id, status);
        getView();
    }

    public void getView() {
        db = new Database(getApplicationContext());
        ListView orderListView = findViewById(R.id.chartList);
        List<Order> orderList = db.getTruckOwnerChart(truck_user_id);
        adapter = new ChartAdpater(getApplicationContext(),orderList, truck_user_id);
        orderListView.setAdapter(adapter);

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

    }
}
