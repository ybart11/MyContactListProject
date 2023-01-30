package com.example.mycontactlist;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;

/*
this class purpose: Opens and closes the database and contains the queries used to store and
    retrieve data from the databse
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

            // The database's update method is called to place the changes in the database
            didSucceed = database.update("contact", updateValues,
                    "_id=" + rowId, null) > 0;

        } catch (Exception e) {
            // Do nothing -will return false if there is an exception
        }
        return didSucceed;
    }



}


