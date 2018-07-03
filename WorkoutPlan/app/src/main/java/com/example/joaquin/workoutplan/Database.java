package com.example.joaquin.workoutplan;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

@android.arch.persistence.room.Database(entities = {Exercise.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    public abstract ExerciseDao exerciseDao();

    public synchronized static Database getInstance(Context context){
        if (INSTANCE==null){
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    private static Database buildDatabase(final Context context){
        return Room.databaseBuilder(context, Database.class,
                "Database").addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        getInstance(context).exerciseDao().insertAll(Exercise.populateData());
                    }
                });
            }
        }).allowMainThreadQueries().build();
    }
}
