package com.example.mycontactlist;


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
}
