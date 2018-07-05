package com.example.joaquin.workoutplan;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity(tableName = "routine_table")
public class Routine {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int Id;
    @ColumnInfo(name = "start_date")
    private String StartDate;
    @ColumnInfo(name = "finish_date")
    private String FinishDate;
    @ColumnInfo(name = "days")
    private ArrayList<String> Days = new ArrayList<>();
    @ColumnInfo(name = "exercises")
    private ArrayList<Integer> Exercises = new ArrayList<>();
    @ColumnInfo(name = "hour")
    private String Hour;
    @ColumnInfo(name = "is_active")
    private Boolean IsActive;

    public Routine(String StartDate, String FinishDate, ArrayList<String> Days,
                   ArrayList<Integer> Exercises, String Hour, Boolean IsActive) {
        this.StartDate = StartDate;
        this.FinishDate = FinishDate;
        this.Days.addAll(Days);
        this.Exercises.addAll(Exercises);
        this.Hour = Hour;
        this.IsActive = IsActive;
    }

    @NonNull
    public int getId() { return Id; }
    public String getStartDate() { return StartDate; }
    public String getFinishDate() { return FinishDate; }
    public ArrayList<String> getDays() { return Days; }
    public ArrayList<Integer> getExercises() { return Exercises; }
    public String getHour() { return Hour; }
    public Boolean getIsActive() { return IsActive; }

    public void setId(@NonNull int id) { Id = id; }
    public void setStartDate(String startDate) { this.StartDate = startDate; }
    public void setFinishDate(String finishDate) { this.FinishDate = finishDate; }
    public void setDays(ArrayList<String> days) { this.Days = days; }
    public void setExercises(ArrayList<Integer> exercises) { this.Exercises = exercises; }
    public void setHour(String hour) { this.Hour = hour; }
    public void setIsActive(Boolean active) { this.IsActive = active; }

}
