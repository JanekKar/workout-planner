package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.example.workoutplanner.database.ExerciseViewModel;
import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.models.Exercise;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExerciseViewModel e = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        e.getExercises().observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                System.out.println(exercises);
            }
        });

    }
}