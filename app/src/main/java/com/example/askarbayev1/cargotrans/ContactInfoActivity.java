package com.example.askarbayev1.cargotrans;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.Map;

public class ContactInfoActivity extends AppCompatActivity {
    CustomListAdapter adapter;
    LinkedList<String> contactList;
    Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        Log.d("Contact", "Info");
        db = new Database(getApplicationContext());
        Intent intent = getIntent();
        int status = intent.getIntExtra("status", -1);
        int user_id = intent.getIntExtra("user_id", -1);
        main(user_id, status);
        loadContactList(user_id, status);
    }

    private void loadContactList(int user_id, int status) {
        ListView listView = (ListView) findViewById(R.id.contactlist);
        Log.d("status", status+"");
        Log.d("user_id", user_id+"");
        Map<String, String> info = db.getUserInfo(user_id, status);
        Log.d("HashMap", info+"");
        contactList = new LinkedList<>();
        contactList.add("username");

        adapter = new CustomListAdapter(getApplicationContext(), info, true);
        listView.setAdapter(adapter);
    }
    private void main(final int user_id, final int status) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_item1);
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
                                if (status==1){
                                    Intent intent1 = new Intent(getApplicationContext(), AvailableRequestActivity.class);
                                    intent1.putExtra("user_id", user_id);
                                    intent1.putExtra("status", status);
                                    startActivity(intent1);
                                }
                                else {
                                    Intent intent1 = new Intent(getApplicationContext(), AvailableOrders.class);
                                    intent1.putExtra("user_id", user_id);
                                    intent1.putExtra("status", status);
                                    startActivity(intent1);
                                }

                                break;
                            case R.id.action_item3:
                                if (status!=2){
                                    Intent intent2 = new Intent(getApplicationContext(), OrdersActivity.class);
                                    intent2.putExtra("user_id", user_id);
                                    intent2.putExtra("status", status);
                                    startActivity(intent2);
                                }
                                break;
                        }
                        return true;
                    }
                });

    }


}
