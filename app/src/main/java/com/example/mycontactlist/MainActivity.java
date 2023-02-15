package com.example.mycontactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Calendar;

/*
AppCompatActivity: base class for activities that wish to use some of the newer platform features
    on older android devices.

FragmentManager: class responsible for performing actions on your app's fragments, such as adding,
    removing, or replacing them, and adding them to the back stack.

ScrollView: used to ensure that users can access all the data entry widgets, regardless of the size
    of their device

TextWatcher: an object that, when attached to a widget that allows editing, will execute its
    methods when the text in the widget is changed.


 */

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    // Associate between the MainActivity class and a Contact object
    private Contact currentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListButton();
        initMapButton();
        initSettingsButton();

        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            initContact(extras.getInt("contactID"));
        }
        else {
            currentContact = new Contact();
        }

        setForEditing(false);
        initToggleButton();
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();



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

    private void initMapButton() {

        ImageButton ibList = findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,
                        ContactMapActivity.class);

                if (currentContact.getContactID() == -1) {
                    Toast.makeText(getBaseContext(), "Contact must be saved before it can be " +
                            "mapped", Toast.LENGTH_LONG).show();
                }
                else {
                    intent.putExtra("contactid", currentContact.getContactID());
                }

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
                // Goes back to DatePickerDialog java class
                DatePickerDialog datePickerDialog = new DatePickerDialog();

                // Method displays dialog
                // The method requires an instance of a FragmentManager and a name, which
                //      the FragmentManager uses to keep track of the dialog
                datePickerDialog.show(fm, "DatePick");

            }
        });
    }

    private void initSaveButton () {
        Button saveButton = findViewById(R.id.buttonSave);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wasSuccessful = false; // redundant
                // Instantiated
                ContactDataSource ds = new ContactDataSource(MainActivity.this);
                try {
                    ds.open();

                    if (currentContact.getContactID() == -1)  {
                        wasSuccessful = ds.insertContact(currentContact);

                        if (wasSuccessful) {
                            Toast.makeText(MainActivity.this, "Successfully saved!",
                                    Toast.LENGTH_LONG).show();
                            int newId = ds.getLastContactID();
                            currentContact.setContactID(newId);
                        }
                    }
                    else {
                        wasSuccessful = ds.updateContact(currentContact);
                    }

                    ds.close();

                } catch (Exception e) {
                    wasSuccessful = false;
                }

                /* If the save operation was successful, the ToggleButton is toggled to viewing
                    mode and the screen is set for viewing. If it was not successful, the
                     activity remains in editing mode. */
                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }

    /* Sets all the EditTexts to update the MainActivity's contact object with any
        changes users make as they implement them. */
    private void initTextChangedEvents() {

        // Contact name
        // Declared as final bc it is used inside the event code
        final EditText etContactName = findViewById(R.id.editName);

        // TextWatcher requires all three methods though only one will be used
        etContactName.addTextChangedListener(new TextWatcher() {

            /* Executed when the user presses down on a key to enter it into an EditText but
            before the value in the EditText is actually changed */
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            // Execute after each and every character change in an EditText
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            // Called after the user completes editing the data and leaves the EditText
            // Captures the data the user entered
            @Override
            public void afterTextChanged(Editable s) {

                /* Executed when the user ends editing of the EditText.
                   Gets the text in EditText, converts it to a string, and
                    sets the contactName attribute of the currentContact object to that value*/
                currentContact.setContactName(etContactName.getText().toString());
            }
        });

        // Street address
        final EditText etStreetAddress = findViewById(R.id.editAddress);
        etStreetAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(etStreetAddress.getText().toString());
            }
        });

        // City
        final EditText etCity = findViewById(R.id.editCity);
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCity(etCity.getText().toString());
            }
        });

        // State
        final EditText etState = findViewById(R.id.editState);
        etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setState(etState.getText().toString());
            }
        });

        // Zip code
        final EditText etZipCode = findViewById(R.id.editZipcode);
        etZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setZipCode(etZipCode.getText().toString());
            }
        });

        // Home phone number
        final EditText etHome = findViewById(R.id.editHome);
        etHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setPhoneNumber(etHome.getText().toString());
            }
        });

        // Cell  phone number
        final EditText etCell = findViewById(R.id.editCell);
        etCell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCellNumber(etCell.getText().toString());
            }



        });

        // Email
        final EditText etEmail = findViewById(R.id.editEMail);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setEMail(etEmail.getText().toString());
            }
        });


        // Sets the phone number EditTexts to auto-format the number as it's typed
        etHome.addTextChangedListener( new PhoneNumberFormattingTextWatcher());
        etCell.addTextChangedListener( new PhoneNumberFormattingTextWatcher());

    }

    // Retrieves the contact and populates the layout with the values of the retrieved contact
    private void initContact (int id) {
        ContactDataSource ds = new ContactDataSource(MainActivity.this);
        try {
            ds.open();
            currentContact = ds.getSpecificContact(id);
            ds.close();

        } catch (Exception e) {
            Toast.makeText(this, "Load Contact Failed", Toast.LENGTH_LONG).show();
        }


        EditText editName = findViewById(R.id.editName);
        EditText editAddress = findViewById(R.id.editAddress);
        EditText editCity = findViewById(R.id.editCity);
        EditText editState = findViewById(R.id.editState);
        EditText editZipCode = findViewById(R.id.editZipcode);
        EditText editPhone = findViewById(R.id.editHome);
        EditText editCell = findViewById(R.id.editCell);
        EditText editEmail = findViewById(R.id.editEMail);
        TextView birthDay = findViewById(R.id.textBirthday);

        editName.setText(currentContact.getContactName());
        editAddress.setText(currentContact.getStreetAddress());
        editCity.setText(currentContact.getCity());
        editState.setText(currentContact.getState());
        editCity.setText(currentContact.getCity());
        editZipCode.setText(currentContact.getZipCode());
        editPhone.setText(currentContact.getPhoneNumber());
        editCell.setText(currentContact.getCellNumber());
        editEmail.setText(currentContact.getEMail());
        birthDay.setText(DateFormat.format("MM/dd/yyyy",
                currentContact.getBirthday().getTimeInMillis()).toString());



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
    // Mandatory method since this class extends an interface
    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {

        TextView birthDay = findViewById(R.id.textBirthday);

        // chose imported android.text.format from choices
        birthDay.setText(DateFormat.format("MM/dd/yyyy", selectedTime));

        /* This code uses the Contact's class setBirthday method to assign the date selected
            in the custom dialog to the currentContact object. */
        currentContact.setBirthday(selectedTime);

    }

}