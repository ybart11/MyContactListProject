package com.example.mycontactlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

/*
FusedLocationProviderClient: provides the functionality needed to get the GPS coordinates of a
    location in real time

OnMapReadyCallBack interface: is used to notify the activity that the map has been downloaded and
    is ready to be used

SensorManager: a system service. You get a reference to it rather than instantiate it.

 */


public class ContactMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Declares a constant that is used to identify the permission that is being requested
    final int PERMISSION_REQUEST_LOCATION = 101;
    GoogleMap gMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    ArrayList <Contact> contacts = new ArrayList<>();
    Contact currentContact = null;

    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    TextView textDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);

        // Registering Sensors for Monitoring
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Test whether sensor is available
        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(mySensorEventListener, accelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(mySensorEventListener, magnetometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(this, "Sensors not found", Toast.LENGTH_LONG).show();
        }
            textDirection = (TextView) findViewById(R.id.textHeading);

        Bundle extras = getIntent().getExtras();
        try {
            ContactDataSource ds = new ContactDataSource(ContactMapActivity.this);
            ds.open();
            if (extras != null) {
                currentContact = ds.getSpecificContact(extras.getInt("contactid"));
            } else {
                contacts = ds.getContacts("contactname", "ASC");
            }

            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Contact(s) could not be retrieved.",
                    Toast.LENGTH_LONG).show();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        // Map is retrieved asynchronously
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        createLocationRequest();
        createLocationCallback();

        initListButton();
        initSettingsButton();
        initMapTypeButtons();
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

    // Class that handles the actual events from the sensors and takes action on them
    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}

            // Sensor readings are returned as a float array
            float[] accelerometerValues;
            float[] magneticValues;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                accelerometerValues = event.values;
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magneticValues = event.values;
            if (accelerometerValues != null && magneticValues != null) {
                float R[] = new float[9];
                float I[] = new float[9];

                boolean success = SensorManager.getRotationMatrix(R, I,
                        accelerometerValues, magneticValues);

                /* If the matrices are successfully calculated, the SensorManager is asked to
                    calculate the orientation of the device. Three dimensions. */
                if (success) {
                    float[] orientation = new float[3];
                    SensorManager.getOrientation(R, orientation);

                    float azimut = (float) Math.toDegrees(orientation[0]);

                    if (azimut < 0.0f) { azimut += 360.0f; } // eliminate negative numbers

                    // Use degree heading to get text description
                    String direction;
                    if (azimut >= 315 || azimut < 45) { direction = "N"; }
                    else if (azimut >= 225 && azimut < 315) { direction = "W"; }
                    else if (azimut >= 135 && azimut < 225) { direction = "S"; }
                    else { direction = "E"; }
                    textDirection.setText(direction);
                }
            }
        }
    };

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
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return ;
                }
                for (Location location : locationResult.getLocations()) {
                    Toast.makeText(getBaseContext(), "Lat: " + location.getLatitude() +
                            " Long: " + location.getLongitude() +
                            "  Accuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
                }
            }
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

        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int measureWidth = size.x;
        int measureHeight = size.y;

        /* If the contact's ArrayList contains Contact objects, the Activity loops through them,
            adding each one to the map. */
        if (contacts.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < contacts.size(); i++) {
                currentContact = contacts.get(i);

                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " +
                        currentContact.getState() + " " +
                        currentContact.getZipCode();

                try {
                    addresses = geo.getFromLocationName(address, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LatLng point = new LatLng(addresses.get(0).getLatitude(),
                        addresses.get(0).getLongitude());
                builder.include(point);
                gMap.addMarker(new MarkerOptions().position(point).
                        title(currentContact.getContactName()).snippet(address));
            }

            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                    measureWidth, measureHeight, 450));
        }
        else {

            /* Else if the ArrayList does not contain any objects, the code checks whether there is
            a single Contact object to map. If there is, the address is retrieved and a LatLng
             object is instantiated for the contact's coordinates; the Latlng object is used to add
             a marker to the map. */
            if (currentContact != null) {
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " +
                        currentContact.getState() + " " +
                        currentContact.getZipCode();

                try {
                    addresses = geo.getFromLocationName(address, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LatLng point = new LatLng(addresses.get(0).getLatitude(),
                        addresses.get(0).getLongitude());
                gMap.addMarker(new MarkerOptions().position(point).
                        title(currentContact.getContactName()).snippet(address));
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
            }
            else {
                AlertDialog alertDialog = new AlertDialog.Builder(ContactMapActivity.this).
                        create();
                alertDialog.setTitle("No data");
                alertDialog.setMessage("No data is available for the mapping function.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }
        }

        // Permission check
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

    // RadioButton code
    private void initMapTypeButtons() {
        RadioGroup rgMagType = findViewById(R.id.radioGroupMapType);
        rgMagType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbNormal = findViewById(R.id.radioButtonNormal);
                if (rbNormal.isChecked()) {
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                else {
                    gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });
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