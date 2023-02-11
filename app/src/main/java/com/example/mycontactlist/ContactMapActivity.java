package com.example.mycontactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class ContactMapActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        initListButton();
        initSettingsButton();
    }

    // Stops the sensors if the Activity's life-cycle state changes
    public void onPause () {
        super.onPause();
        try {
            // Ends listening to the gpsListener
            locationManager.removeUpdates(gpsListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void initGetLocationButton () {
//        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
//        locationButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                try {
//
//                    // getBaseContext is used to get the root context, the Activity
//                    locationManager = (LocationManager) getBaseContext().
//                            getSystemService(Context.LOCATION_SERVICE);
//
//                    gpsListener = new LocationListener() {
//
//                        @Override
//                        /* When a location change is detected, is it reported to this method
//                            as a location object */
//                        public void onLocationChanged(@NonNull Location location) {
//                            TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
//                            TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
//                            TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
//                            txtLatitude.setText(String.valueOf(location.getLatitude()));
//                            txtLongitude.setText(String.valueOf(location.getLongitude()));
//                            txtAccuracy.setText(String.valueOf(location.getAccuracy()));
//                        }
//
//                        // Required by LocationListener in addition to onLocationChanged
//                        // Generally used to alert the app that the sensor status has changed
//                        public void onStatusChanged (String provider, int status, Bundle extras){}
//                        public void onProviderEnabled(String provider) {}
//                        public void onProviderDisabled(String provider) {}
//                    };
//
//                    // sent the message requestLocationUpdates to begin listening to location changes
//                    locationManager.requestLocationUpdates(
//                            LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
//
//                } catch (Exception e) {
//                    Toast.makeText(getBaseContext(), "Error, Location not available",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

    private void startLocationUpdates() {

        //
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {

            // getBaseContext is used to get the root context, the Activity
            locationManager = (LocationManager) getBaseContext().
                    getSystemService(Context.LOCATION_SERVICE);

            gpsListener = new LocationListener() {

                @Override
                        /* When a location change is detected, is it reported to this method
                            as a location object */
                public void onLocationChanged(@NonNull Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                    TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);
                    TextView txtAccuracy = (TextView) findViewById(R.id.textAccuracy);
                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));
                }

                // Required by LocationListener in addition to onLocationChanged
                // Generally used to alert the app that the sensor status has changed
                public void onStatusChanged (String provider, int status, Bundle extras){}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };

            // sent the message requestLocationUpdates to begin listening to location changes
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), "Error, Location not available",
                    Toast.LENGTH_LONG).show();
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

    private void initMapButton() {

        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setEnabled(false);
    }


}