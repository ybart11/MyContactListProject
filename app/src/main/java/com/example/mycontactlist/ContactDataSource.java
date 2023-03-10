package com.example.mycontactlist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

/*
this class purpose: Opens and closes the database and contains the queries used to store and
    retrieve data from the database
 */

public class ContactDataSource {

    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource (Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    // Create and/or open a database that will be used for reading and writing.
    public void open () throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Note: the database inserts the ID bc the _id field was declared as an autoincrement field
    public boolean insertContact(Contact c) {
        // Tells the calling code if the operation succeeded
        boolean didSucceed = false;

        try {

            // Used to store a set of key/value pairs
            ContentValues initialValues = new ContentValues();

            /* The values for the table are retrieved from the contact object, associated with
                the correct field, and inserted into the ContentValues Object. */
            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.getEMail());
            // Store as millis because SQLite doesn't support storing data as dates directly
            initialValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));

            // Inserting a picture to the database
            if (c.getPicture() != null) {
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, boas);
                byte[] photo = boas.toByteArray();
                initialValues.put("contactphoto", photo);
            }

            /* The method returns the number of records (rows) successfully inserted. The value
                is compared to zero. If it is greater than zero, then the operation succeeded
                 and the return value is set to true. */
            didSucceed = database.insert("contact", null, initialValues) > 0;

        } catch (Exception e) {
            /*
            If the method throws an exception, the return value is already set to false,
            so we don't hae to do anything
             */
        }

        return didSucceed;
    }

    public boolean updateContact (Contact c) {
        boolean didSucceed = false;

        try {

            Long rowId = (long) c.getContactID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.getEMail());
            updateValues.put("birthday", String.valueOf(c.getBirthday().getTimeInMillis()));

            // Updating a picture to the database
            if (c.getPicture() != null) {
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, boas);
                byte[] photo = boas.toByteArray();
                updateValues.put("contactphoto", photo);
            }

            // The database's update method is called to place the changes in the database
            didSucceed = database.update("contact", updateValues,
                    "_id=" + rowId, null) > 0;

        } catch (Exception e) {
            // Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    // Retrieves the new contact ID
    public int getLastContactID() {
        int lastId;
        try {

            /* Query is to get maximum value bc the last contact entered will have the max value
            because the _id field is set to autoincrement. */
            String query = "Select MAX (_id) from contact";

            /* A cursor is declared and assigned to hold the results of the execution of the
                query. A cursor is an object that is used to hold and move through the results
                 of a query. */
            Cursor cursor = database.rawQuery(query, null);

            // The cursor is told to move to the first record in the returned data
            cursor.moveToFirst();

            /* The max ID is retrieved from the record set. Fields in the record set are indexed
                starting at 0. */
            lastId = cursor.getInt(0);


            cursor.close();

        } catch (Exception e) {
            lastId = -1;
        }

        return lastId;
    }

    // Method that will retrieve each contact's name
    public ArrayList<String> getContactName () {
        ArrayList <String> contactNames = new ArrayList<>();

        try {
            String query = "Select contactname from contact";

            // Cursor object holds the result of the query
            Cursor cursor = database.rawQuery(query, null);

            /* A loop is set up to go through all the records in the cursor. The loop is
                initialized by moving to the first record in the cursor. Next, the while loop
                 is set up to test if the end of the cursor's record set has been reached.
                 Forgetting the moveToNext() command will leave your method in an infinite loop
                 because it will never reach the end of the record set. */
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contactNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            contactNames = new ArrayList<>();
        }

        return contactNames;
    }

    public ArrayList <Contact> getContacts(String sortField, String sortOrder) {
        ArrayList <Contact> contacts = new ArrayList<>();
        try {
            String query = "SELECT * FROM contact ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Contact newContact;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newContact = new Contact();
                newContact.setContactID(cursor.getInt(0)); // first field
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.setEMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();
            }
        } catch (Exception e) {
            contacts = new ArrayList<>();
        }

        return contacts;
    }

    // Retrieves a specific contact based on the contact's ID
    public Contact getSpecificContact (int contactId){

        Contact contact = new Contact();
        String query = "SELECT * FROM contact where _id =" + contactId;
        Cursor cursor = database.rawQuery(query, null);

        /* The cursor moves to the first record returned. If a contact is found, the Contact
            object is populated. If no contact was retrieved, the moveTofirst method will be false
             and the contact will not be populated. */
        if (cursor.moveToFirst()) {
            contact.setContactID(cursor.getInt(0)); // first field
            contact.setContactName(cursor.getString(1));
            contact.setStreetAddress(cursor.getString(2));
            contact.setCity(cursor.getString(3));
            contact.setState(cursor.getString(4));
            contact.setZipCode(cursor.getString(5));
            contact.setPhoneNumber(cursor.getString(6));
            contact.setCellNumber(cursor.getString(7));
            contact.setEMail(cursor.getString(8));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
            contact.setBirthday(calendar);

            // Getting a picture from the database
            byte [] photo = cursor.getBlob(10);
            if (photo != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap thePictures = BitmapFactory.decodeStream(imageStream);
                contact.setPicture(thePictures);
            }

            cursor.close();
        }
        return contact;
    }

    // Method to delete a contact using ID
    public boolean deleteContact (int contactId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("contact", "_id=" + contactId,
                    null) > 0;
        } catch (Exception e) {
            // Do nothing -return value already set to false
        }

        return didDelete;
    }




}


