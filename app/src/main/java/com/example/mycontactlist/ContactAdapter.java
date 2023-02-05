package com.example.mycontactlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList <String> contactData;

    // Holds the OnClickListener object passed from the activity
    private View.OnClickListener mOnItemClickListener;

    // This inner class is pretty much like onCreate
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewContact;

        /* @NonNull indicates that the parameter cannot contain a null value or, if it's before
            a method, that the method cannot return a null value  */
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textViewName);

            // Sets tag so we can identify which item was clicked
            itemView.setTag(this);

            // Sets the ViewHolder's OnClickListener to the listener passed from the activity
            itemView.setOnClickListener(mOnItemClickListener);
        }
        // Will be used by the adapter to set and change the displayed text
        public TextView getContactTextView() {
            return textViewContact;
        }
    }

    // This constructor is used to associate data to be displayed with the adapter
    public ContactAdapter (ArrayList<String> arrayList) {
        contactData = arrayList;
    }

    public ContactAdapter () {}

    // Sets up an adapter method so that we can pass the listener from the activity to the adapter
    public void setOnItemClickListener (View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    /* Is called for each item in the data set to be displayed. Its job is to create the visual
        display for each item using the layout file created. For each item, a ViewHolder is
         created using the inflated XML and returned to the RecyclerView to be displayed
         in the activity. Gives layout for each of our rows. */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_view,
                                                            parent, false);
        return new ContactViewHolder(v);
    }

    /* It is passed to the ViewHolder created by the onCreateViewHolder method as a generic
        ViewHolder. This is then cast into the associated ContactViewHolder, and the classes
         getContactTextView method is called to set the text attribute of the TextView to the name
          of the contact at the current position in the data set. */
    // Called by the RecyclerView to display the data at the s position
    // Tells Adapter to update data on each of our rows based on the RecyclerView position
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Change value within holder that is passed in
        ContactViewHolder cvh = (ContactViewHolder) holder;

        //
        cvh.getContactTextView().setText(contactData.get(position));
    }

    // Used to determine how many times the other two methods need to be executed
    // Returns the number of items in the data set
    // Wants to know how much data we have to display to the user
    @Override
    public int getItemCount() {
        return contactData.size();
    }
}