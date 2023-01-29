package com.example.mycontactlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
this class purpose: Primary function of this class is to determine what must be done upon creation
    of the database and what must be done when the database is upgraded. Provide for the creation,
    modification, and deletion of tables in the database.

SQLiteOpenHelper: a helper to manage database creation and version management

Context: Hover over class name for info. It allows us to access resources. It allows us to
    interact with other Android components by sending messages. It gives you information
    about your app environment.

Log.w(): Basically, use this to log stuff you didn't expect to happen but isn't necessarily an
    error. Kind of like, "hey, this happened, and it's weird, we should look into it."

static: belongs to a type itself, rather than to an instance of that type. Shared across all
    instances of the class.

DATABASE_VERSION = 1: Hold the database version number. Every time the database is accessed, the
    existing database version is compared to the one here. If the number is higher, the onUpgrade
    method is executed. The number is incremented by the developer when they need to upgrade an
    existing database.

 */



public class ContactDBHelper extends SQLiteOpenHelper {

    // declared to name the database file. Required. ".db" is the extension
    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String CREATE_TABLE_CONTACT =
            "create table contact (_id integer primary key autoincrement, "
                    + "contactname text not null, streetaddress text, "
                    + "city text, state text, zipcode text, "
                    + "phonenumber text, cellnumber text, "
                    + "email text, birthday text);";

    // Required. Calls the super class's constructor method.
    public ContactDBHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Abstract method from SQLiteOpenHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACT);
    }

    // Abstract method from SQLiteOpenHelper
    /* Executed when the database is opened and the current version # in the code is higher than
        the version number of the current database. */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ContactDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(db);
    }
}
