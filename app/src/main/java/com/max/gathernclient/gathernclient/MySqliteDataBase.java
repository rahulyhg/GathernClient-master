package com.max.gathernclient.gathernclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteDataBase extends SQLiteOpenHelper {

    private final static String DB_NAME = "SHDB" ;
    private final static int DB_VERSION = 1 ;

    public MySqliteDataBase (Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE userLocation(id integer primary key autoincrement,lat double,lon double)");
        db.execSQL("CREATE TABLE calenderDate(id integer primary key autoincrement,dayNumber  String,dayText  String,month String ,year String  )");
        db.execSQL("CREATE TABLE singedUser(id integer primary key autoincrement,signState int)");
        db.execSQL("CREATE TABLE house(id integer primary key autoincrement,zone String,name String , code String , price String , rate String , numberOfPersons String , numOfWc String)");
        db.execSQL("CREATE TABLE userData(id integer primary key autoincrement,mobile String,deviceMODEL String , email String , firstName String , lastName String , yearOfBirth String , monthOfBirth String ,dayOfBirth String ,access_token String )");
        db.execSQL("CREATE TABLE appData(id integer primary key autoincrement,allData String , dataVersion String)");
        db.execSQL("CREATE TABLE savedFilterItems(id integer primary key autoincrement,arrayList String )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}