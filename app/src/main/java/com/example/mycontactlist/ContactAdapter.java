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

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewContact;

        /* @NonNull indicates that the parameter cannot contain a null value or, if it's before
            a method, that the method cannot return a null value  */
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textViewName);
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


    /* -- Required methods for a RecyclerView.Adapter -- */

    /* Is called for each item in the data set to be displayed. Its job is to create the visual
        display for each item using the layout file created. For each item, a ViewHolder is
         created using the inflated XML and returned to the RecyclerView to be displayed
         in the activity. */
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
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        ContactViewHolder cvh = (ContactViewHolder) holder;

        //
        cvh.getContactTextView().setText(contactData.get(position));
    }

    // Used to determine how many times the other two methods need to be excuted
    // Returns the number of items in the data set
    @Override
    public int getItemCount() {
        return contactData.size();
    }
}