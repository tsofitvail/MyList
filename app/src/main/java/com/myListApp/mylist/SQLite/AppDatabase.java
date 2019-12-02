package com.myListApp.mylist.SQLite;

import android.content.Context;

import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.myListApp.mylist.Models.ArchiveItemModel;
import com.myListApp.mylist.Models.ItemModel;

@Database(entities = {ItemModel.class, ArchiveItemModel.class}, version = 5,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "items.db";
    private static AppDatabase INSTANCE;

    public abstract ItemListDao itemListDao();
    public abstract ArchiveItemListDao archiveItemListDao();


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()//tells Room it is allowed to delete the old data
                    .allowMainThreadQueries()//Disables the main thread query check for Room
                    .setJournalMode(JournalMode.TRUNCATE)//for not create db.bad and db.wal files that's creating hindrance in exporting room db
                    .build();
        }
        return INSTANCE;
    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }
}
