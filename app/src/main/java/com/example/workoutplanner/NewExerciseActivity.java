package com.example.workoutplanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.workoutplanner.database.ViewModels.ExerciseViewModel;
import com.example.workoutplanner.database.models.Exercise;

public class NewExerciseActivity extends AppCompatActivity {

    long id;
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

        id = getIntent().getLongExtra(ExerciseListActivity.KEY_EXTRA_EEXERCISE_ID, -1);

        if (id != -1) {
            exerciseName.setText(getIntent().getStringExtra(ExerciseListActivity.KEY_EXTRA_EEXERCISE_NAME));
            typeName.setText(getIntent().getStringExtra(ExerciseListActivity.KEY_EXTRA_EEXERCISE_TYPE));
            weight.setChecked(getIntent().getBooleanExtra(ExerciseListActivity.KEY_EXTRA_EEXERCISE_WEIGHT, false));

        }

        ExerciseViewModel evm = ViewModelProviders.of(this).get(ExerciseViewModel.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!exerciseName.getText().toString().isEmpty()) {
                    Exercise e = new Exercise(exerciseName.getText().toString(), typeName.getText().toString(), weight.isChecked());
                    if (id != -1) {
                        e.setExerciseId(id);
                        evm.update(e);
                    } else
                        evm.insert(e);
                    finish();
                }
            }
        });

        this.getSupportActionBar().setSubtitle(R.string.new_exercise_subtitle);

    }

}