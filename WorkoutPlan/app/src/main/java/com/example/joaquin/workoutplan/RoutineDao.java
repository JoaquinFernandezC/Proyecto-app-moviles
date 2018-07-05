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

    @Query("SELECT * from routine_table ORDER BY Id ASC")
    List<Routine> getAllRoutines();

    @Query("SELECT COUNT(*) from routine_table")
    int countRoutines();

    @Query("SELECT * from routine_table WHERE id = :id")
    Routine getRoutineById(int id);

    @Query("DELETE from routine_table")
    void deleteAll();

    @Query("SELECT id from routine_table WHERE is_active= 1")
    int getRoutineId();

    @Query("UPDATE routine_table SET is_active = 0")
    void setRoutinesInactive();
}

