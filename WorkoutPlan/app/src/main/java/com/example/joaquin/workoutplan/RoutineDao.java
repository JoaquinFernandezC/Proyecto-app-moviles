package com.example.joaquin.workoutplan;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RoutineDao {
    @Insert(onConflict = REPLACE)
    void insertRoutine(Routine routine);

    @Query("SELECT * from exercise_table ORDER BY Id ASC")
    List<Exercise> getAllExercises();
}

