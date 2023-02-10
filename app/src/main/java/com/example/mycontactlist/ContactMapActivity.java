package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class ContactMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_map);
        initListButton();
        initSettingsButton();
        initGetLocationButton();
    }

    private void initGetLocationButton () {
        Button locationButton = (Button) findViewById(R.id.buttonGetLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText editAddress  = (EditText) findViewById(R.id.editTextMapAddress);
                EditText editCity = (EditText) findViewById(R.id.editTextMapCity);
                EditText editState = (EditText) findViewById(R.id.editTextMapState);
                EditText editZipCode = (EditText) findViewById(R.id.editTextMapZipcode);

                // Proper format for Geocoder
                String address = editAddress.getText().toString() + ", " +
                                    editCity.getText().toString() + ", " +
                                    editState.getText().toString() + ", " +
                                    editZipCode.getText().toString();

                List <Address> addresses = null;
                Geocoder geo = new Geocoder(ContactMapActivity.this);

                try {
                    addresses = geo.getFromLocationName(address, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* The latitude and longitude of the first address in the returned list are
                    displayed in the appropriate TextView widgets*/
                TextView txtLatitude = (TextView) findViewById(R.id.textLatitude);
                TextView txtLongitude = (TextView) findViewById(R.id.textLongitude);

                txtLatitude.setText(String.valueOf(addresses.get(0).getLatitude()));
                txtLongitude.setText(String.valueOf(addresses.get(0).getLongitude()));

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

    private void initMapButton() {

        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setEnabled(false);
    }


}