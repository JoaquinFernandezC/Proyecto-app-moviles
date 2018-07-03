package com.example.joaquin.workoutplan;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {

    @Insert
    void insertAll(Exercise... exercises);

    @Query("SELECT * from exercise_table ORDER BY Id ASC")
    List<Exercise> getAllExercises();

    @Query("SELECT COUNT(*) from exercise_table")
    int countExercices();
}
