package com.example.joaquin.workoutplan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private TextView startDate;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private TextView finishDate;
    private DatePickerDialog.OnDateSetListener finishDateListener;
    private TextView startTime;
    private TimePickerDialog.OnTimeSetListener startTimeListener;
    private Database db;
    private Spinner exerciseSpinner;
    private Button selectExercise;
    private List<Exercise> exercises;
    private ArrayList<String> exerciseNames;
    private ArrayAdapter adapter;
    private List<Exercise> selectedExercises;
    private TextView selectedExercisesTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Create a new Routine");

        startDate = (TextView) findViewById(R.id.startDate);
        finishDate = (TextView) findViewById(R.id.finishDate);
        startTime = (TextView) findViewById(R.id.hour);
        exerciseSpinner = (Spinner) findViewById(R.id.exercises);
        selectExercise = (Button) findViewById(R.id.select_exercise);
        selectedExercises = new ArrayList<Exercise>();
        selectedExercisesTV = (TextView) findViewById(R.id.selected_exercises);

        db = Database.getInstance(MainActivity.this);
        exercises = db.exerciseDao().getAllExercises();
        exerciseNames = new ArrayList<String>();
        for (Exercise exercise: exercises
             ) {
            String item = exercise.getId() + ". " +exercise.getName() + "(" +
                    exercise.getSets() + " sets of " + exercise.getRepetitions();
            if (exercise.getIsRepTime()) item += " sec.";
            else item += "reps.";
            item += ")";
            exerciseNames.add(item);
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, exerciseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(adapter);

        startDate.setOnClickListener(listener);
        finishDate.setOnClickListener(listener);

        selectExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = exerciseSpinner.getSelectedItemPosition() + 1;
                for (Exercise exercise: exercises){

                    if (exercise.getId() == selected){
                        if (selectedExercises.contains(exercise)){
                            Toast.makeText(MainActivity.this, "Cannot add same exercise",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            selectedExercises.add(exercise);
                            String text = selectedExercisesTV.getText().toString();
                            text += " " + exercise.getName() + "|";
                            selectedExercisesTV.setText(text);
                            Toast.makeText(MainActivity.this, "Exercise added successfully",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        startTimeListener,
                        hour, minute,
                        true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        startDateListener = createDateListener(startDateListener, startDate);
        finishDateListener = createDateListener(finishDateListener, finishDate);

        startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = hour + ":" + minute;
                startTime.setText(time);
                startTime.setGravity(Gravity.CENTER);
            }
        };
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog.OnDateSetListener dateSetListener;
            switch (view.getId()){
                case R.id.startDate:
                    dateSetListener = startDateListener;
                    break;
                case R.id.finishDate:
                    dateSetListener = finishDateListener;
                    break;
                default:
                    dateSetListener = startDateListener;
                    break;

            }
            DatePickerDialog dialog = new DatePickerDialog(
                    MainActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    year, month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    };
    private DatePickerDialog.OnDateSetListener createDateListener(
            DatePickerDialog.OnDateSetListener listener, final TextView textView){
        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker,  int year, int month, int day) {
                month = month+1;
                String date = day + "/" + month + "/" + year;
                textView.setText(date);
                textView.setGravity(Gravity.CENTER);
            }
        };
        return listener;
    }

}
