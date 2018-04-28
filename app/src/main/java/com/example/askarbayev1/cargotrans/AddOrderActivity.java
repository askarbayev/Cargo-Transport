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


public class AddOrderActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

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
    int user_id;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        status = intent.getIntExtra("status", -1);
        user_id = intent.getIntExtra("user_id", -1);
        if (googlePlayAvailable()) {
            setContentView(R.layout.activity_add_order);
            defineElements();
            getView();
        }

    }

    public void getView() {
        editTextListener();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        location.setVisibility(View.VISIBLE);
        view = findViewById(R.id.my_view);
        hideShow = findViewById(R.id.hideshow);
        bt_show = findViewById(R.id.show);
        view.setVisibility(View.INVISIBLE);
        view.setVisibility(View.GONE);
        hideShow.setText("HIDE");
        //isUp = false;

    }
    public void defineElements(){
        orderInfo = new HashMap<>();
        location = findViewById(R.id.location);
        destination = findViewById(R.id.destination);
        description = findViewById(R.id.description);
        height = findViewById(R.id.height);
        width = findViewById(R.id.width);
        length = findViewById(R.id.length);
        weight = findViewById(R.id.weight);
        budget = findViewById(R.id.budget);
        pickup = findViewById(R.id.pickup);
        drop = findViewById(R.id.drop);
        pickUp_text = findViewById(R.id.pickup_text);
        dropOff_text = findViewById(R.id.drop_text);
    }

    public void editTextListener(){
        location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                fillText = 1;
                String locat = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        if (locat.length()>2){
                            findLocation(locat);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });
        destination.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                fillText = 2;
                String dest = v.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        if (dest.length()>2){
                            findLocation(dest);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });

    }

    public void slideUp(){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight()+300,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(400);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideDown(){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(400);
        animate.setFillAfter(true);
        view.setVisibility(View.INVISIBLE);
        view.setVisibility(View.GONE);
        view.startAnimation(animate);
    }
    public void hideShow(View view) {
        slideDown();
        isShow = false;
        bt_show.setText("fill info");

    }
    public void Show(View view) {
        if (!isShow){
            slideUp();
            bt_show.setText("Request Order");
            isShow = true;
        }
        else {
            bt_show.setText("fill info");
            isShow = false;
            requestOrder();
        }
    }

    private void requestOrder() {
        String locat = location.getText().toString();
        String pickup_date = pickUp_text.getText().toString();
        String dropoff_date = dropOff_text.getText().toString();
        Log.d("pickUp_text", pickUp_text.getText().toString());
        Log.d("dropOfftext", dropOff_text.getText().toString());
        boolean checkValues = true;
        if (locat.length()==0){
            location.setError("enter location");
            checkValues = false;
        }
        String dest = destination.getText().toString();
        if (dest.length()==0){
            destination.setError("enter destination");
            checkValues = false;
        }
        String descrip = description.getText().toString();
        if (descrip.length()==0){
            description.setError("enter description");
            checkValues = false;
        }
        String height_val = height.getText().toString();
        if (height_val.length()==0){
            height.setError("enter height");
            checkValues = false;
        }
        String width_val = width.getText().toString();
        if (width_val.length()==0){
            width.setError("enter width");
            checkValues = false;
        }
        String length_val = length.getText().toString();
        if (length_val.length()==0){
            length.setError("enter length");
            checkValues = false;
        }
        String weight_val = weight.getText().toString();
        if (weight_val.length()==0){
            weight.setError("enter weight");
            checkValues = false;
        }
        double budget_val = Double.valueOf(budget.getText().toString());
        if (budget.getText().toString().length()==0){
            budget.setError("enter budget");
            checkValues = false;
        }

        if (checkValues){
            String loc_latitude = orderInfo.get("loc_latitude");
            String loc_longitude = orderInfo.get("loc_longitude");
            String dest_latitude = orderInfo.get("dest_latitude");
            String dest_longitude = orderInfo.get("dest_longitude");
            db = new Database(getApplicationContext());
            boolean res = db.insertOrder(user_id, status, locat, dest, descrip, height_val, width_val, length_val, weight_val,
                    budget_val, pickup_date, dropoff_date, loc_latitude, loc_longitude, dest_latitude, dest_longitude);
            if (res){
                Toast.makeText(getApplicationContext(), "your order has been succesfully added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("status", status);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }

        }



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
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (googleMap!=null){


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

                        if (fillText == 2){
                            destination.setText(addressInfo);
                            orderInfo.put("destination", addressInfo);
                            orderInfo.put("dest_latitude", String.valueOf(ll.latitude));
                            orderInfo.put("dest_longitude", String.valueOf(ll.longitude));

                        }
                        else {
                            location.setText(addressInfo);
                            orderInfo.put("location", addressInfo);
                            orderInfo.put("loc_latitude", String.valueOf(ll.latitude));
                            orderInfo.put("loc_longitude", String.valueOf(ll.longitude));
                        }
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

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        googleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
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

            setMarker(locality, lat, lng);
        }

    }

    private void setMarker(String locality, double lat, double lng) {
        if (marker!=null){
            marker.remove();
        }

        MarkerOptions options = new MarkerOptions()
                            .title(locality)
                            .position(new LatLng(lat, lng))
                            .draggable(true)
                            .snippet("Pick up location");
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



}
