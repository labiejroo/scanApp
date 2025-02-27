/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scandit.datacapture.matrixscanbubblessample;

import com.scandit.datacapture.matrixscanbubblessample.models.AppManager;
import com.scandit.datacapture.matrixscanbubblessample.Data.DataBaseHandler;
import android.os.Bundle;
import android.util.Log;

import android.database.Cursor;
import android.database.SQLException;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.scandit.datacapture.matrixscanbubblessample.models.MyItem;
import com.scandit.datacapture.matrixscanbubblessample.scan.ScanFragment;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ScanFragment.newInstance())
                    .commit();
        }

        DataBaseHandler myDbHelper = new DataBaseHandler(MainActivity.this);
        try {
            myDbHelper.createDataBase();
        } catch (Exception ioe) {
            throw new Error("Unable to create database");
        }
        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        Toast.makeText(MainActivity.this, "Successfully Imported", Toast.LENGTH_SHORT).show();

        Cursor c = myDbHelper.returnCursor();

//        if (c.moveToFirst()) {
//            do {
//                Toast.makeText(MainActivity.this,
//                        "_id: " + c.getString(0) + "\n" +
//                                "name: " + c.getString(1) + "\n" +
//                                "isScrap: " + c.getString(2) + "\n" +
//                                "code:  " + c.getString(3)+ "\n" +
//                                "remark:  " + c.getString(4),
//                        Toast.LENGTH_LONG).show();
//            } while (c.moveToNext());
//        }

        List<MyItem> myItems = new ArrayList<>();

       myItems = myDbHelper.getAllContacts();

       AppManager.getInstance().setMyItems(myItems);
    }
}
