package com.example.askarbayev1.cargotrans;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Direction extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    Location currentLocation;
    View view;
    Button hideShow, bt_show;
    Button pickup, drop;
    EditText location, destination, description, height, width, length, weight, budget;
    private int year, month, day;
    private Calendar calendar;
    boolean isShow = false;
    int fillText = -1;
    Map<String, String> orderInfo;
    TextView pickUp_text, dropOff_text;
    Database db;
    double start_latitude;
    double start_longitude;
    double end_latitude;
    double end_longitude;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        start_latitude = intent.getDoubleExtra("start_latitude", 0);
        start_longitude = intent.getDoubleExtra("start_longitude", 0);
        end_latitude = intent.getDoubleExtra("end_latitude", 0);
        end_longitude = intent.getDoubleExtra("end_longitude", 0);
        if (googlePlayAvailable()) {
            setContentView(R.layout.activity_direction);
            getView();
        }

    }

    public void getView() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googlePlayAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvalible = api.isGooglePlayServicesAvailable(this);
        if (isAvalible == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvalible)) {
            Dialog dialog = api.getErrorDialog(this, isAvalible, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "can't connect to google play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    LocationRequest locationRequest;

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (googleMap!=null){
            getMarkers();
            float[] results = new float[10];
            Location.distanceBetween(start_latitude, start_longitude, end_latitude, end_longitude, results);
            Button info = findViewById(R.id.info);
            //info.setText(String.valueOf(results[0]));
            Object[] dataTransfer  = new Object[4];
            String url = getDirectionsUrl();
            GetDirectionsData getDirectionsData = new GetDirectionsData();
            dataTransfer[0] = googleMap;
            dataTransfer[1] = url;
            dataTransfer[2] = new LatLng(end_latitude, end_longitude);
            dataTransfer[3] = info;
            getDirectionsData.execute(dataTransfer);

            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.marker_window, null);
                    TextView locality = v.findViewById(R.id.locality);
                    TextView longitute = v.findViewById(R.id.longitute);
                    TextView logitute = v.findViewById(R.id.logitute);
                    TextView snippet = v.findViewById(R.id.snippet);

                    LatLng ll = marker.getPosition();
                    locality.setText(marker.getTitle());
                    longitute.setText("Latitude: " + ll.latitude);
                    logitute.setText("Longitude: " + ll.longitude);
                    if (fillText == 2){
                        snippet.setText("Drop off location");
                    }
                    else {
                        snippet.setText(marker.getSnippet());
                    }

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(ll.latitude, ll.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addresses != null && addresses.size() > 0 ){
                        Address address = addresses.get(0);
                        String addressInfo = address.getAddressLine(0);
                    }


                    return v;
                }
            });

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,}, 101);
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

    }

    public String getDirectionsUrl(){
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin="+start_latitude+","+start_longitude);
        googleDirectionsUrl.append("&destination="+end_latitude+","+end_longitude);
        googleDirectionsUrl.append("&key="+"AIzaSyBIXdKamfgSSi3f4BAe9McLsxAz-rdrhb8");
        return googleDirectionsUrl.toString();
    }


    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        googleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        Log.d("LAT", lat+"");
        Log.d("LNG", lng+"");
        LatLng ll = new LatLng(lat, lng);
        Log.d("ll", ll+"");
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        Log.d("update", update+"");
        googleMap.moveCamera(update);
    }

    Marker marker;
    public void findLocation(String location) throws IOException {
        if (location.length()>0){
            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList = geocoder.getFromLocationName(location, 2);
            Address address = addressList.get(0);
            String locality = address.getLocality();
            double lat = address.getLatitude();
            double lng = address.getLongitude();
            String adminArea = address.getAdminArea();
            String postalCode = address.getPostalCode();
            String strinRep = address.toString();
            Log.d("stringRep", strinRep);
            goToLocationZoom(lat, lng, 15);

            setMarker(locality, lat, lng, "");
        }

    }
    public void getMarkers() {
        try {
            getStartMarker(start_latitude, start_longitude);
            getEndtMarker(end_latitude, end_longitude);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getStartMarker(double start_latitude, double start_longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this);
        Log.d("start_latitude", start_latitude+"");
        Log.d("start_longitude", start_longitude+"");
        List<Address> addressList = geocoder.getFromLocation(start_latitude, start_longitude, 1);
        Address address = addressList.get(0);
        String locality = address.getLocality();
        double lat = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationZoom(start_latitude, start_longitude, 5);

        setMarker(locality, lat, lng, "Pick up location");

    }
    public void getEndtMarker(double end_latitude, double end_longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this);
        Log.d("end_latitude", end_latitude+"");
        Log.d("end_longitude", end_longitude+"");
        List<Address> addressList = geocoder.getFromLocation(end_latitude, end_longitude, 1);
        Address address = addressList.get(0);
        String locality = address.getLocality();
        double lat = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationZoom(end_latitude, end_longitude, 5);

        setMarker(locality, lat, lng, "Drop off destination");

    }

    private void setMarker(String locality, double lat, double lng, String pickOrDrop) {
        if (marker!=null){
            //marker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .position(new LatLng(lat, lng))
                .draggable(true)
                .snippet(pickOrDrop);
        marker = googleMap.addMarker(options);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);

        }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,}, 101);
        }

    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location == null){
            Toast.makeText(this, "can't get current location", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "your current current location", Toast.LENGTH_SHORT).show();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            googleMap.animateCamera(update);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void setPickUpdate(View view) {
        DialogFragment newFragment = new DatePickerFragment(pickup, pickUp_text);
        newFragment.show(getSupportFragmentManager(), "datePicker");


    }

    public void setDropDate(View view) {
        DialogFragment newFragment = new DatePickerFragment(drop, dropOff_text);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private class GeoApiContext {
    }
}
