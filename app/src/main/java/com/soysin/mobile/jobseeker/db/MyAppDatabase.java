package com.soysin.mobile.jobseeker.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.soysin.mobile.jobseeker.model.Account;


@Database(entities = {Account.class},version = 4)
public abstract class MyAppDatabase extends RoomDatabase {
    //create database instance
    private static MyAppDatabase myAppDatabase;

    //create database name
    private static String DATABASE_NAME = "accounts";

    public synchronized static MyAppDatabase getInstance(Context context){
        //check condition
        if (myAppDatabase == null){
            myAppDatabase = Room.databaseBuilder(context.getApplicationContext(),MyAppDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        //return database
        return myAppDatabase;
    }
    public abstract MyDAO getMyDao();
}
