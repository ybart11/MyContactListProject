package com.example.mycontactlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList <Contact> contactData;

    // Holds the OnClickListener object passed from the activity
    private View.OnClickListener mOnItemClickListener;

    // This inner class is pretty much like onCreate
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewContact;
        public TextView textPhone;
        public Button deleteButton;

        /* @NonNull indicates that the parameter cannot contain a null value or, if it's before
            a method, that the method cannot return a null value  */
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
            // Sets tag so we can identify which item was clicked
            itemView.setTag(this);

            // Sets the ViewHolder's OnClickListener to the listener passed from the activity
            itemView.setOnClickListener(mOnItemClickListener);
        }
        // Will be used by the adapter to set and change the displayed text
        public TextView getContactTextView() {
            return textViewContact;
        }

        public TextView getPhoneTextView () {
            return textPhone;
        }

        public Button getDeleteButton() {
            return deleteButton;
        }

    }

    // This constructor is used to associate data to be displayed with the adapter
    public ContactAdapter (ArrayList<Contact> arrayList) {
        contactData = arrayList;
    }

    // Sets up an adapter method so that we can pass the listener from the activity to the adapter
    public void setOnItemClickListener (View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    //  Gives layout for each of our rows.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,
                                                            parent, false);
        return new ContactViewHolder(v);
    }

    // Called by the RecyclerView to display the data at the s position
    // Tells Adapter to update data on each of our rows based on the RecyclerView position
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Change value within holder that is passed in
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());
    }

    // Used to determine how many times the other two methods need to be executed
    // Returns the number of items in the data set
    // Wants to know how much data we have to display to the user
    @Override
    public int getItemCount() {
        return contactData.size();
    }
}