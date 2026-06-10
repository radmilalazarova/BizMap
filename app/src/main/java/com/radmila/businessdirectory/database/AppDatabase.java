package com.radmila.businessdirectory.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {CompanyEntity.class}, version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;


    public abstract CompanyDao companyDao();


    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {

                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "business_directory.db"
                            )

                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}