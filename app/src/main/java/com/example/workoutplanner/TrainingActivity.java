package com.example.workoutplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutplanner.database.ViewModels.ExerciseViewModel;
import com.example.workoutplanner.database.models.Set;

import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity {

    private long workoutId;
    private String exerciseName;

    private int currentSet = 0;

    private TextView exerciseNameTextView;
    private TextView setNumberTextView;
    private TextView repsToDoTextView;
    private EditText repsDoneEditText;
    private Button nextSetButton;

    private ArrayList<Set> setList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        exerciseNameTextView = findViewById(R.id.exercise_name);
        setNumberTextView = findViewById(R.id.set_number);
        repsToDoTextView = findViewById(R.id.reps_to_do);
        repsDoneEditText = findViewById(R.id.reps_done);
        nextSetButton = findViewById(R.id.next_set_button);

        setList = getIntent().getParcelableArrayListExtra(WorkoutActivity.EXERCISE_SET_LIST_EXTRA);
        exerciseName = getIntent().getStringExtra(WorkoutActivity.EXERCISE_NAME_EXTRA);
        if (setList==null || setList.isEmpty()){
            finish();
            //TODO Nie wiem jeszcze co wtedy
        }

        exerciseNameTextView.setText(exerciseName);


        setAllFields();


        nextSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(++currentSet < setList.size()){
                    //TODO save set as done
                    setAllFields();
                    if(currentSet+1 == setList.size()){
                        nextSetButton.setText(R.string.last_set);
                    }
                }else{
                    finish();
                }
            }
        });
    }

    private void setAllFields() {
        setNumberTextView.setText(getResources().getString(R.string.set_number, currentSet+1, setList.size()));
        repsToDoTextView.setText(setList.get(currentSet).getNumberOfRepsToDO()+"");
        repsDoneEditText.setText(setList.get(currentSet).getNumberOfRepsToDO()+"");
    }
}