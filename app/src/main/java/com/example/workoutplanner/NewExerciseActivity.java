package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.workoutplanner.database.models.Exercise;

public class NewExerciseActivity extends AppCompatActivity {

    private EditText exerciseName;
    private EditText typeName;
    private CheckBox weight;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        exerciseName = findViewById(R.id.exercise_name);
        typeName = findViewById(R.id.type_name);
        weight = findViewById(R.id.additional_weight);
        saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!exerciseName.getText().toString().isEmpty()){
                    Exercise e = new Exercise(exerciseName.getText().toString(), typeName.getText().toString(), weight.isChecked());
                    Log.d("MainActivity", e.toString());
                }
            }
        });
    }

}