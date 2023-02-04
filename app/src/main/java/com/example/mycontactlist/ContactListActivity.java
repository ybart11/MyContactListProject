package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initMapButton();
        initSettingsButton();


        // Enter Listing 6.3 Simple List Activation Code
        ContactDataSource ds = new ContactDataSource(this);
        ArrayList <String> names;

        try {
            ds.open();
            names = ds.getContactName();
            ds.close();

            // Set up the RecyclerView to display the data
            RecyclerView contactList = findViewById(R.id.rvContacts);

            // Creates an instance of the LayoutManager used to display the individual items
            // Use LinearLayoutManager to display a vertical scrolling list
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

            // Associate LayoutManager with the RecyclerView
            contactList.setLayoutManager(layoutManager);

            // Instantiates the ContactAdapter ojbect, passing it to the ArrayList of contact names
            ContactAdapter contactAdapter = new ContactAdapter(names);

            // Finally, this adapter (contactAdapter) is associated with the RecyclerView
            contactList.setAdapter(contactAdapter);


        } catch (Exception e) {

            /* First parameter indicates where the message should display. In this case, we want
                it in the current activity (this). */
            Toast.makeText(this, "Error retrieving contacts",
                    Toast.LENGTH_LONG).show();
        }
    }


    private void initMapButton() {

        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactListActivity.this, ContactMapActivity.class);

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
                Intent intent = new Intent(ContactListActivity.this, ContactSettingsActivity.class);

                // Alert the operating system to not make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    // "Be sure to disable the List button
    private void initListButton() {

        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setEnabled(false);

    }
}