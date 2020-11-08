package com.scandit.datacapture.matrixscanbubblessample.Data;
import java.util.List;
import java.util.ArrayList;

import com.scandit.datacapture.matrixscanbubblessample.models.MyItem;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHandler extends SQLiteOpenHelper {

    String DB_PATH = null;
    private static String DB_NAME = "mydatabase.db";
    private static String TABLE_NAME = "mojatabela";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private static final int DATABASE_VERSION = 46;

    public DataBaseHandler(Context context) {

        super(context, DB_NAME, null, DATABASE_VERSION);

        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {
            Log.e("Path 1", "exist");
        } else {
            Log.e("Path 1", "NOTexist");
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public Cursor returnCursor()
    {
        String selectAll = "SELECT * FROM " + TABLE_NAME;

        Cursor c = myDataBase.rawQuery(selectAll, null);
        return c;
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        this.myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        Cursor c = this.myDataBase.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public List<MyItem> getAllContacts() {

        //Initialize
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        //Select all contacts
        String selectAll = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = myDataBase.rawQuery(selectAll, null);


        List<MyItem> myItemList = new ArrayList<>();

        //Loop through our data
        if (cursor.moveToFirst()) {
            do {
                MyItem contact = new MyItem();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setIsScrap(cursor.getString(2));
                contact.setCodeNumber(cursor.getString(3));
                contact.setRemark(cursor.getString(4));

                //add contact objects to our list
                myItemList.add(contact);
            }while (cursor.moveToNext());
        }

        return myItemList;
    }

    @Override
    public synchronized void close() {
        if (this.myDataBase != null)
            this.myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.myDataBase.query(TABLE_NAME, null, null, null, null, null, null);
    }

}