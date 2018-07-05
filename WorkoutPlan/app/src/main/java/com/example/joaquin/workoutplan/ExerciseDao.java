package com.example.joaquin.workoutplan;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ExerciseDao {

    @Insert(onConflict = REPLACE)
    void insertAll(Exercise... exercises);

    @Query("SELECT * from exercise_table ORDER BY Id ASC")
    List<Exercise> getAllExercises();

    @Query("SELECT * from exercise_table WHERE id = :id")
    Exercise getExerciseById(int id);

    @Query("SELECT COUNT(*) from exercise_table")
    int countExercices();

    @Query("DELETE from exercise_table")
    void deleteAll();
}
