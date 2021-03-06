package com.example.joaquin.workoutplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Database db;
    private Routine routine;
    private TextView exercisesTV;
    private String ex;
    private ArrayList<Integer> exercisesId;
    private Exercise exercise;
    private Button plus;
    private Button minus;
    private TextClock dateTime;
    private TextView progress;
    private TextView startDate;
    private TextView finishDate;
    private SimpleDateFormat simpleDateFormat;
    private List<Routine> routines;
    private boolean isAnyActive;
    private Button createNewRoutine;
    private Intent intent;
    private TextView startHour;
    private ArrayList<String> daysList;
    private TextView daysInRoutine;
    private String daysToSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        exercisesTV = (TextView) findViewById(R.id.exercises);
        ex = exercisesTV.getText().toString();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        dateTime = (TextClock) findViewById(R.id.date_time);
        progress = (TextView) findViewById(R.id.progress);
        startDate = (TextView) findViewById(R.id.startDate);
        finishDate = (TextView) findViewById(R.id.finishDate);
        createNewRoutine = (Button) findViewById(R.id.new_routine);
        startHour = (TextView) findViewById(R.id.start_hour);
        daysInRoutine = (TextView) findViewById(R.id.days_in_routine);
        simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
        dateTime.setFormat12Hour("E, MMM dd, yyyy hh:mm a");
        exercisesId = new ArrayList<Integer>();
        db = Database.getInstance(ProfileActivity.this);
        routines = db.routineDao().getAllRoutines();

        if (db.routineDao().countRoutines()== 0){
            intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivityForResult(intent, 1);
        }

        else{
            Log.d("Count", String.valueOf(db.routineDao().countRoutines()));
            for (Routine r: routines){
                if (r.getIsActive()) {
                    isAnyActive = true;
                    break;
                }
            }
            if (!isAnyActive)
            {
                intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivityForResult(intent, 1);
            }
            int id = db.routineDao().getRoutineId();
            setText(id);

        }

        createNewRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progressBar.getMax() > progressBar.getProgress()) {
                    progressBar.incrementProgressBy(1);
                    progress.setText(progressBar.getProgress() + "/" + progressBar.getMax());
                    plus.setEnabled(false);
                    plus.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            plus.setEnabled(true);
                        }
                    }, 24 * 60 * 60 * 1000);
                    Toast.makeText(ProfileActivity.this,
                            "You completed your exercises for today!", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(ProfileActivity.this,
                        "Routine already completed!", Toast.LENGTH_SHORT).show();
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 < progressBar.getProgress()) {
                    progressBar.incrementProgressBy(-1);
                    progress.setText(progressBar.getProgress() + "/" + progressBar.getMax());
                    plus.setEnabled(true);
                }
                else Toast.makeText(ProfileActivity.this,
                        "Cannot go to negative", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            int id = data.getIntExtra("Routine_id", 0);
            setText(id);
        }
    }

    private void setText(int id){
        routine = db.routineDao().getRoutineById(id);
        exercisesId = routine.getExercises();
        daysList = routine.getDays();
        ex = "Exercises:";
        daysToSet = "Days:";

        for (Integer exerciseId: exercisesId){
            exercise = db.exerciseDao().getExerciseById(exerciseId);
            ex += "\n"  + exercise.getName() + "(" + exercise.getBodyPart() + "): "
                    + exercise.getSets() + " sets of " + exercise.getRepetitions();
            if(exercise.getIsRepTime()) ex += " seconds each";
            else ex += " repetitions each";
        }
        for (String day: daysList){
            daysToSet += "\n" + day;
        }
        try{
            Date start = simpleDateFormat.parse(routine.getStartDate());
            Date finish = simpleDateFormat.parse(routine.getFinishDate());
            long diff = Math.abs(start.getTime() - finish.getTime());
            long diffDays = diff/(24*60*60*1000);
            progressBar.setMax((int) diffDays);
            progress.setText(0 + "/" + diffDays);
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        exercisesTV.setText(ex);
        startDate.setText("Started in: " + routine.getStartDate());
        finishDate.setText("Finishes: " + routine.getFinishDate());
        startHour.setText("Starting Hour: "+ routine.getHour());
        daysInRoutine.setText(daysToSet);

    }
}
