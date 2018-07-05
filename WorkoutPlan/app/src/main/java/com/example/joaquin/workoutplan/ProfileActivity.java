package com.example.joaquin.workoutplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        simpleDateFormat = new SimpleDateFormat("dd/M/yyyy");
        dateTime.setFormat12Hour("E, MMM dd, yyyy hh:mm a");
        exercisesId = new ArrayList<Integer>();
        db = Database.getInstance(ProfileActivity.this);
        if (db.routineDao().countRoutines()== 0){
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivityForResult(intent, 1);
        }
        else{
            int id = db.routineDao().getRoutineId();
            setText(id);

        }

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
        for (Integer exerciseId: exercisesId){
            exercise = db.exerciseDao().getExerciseById(exerciseId);
            ex += "\n"  + exercise.getName() + "(" + exercise.getBodyPart() + "): "
                    + exercise.getSets() + " sets of " + exercise.getRepetitions();
            if(exercise.getIsRepTime()) ex += " seconds each";
            else ex += " repetitions each";
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
        startDate.setText(startDate.getText() + routine.getStartDate());
        finishDate.setText(finishDate.getText() + routine.getFinishDate());

    }
}
