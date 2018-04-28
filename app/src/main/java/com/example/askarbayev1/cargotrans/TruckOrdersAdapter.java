package com.example.askarbayev1.cargotrans;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TruckOrdersAdapter extends BaseAdapter {
    Database db;
    List<Order> orders;
    private Context context;
    private LayoutInflater inflater;
    private int truck_user_id;
    TruckOrdersAdapter(Context context, List<Order> orders, int truck_user_id){
        this.orders = orders;
        this.context = context;
        this.truck_user_id = truck_user_id;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.truck_orders_list, parent, false);
        }
        db = new Database(context);
        final Order order = (Order) this.getItem(position);
        TextView title = view.findViewById(R.id.title);
        TextView height = view.findViewById(R.id.height);
        TextView width = view.findViewById(R.id.width);
        TextView length = view.findViewById(R.id.length);
        TextView weight = view.findViewById(R.id.weight);
        TextView pickup_date = view.findViewById(R.id.pickup_date);
        TextView drop_date = view.findViewById(R.id.drop_date);
        TextView from_location = view.findViewById(R.id.from_location);
        TextView to_destination = view.findViewById(R.id.to_destination);
        TextView username = view.findViewById(R.id.username);
        Button acceptOrder = view.findViewById(R.id.acceptOrder);

        buttonListener(acceptOrder, order.getId(), order.getUser_id());

        title.setText(order.getDescription());
        height.setText("h - "+order.getHeight()+", ");
        length.setText("len - "+order.getLength()+" ");
        width.setText("w - "+order.getWidth() +", ");
        pickup_date.setText(order.getPickup_date());
        drop_date.setText(order.getDropoff_date());
        weight.setText(order.getWeight());
        Log.d("order.getId", order.getId()+"");
        String location [] = order.getLocation().split(",");
        String destination [] = order.getDestination().split(",");
        if (location.length == 5){
            location = Arrays.copyOfRange(location, 1, 4);
        }
        else if (location.length == 4){
            location = Arrays.copyOfRange(location, 0, 3);
        }
        if (destination.length == 5){
            destination = Arrays.copyOfRange(destination, 1, 4);
        }
        else if (destination.length == 4){
            destination = Arrays.copyOfRange(destination, 0, 3);
        }
        StringBuilder builder_location = new StringBuilder();
        for (int i=0; i<location.length; i++){
            if (i!=location.length-1){
                builder_location.append(location[i]+", ");
            }
            else {
                builder_location.append(location[i]);
            }
        }
        StringBuilder builder_destination = new StringBuilder();
        for (int i=0; i<destination.length; i++){
            if (i!=destination.length-1){
                builder_destination.append(destination[i]+", ");
            }
            else {
                builder_destination.append(destination[i]);
            }
        }
        from_location.setText(builder_location.toString());
        to_destination.setText(builder_destination.toString());

        HashMap<String, String> userInfo = (HashMap<String, String>) db.getUserInfo(order.getUser_id(), order.getStatus());
        Log.d("userInfo", userInfo+"");
        String username_val = null;
        int i = 1;
        for ( Map.Entry<String, String> entry : userInfo.entrySet()) {
            String key = entry.getKey();
            Log.d("key", key);
            Log.d("value", entry.getValue());
            if (i == 4){
                username_val = entry.getValue();
            }
            i++;


        }
        username.setText(username_val);

        from_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, Direction.class);
                Log.d("start_latitude", Double.valueOf(order.getLoc_latitude())+"");
                Log.d("start_longitude", Double.valueOf(order.getLoc_longitude())+"");
                Log.d("end_latitude", Double.valueOf(order.getDest_latitude())+"");
                Log.d("end_longitude", Double.valueOf(order.getDest_longitude())+"");
                intent.putExtra("start_latitude", Double.valueOf(order.getLoc_latitude()));
                intent.putExtra("start_longitude", Double.valueOf(order.getLoc_longitude()));
                intent.putExtra("end_latitude", Double.valueOf(order.getDest_latitude()));
                intent.putExtra("end_longitude", Double.valueOf(order.getDest_longitude()));
                context.startActivity(intent);
            }
        });
        to_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context, Direction.class);
                Log.d("start_latitude", Double.valueOf(order.getLoc_latitude())+"");
                Log.d("start_longitude", Double.valueOf(order.getLoc_longitude())+"");
                Log.d("end_latitude", Double.valueOf(order.getDest_latitude())+"");
                Log.d("end_longitude", Double.valueOf(order.getDest_longitude())+"");
                intent.putExtra("start_latitude", Double.valueOf(order.getLoc_latitude()));
                intent.putExtra("start_longitude", Double.valueOf(order.getLoc_longitude()));
                intent.putExtra("end_latitude", Double.valueOf(order.getDest_latitude()));
                intent.putExtra("end_longitude", Double.valueOf(order.getDest_longitude()));
                context.startActivity(intent);
            }
        });


        return view;
    }

    private void buttonListener(Button acceptOrder, final int order_id, final int indv_user_id) {
        db = new Database(context);
        acceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean res = db.insertTrade(order_id, truck_user_id, indv_user_id);
                if (res){
                    Toast.makeText(context, "your request succesfully added to your chart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

