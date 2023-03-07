package com.example.mycontactlist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import org.w3c.dom.Text;

import java.util.ArrayList;

/*
Context: Hover over class name for info. It allows us to access resources. It allows us to
        interact with other Android components by sending messages. It gives you information
        about your app environment.

 */

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList <Contact> contactData;

    // Holds the OnClickListener object passed from the activity
    private View.OnClickListener mOnItemClickListener;

    private boolean isDeleting;

    /* Needed to open the database so the contact can be deleted and to display the message
        if the deletion fails */
    private Context parentContext;

    // This inner class is pretty much like onCreate
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewContact;
        public TextView textPhone;
        public TextView textAddress;
        public TextView textCity;
        public TextView textState;
        public TextView textZipcode;
        public Button deleteButton;

        /* @NonNull indicates that the parameter cannot contain a null value or, if it's before
            a method, that the method cannot return a null value  */
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
            textAddress = itemView.findViewById(R.id.text_Address);
            textCity = itemView.findViewById(R.id.text_City);
            textState = itemView.findViewById(R.id.text_State);
            textZipcode = itemView.findViewById(R.id.text_zipcode);

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

        public TextView getAddressTextView() {
            return textAddress;
        }

        public TextView getCityTextView () {
            return textCity;
        }

        public TextView getStateTextView() {
            return textState;
        }

        public TextView getZipcodeTextView() {
            return textZipcode;
        }



    }

    public ContactAdapter (ArrayList<Contact> arrayList, Context context) {
        contactData = arrayList;
        parentContext = context;
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 @SuppressLint("RecyclerView") final int position) {

        // Change value within holder that is passed in
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());
        cvh.getAddressTextView().setText(contactData.get(position).getStreetAddress() + ",");
        cvh.getCityTextView().setText(contactData.get(position).getCity() + ",");
        cvh.getStateTextView().setText(contactData.get(position).getState() + ",");
        cvh.getZipcodeTextView().setText(contactData.get(position).getZipCode());

        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteItem(position); // replace with holder.getHolderPosition
                }
            });
        }
        else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }

        // Here is where you should add code to change the color of the contact names
        if(position % 3 == 0) // Checks if position is even
        {
            ((ContactViewHolder) holder).textViewContact.
                    setTextColor(Color.parseColor("#FF0000")); // red

        }
        else if (position % 2 == 0) {
            ((ContactViewHolder) holder).textViewContact.
                    setTextColor(Color.parseColor("#00FF00")); // green
        }
        else
        {
            ((ContactViewHolder) holder).textViewContact.
                    setTextColor(Color.parseColor("#0000FF")); // blue
        }

    }

    // Used to determine how many times the other two methods need to be executed
    // Returns the number of items in the data set
    // Wants to know how much data we have to display to the user
    @Override
    public int getItemCount() {
        return contactData.size();
    }


    private void deleteItem (int position) {
        Contact contact = contactData.get(position);
        ContactDataSource ds = new ContactDataSource(parentContext);
        try {
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getContactID());
            ds.close();

            if (didDelete) {
                contactData.remove(position);
                notifyDataSetChanged(); // refresh display
                Toast.makeText(parentContext, "Delete Success!", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }

    // A setter since the isDeleting is private
    public void setDelete (boolean b) {
        isDeleting = b;
    }

}