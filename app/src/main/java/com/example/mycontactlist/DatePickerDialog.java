package com.example.mycontactlist;

import android.os.Bundle;
import java.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.fragment.app.DialogFragment;

/*
                                Important terms
Fragment: represents a reusable portion of your app's UI. Defines and manages its own layout,
    has its own lifecycle, and can handle its own input events. Cannot live on their own.
    Must be hosted by an activity or another fragment.
    Represents a particular operation or interface that is running within a larger Activity

DialogFragment: displays a Dialog, but inside a Fragment

Dialog: a small window that prompts the user to make a decision or enter additional info. Does not
    fill the screen and is normally used for modal events that require to take an action before
    they can proceed.

View: a basic building block that responds to user inputs. Eg: EditText, Button, CheckBox

Activity: provides the window in which the app draws its UI

LayoutInflater: used to instantiate the contents of layout XML files into their corresponding
    View objects. In other words, it takes an XML file as input and builds the View objects from it.

ViewGroup: a special view that can contain other views (called children). Base class for layouts
    and views containers.

onCreateView(LayoutInflater, ViewGroup, Bundle): creates and returns the view hierarchy
    associated with the fragment


 */




// subclass of DialogFragment which is also subclass of the Fragment class
public class DatePickerDialog extends DialogFragment {

    // Declare class variable to hold the selected date
    Calendar selectedDate;


    /*
     A listener must be created with the DialogFragment. This is how the dialog communicates the
     user's actions on the dialog back to the activity that displayed the dialog. The listener must
     have a method to report the results of the dialog. The activity will have to implement the
     listener to handle the user's action.
     */
    public interface SaveDateListener {
        void didFinishDatePickerDialog (Calendar selectedTime);
    }


    /*
     Empty constructor required for DialogFragment
     The framework will often re-instantiate a fragment class when needed, in particular during
     state restore, and needs to be able to find this constructor to instantiate it. Else,
     runtime exception will occur in some cases during state restore
    */
    public DatePickerDialog () {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.select_date, container);

        getDialog().setTitle("Select Date");

        // selectedDate Calender object is instantiated. Current date by default
        selectedDate = Calendar.getInstance();

        // Capture the selected date anytime the user changes the date
        final CalendarView cv = view.findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year,
                                            int month, int day) {

                selectedDate.set(year, month, day);

            }
        });


        // When user clicks the OK button, it sends the last selected date to the saveItem method
        // to report the selection to the main activity
        Button saveButton = view.findViewById(R.id.buttonSelect);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveItem(selectedDate);
            }
        });


        Button cancelButton = view.findViewById(R.id.buttonCancel);
            cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getDialog().dismiss();
            }
        });

        return view;
    }

    private void saveItem (Calendar selectedTime) {

        // Interface inst
        SaveDateListener activity = (SaveDateListener) getActivity();
        activity.didFinishDatePickerDialog(selectedTime);
        getDialog().dismiss();
    }

}
