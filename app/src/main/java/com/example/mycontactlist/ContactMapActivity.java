package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.snackbar.Snackbar;

/*
FusedLocationProviderClient: provides the functionality needed to get the GPS coordinates of a
    location in real time

OnMapReadyCallBack interface: is used to notify the activity that the map has been downloaded and
    is ready to be used

 */


public class ContactMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Declares a constant that is used to identify the permission that is being requested
    final int PERMISSION_REQUEST_LOCATION = 101;
    GoogleMap gMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Map is retrieved asynchronously
        mapFragment.getMapAsync(this);

        createLocationRequest();
        createLocationCallback();

        initListButton();
        initSettingsButton();
    }

    // Stops the sensors if the Activity's life-cycle state changes
    @Override
    public void onPause () {
        super.onPause();
        if ((Build.VERSION.SDK_INT >= 23) &&
                (ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)) {
            return;
        }
    }

    private void createLocationRequest () {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY)
//                .setIntervalMillis(10000)
//                .setMinUpdateIntervalMillis(5000)
//                .build();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return ;
                }
                for (Location location : locationResult.getLocations()) {
                    Toast.makeText(getBaseContext(), "Lat: " + location.getLatitude() +
                            " Long: " + location.getLongitude() +
                            "  Accuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
                }
            };
        };
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        gMap.setMyLocationEnabled(true);
    }

    private void stopLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try{
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(ContactMapActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            ContactMapActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        Snackbar.make(findViewById(R.id.activity_contact_map),
                                        "MyContactList requires this permission to locate " +
                                                "your contacts", Snackbar.LENGTH_INDEFINITE)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ActivityCompat.requestPermissions(
                                                ContactMapActivity.this,
                                                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                                PERMISSION_REQUEST_LOCATION);
                                    }
                                })
                                .show();
                    } else {
                        ActivityCompat.requestPermissions(ContactMapActivity.this, new
                                        String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_LOCATION);
                    }
                } else {
                    startLocationUpdates();
                }
            } else {
                startLocationUpdates();
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error requesting permission",
                    Toast.LENGTH_LONG).show();
        }
    }


//        try {
//
//            // getBaseContext is used to get the root context, the Activity
//            locationManager = (LocationManager) getBaseContext().
//                    getSystemService(Context.LOCATION_SERVICE);
//
//            gpsListener = new LocationListener() {
//
//                @Override
//                        /* When a location change is detected, is it reported to this method
//                            as a location object */
//                public void onLocationChanged(@NonNull Location location) {
//                    TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
//                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
//                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
//                    txtLatitude.setText(String.valueOf(location.getLatitude()));
//                    txtLongitude.setText(String.valueOf(location.getLongitude()));
//                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
//
//                    if (isBetterLocation(location)) {
//                        currentBestLocation = location;
//                    }
//                }
//
//                // Required by LocationListener in addition to onLocationChanged
//                // Generally used to alert the app that the sensor status has changed
//                public void onStatusChanged (String provider, int status, Bundle extras){}
//                public void onProviderEnabled(String provider) {}
//                public void onProviderDisabled(String provider) {}
//            };
//
//            networkListener = new LocationListener() {
//
//                @Override
//                        /* When a location change is detected, is it reported to this method
//                            as a location object */
//                public void onLocationChanged(@NonNull Location location) {
//                    TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
//                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
//                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
//                    txtLatitude.setText(String.valueOf(location.getLatitude()));
//                    txtLongitude.setText(String.valueOf(location.getLongitude()));
//                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
//
//                    if (isBetterLocation(location)) {
//                        currentBestLocation = location;
//                    }
//                }
//
//                // Required by LocationListener in addition to onLocationChanged
//                // Generally used to alert the app that the sensor status has changed
//                public void onStatusChanged (String provider, int status, Bundle extras){}
//                public void onProviderEnabled(String provider) {}
//                public void onProviderDisabled(String provider) {}
//            };
//
//            // sent the message requestLocationUpdates to begin listening to location changes
//            locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
//            locationManager.requestLocationUpdates(
//                    LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);
//
//        } catch (Exception e) {
//            Toast.makeText(getBaseContext(), "Error, Location not available",
//                    Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    // Required to respond to the user's response
    public void onRequestPermissionsResult (int requestCode,
                                            String[] permissions, int [] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(ContactMapActivity.this,
                            "MyContactList will not locate your contacts.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // Associate the ImageButton named imageButtonList on the activity_main layout
    // with the code that is executed when it is pressed
    private void initListButton() {

        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactMapActivity.this, ContactListActivity.class);

                // Alert the operating system to not make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


    private void initSettingsButton() {

        ImageButton ibList = findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactMapActivity.this, ContactSettingsActivity.class);

                // Alert the operating system to not make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }


//    private void initMapButton() {
//
//        ImageButton ibList = findViewById(R.id.imageButtonMap);
//        ibList.setEnabled(false);
//    }


}