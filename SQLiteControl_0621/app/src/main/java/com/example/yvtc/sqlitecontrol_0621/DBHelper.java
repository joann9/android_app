package com.example.yvtc.sqlitecontrol_0621;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DatabaseName = "coffee.db";  //對應下方  super(context, DatabaseName, null, DatabaseVersion);
    public static final int DatabaseVersion = 1;
    public static final String createTableSQL = "CREATE TABLE " + "coffee_list (_id INTEGER PRIMARY KEY, title TEXT, price NUMERIC,"+
            "image BLOB, created_time TEMESTAMP default CURRENT_TIMESTAMP)";  //_id 規定一定要有
    public static final String dropTableSQL = "DROP TABLE IF EXISTS coffee_list";

    public DBHelper(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);  //原來是 super(context, name, factory, version)
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //onDrapTable(db);
        db.execSQL(dropTableSQL);
        onCreate(db);
    }


}
