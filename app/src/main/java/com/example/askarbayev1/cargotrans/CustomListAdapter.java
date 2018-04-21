package com.example.askarbayev1.cargotrans;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private Map<String, String> info;
    private LayoutInflater inflater;
    TextView type, value;
    private boolean isListView;
    String[] keys;

    CustomListAdapter(Context context, Map<String, String> info, boolean isListView){
        this.context = context;
        this.info = info;
        this.keys = info.keySet().toArray(new String[info.size()]);
        this.isListView = isListView;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.contact_list_item, parent, false);
        if (view == null) {
            if(isListView)
                view = inflater.inflate(R.layout.contact_list_item, parent, false);
        }
        Log.d("Custom List Adapter", "dapter");
        type = (TextView) view.findViewById(R.id.type);
        value = (TextView) view.findViewById(R.id.value);
        String key = keys[i];
        String val = info.get(key);
        type.setText(key);
        value.setText(val);

        return view;
    }
}
