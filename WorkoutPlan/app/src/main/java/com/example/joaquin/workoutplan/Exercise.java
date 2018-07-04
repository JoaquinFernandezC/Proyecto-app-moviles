package com.example.joaquin.workoutplan;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "exercise_table")
public class Exercise {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int Id;
    @ColumnInfo(name="name")
    private String Name;
    @ColumnInfo(name="body_part")
    private String BodyPart;
    @ColumnInfo(name="repetitions")
    private int Repetitions;
    @ColumnInfo(name="is_time")
    private Boolean IsRepTime;
    @ColumnInfo(name="sets")
    private int Sets;

    public Exercise(String Name, String BodyPart, Boolean IsRepTime, int Sets, int Repetitions){
        setName(Name);
        setBodyPart(BodyPart);
        setIsRepTime(IsRepTime);
        setSets(Sets);
        setRepetitions(Repetitions);
    }

    @NonNull
    public int getId() { return Id; }
    public Boolean getIsRepTime() { return IsRepTime; }
    public Integer getRepetitions() { return Repetitions; }
    public Integer getSets() { return Sets; }
    public String getBodyPart() { return BodyPart; }
    public String getName() { return Name; }

    public void setId(@NonNull int id) { this.Id = id; }
    public void setBodyPart(String bodyPart) { this.BodyPart = bodyPart; }
    public void setIsRepTime(Boolean isRepTime) { this.IsRepTime = isRepTime; }
    public void setName(String name) { this.Name = name; }
    public void setRepetitions(Integer repetitions) { this.Repetitions = repetitions; }
    public void setSets(Integer sets) { this.Sets = sets; }

    public static Exercise[] populateData(){
        return new Exercise[]{
                new Exercise("PushUps", "Upper Body", false,
                        4, 15),
                new Exercise("Crunches", "Abs", false,
                        4, 20),
                new Exercise("Burpees", "Lower Body", true,
                        5, 40),
                new Exercise("Jumping Jacks", "Lower Body", false,
                        4, 30),
                new Exercise("Shoulder Taps", "Upper Body", false,
                        4, 15)
        };
    }
}
