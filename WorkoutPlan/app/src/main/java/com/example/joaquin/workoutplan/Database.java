package com.example.joaquin.workoutplan;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

@android.arch.persistence.room.Database(entities = {Exercise.class, Routine.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {

    private static Database INSTANCE;

    public abstract ExerciseDao exerciseDao();
    public abstract RoutineDao routineDao();

    public synchronized static Database getInstance(Context context){
        if (INSTANCE==null){
            INSTANCE = buildDatabase(context);
        }
        return INSTANCE;
    }

    static final Migration Migration_1_3 = new Migration(1, 3){
        @Override
        public void migrate(SupportSQLiteDatabase database){
        }
    };
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
        }).addMigrations(Migration_1_3).allowMainThreadQueries().build();
    }
}
