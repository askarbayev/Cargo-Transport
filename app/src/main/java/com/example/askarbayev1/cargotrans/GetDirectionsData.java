package com.example.askarbayev1.cargotrans;

import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;

public class GetDirectionsData extends AsyncTask<Object, String, String> {
    GoogleMap googleMap;
    String url;
    String googleDirectionsData;
    String duration, distance;
    LatLng latLng;
    Button button;
    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap)objects[0];
        url = (String) objects[1];
        latLng = (LatLng) objects[2];
        button = (Button) objects[3];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readURl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        HashMap<String, String> directionsList = null;
        DataParser dataParser = new DataParser();
        directionsList = dataParser.parseDirections(s);
        duration = directionsList.get("duration");
        distance = directionsList.get("distance");
        button.setText("Duration: "+duration+"\n"+"Distance: "+distance);

        String [] directionsPath = dataParser.parsePaths(s);
        displayDirections(directionsPath);
    }

    private void displayDirections(String[] directionsPath) {
        int count = directionsPath.length;

        for (int i = 0; i<count; i++){
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLACK);
            options.width(20);
            options.addAll(PolyUtil.decode(directionsPath[i]));
            googleMap.addPolyline(options);
        }
    }
}
