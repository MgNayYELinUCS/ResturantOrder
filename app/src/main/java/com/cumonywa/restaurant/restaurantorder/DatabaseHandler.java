package com.cumonywa.restaurant.restaurantorder;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cumonywa.restaurant.restaurantorder.model.FoodMenuModel;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_QTY = "qty";
    private static final String KEY_PRICE = "price";
    private static final String KEY_TBNAME= "tbname";
    private static final String KEY_WNAME= "wname";
    private static final String KEY_DES= "description";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_QTY + " TEXT," + KEY_PRICE + " TEXT,"+KEY_TBNAME + " TEXT ,"+KEY_WNAME + " TEXT,"+KEY_DES + " TEXT "+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new foodmenu
    public void addContact(String name,int qty,int price,String tablename,String waitername,String des) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_QTY, qty);
        values.put(KEY_PRICE, price);
        values.put(KEY_TBNAME, tablename);
        values.put(KEY_WNAME, waitername);
        values.put(KEY_DES, des);

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Contacts
    public List<FoodMenuModel> getAllContacts( String tablename) {
        List<FoodMenuModel> contactList = new ArrayList<FoodMenuModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS +" WHERE "+KEY_TBNAME+"='"+tablename+"'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);//result set

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FoodMenuModel contact = new FoodMenuModel();
                contact.setFood_name(cursor.getString(1));
                contact.setFood_qty(Integer.parseInt(cursor.getString(2)));
                contact.setPrice(Integer.parseInt(cursor.getString(3)));
                contact.setWaitername(cursor.getString(5));
                contact.setDescription(cursor.getString(6));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Deleting single contact
    public void deleteAllContact(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_TBNAME + " = ?", new String[] { String.valueOf(tablename) });
        db.close();
    }


    public void deleteEachContact(String foodName,String des) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + " = ? AND "+KEY_DES+" = ?", new String[] { String.valueOf(foodName),String.valueOf(des)});
        db.close();
    }

    public void foodupdate(String name,int qty,int price,String tablename,String waitername,String des){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_CONTACTS +" WHERE "+KEY_NAME+"='"+name+"' AND "+KEY_DES+"='"+des+"'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        String description="";
        int qty1=0;
        int count=0;
        if (cursor.moveToFirst()) {
            do {
                count=cursor.getInt(0);
                /*description=cursor.getString(6);*/
            } while (cursor.moveToNext());
        }
        Log.i("Count",count+"");
        if (count>0){
            selectQuery = "SELECT * FROM " + TABLE_CONTACTS +" WHERE "+KEY_NAME+"='"+name+"' AND "+KEY_DES+"='"+des+"'";
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    qty1=cursor.getInt(2);
                    qty += qty1;
                    Log.i("Qty",qty+"");
                    ContentValues cv = new ContentValues();
                    cv.put(KEY_QTY, qty);
                    //cv.put(KEY_DES, des);
                    db.update(TABLE_CONTACTS, cv, KEY_NAME + " = ? AND "+KEY_DES+" = ?", new String[] { String.valueOf(name),String.valueOf(des)});
                } while (cursor.moveToNext());
            }
        }
        else {
            Log.i("Addd","Not Update");
            addContact(name,qty,price,tablename,waitername,des);
        }
        db.close();
    }
    public int getItemCount(String name){
        String selectQuery = "SELECT COUNT(*) FROM " + TABLE_CONTACTS +" WHERE "+KEY_NAME+"='"+name+"'";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        int count=0;
        if (cursor.moveToFirst()) {
            do {
                count=cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        return count;
    }
}
