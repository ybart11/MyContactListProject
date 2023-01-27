package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.Calendar;

/*
AppCompatActivity: base class for activities that wish to use some of the newer platform features
    on older android devices.

FragmentManager: class responsible for performing actions on your app's fragments, such as adding,
    removing, or replacing them, and adding them to the back stack.

ScrollView: used to ensure that users can access all the data entry widgets, regardless of the size
    of their device


 */

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListButton();
        initMapButton();
        initSettingsButton();
        setForEditing(false);
        initToggleButton();
        initChangeDateButton();

    }

    // Associate the ImageButton named imageButtonList on the activity_main layout
    // with the code that is executed when it is pressed
    private void initListButton() {

        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Reference to current activity and what activity it should start
                Intent intent = new Intent(MainActivity.this, ContactListActivity.class);

                // Alert the operating system to not make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

//    private void initListButton() {
//
//        ImageButton ibList = findViewById(R.id.imageButtonList);
//        ibList.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, ContactListActivity.class);
//
//            // Alert the operating system to not make multiple copies of the same activity
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        });
//    }

    private void initMapButton() {

        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactMapActivity.class);

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
                Intent intent = new Intent(MainActivity.this, ContactSettingsActivity.class);

                // Alert the operating system to not make multiple copies of the same activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    // Initialize button to respond to the user
    private void initToggleButton() {

        final ToggleButton editToggle = (ToggleButton) findViewById(R.id.toggleButtonEdit);

        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }

    // Button that enables all the data entry widgets
    private void setForEditing (boolean enabled) {

        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);

        Button buttonChange = findViewById(R.id.btnBirthday);
        Button buttonSave = findViewById(R.id.buttonSave);


        editName.setEnabled(enabled);
        editAddress.setEnabled(enabled);
        editCity.setEnabled(enabled);
        editState.setEnabled(enabled);
        editZipCode.setEnabled(enabled);
        editPhone.setEnabled(enabled);
        editCell.setEnabled(enabled);
        editEmail.setEnabled(enabled);
        buttonChange.setEnabled(enabled);
        buttonSave.setEnabled(enabled);

        if (enabled) {
            editName.requestFocus();
        }
    }

    // Will handle the date that the user selected
    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {

        TextView birthDay = findViewById(R.id.textBirthday);

        // chose imported android.text.format from choices
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime));

    }

    // Change birthday button
    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.btnBirthday);
        changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Required object to manage any and all fragments displayed in an activity
                // Get access to FragmentManager
                FragmentManager fm = getSupportFragmentManager();

                // A new instance of the DatePickerDialog class is created
                // DatePickerDialog extends DialogFragment
                // Goes back to class with
                DatePickerDialog datePickerDialog = new DatePickerDialog();

                // Method displays dialog
                // The method requires an instance of a FragmentManager and a name, which
                //      the FragmentManager uses to keep track of the dialog
                datePickerDialog.show(fm, "DatePick");

            }
        });
    }
}