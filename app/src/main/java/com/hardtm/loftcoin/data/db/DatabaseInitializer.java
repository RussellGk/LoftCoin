package com.hardtm.loftcoin.data.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.hardtm.loftcoin.data.db.room.AppDatabase;
import com.hardtm.loftcoin.data.db.room.DatabaseImplRoom;

public class DatabaseInitializer {

    public Database init(Context context) {
        AppDatabase appDatabase = Room
                .databaseBuilder(context, AppDatabase.class,"loftcoin.db")
                .fallbackToDestructiveMigration() // Drop all old data if needs the migration
                .build();

        return new DatabaseImplRoom(appDatabase);
    }
}
