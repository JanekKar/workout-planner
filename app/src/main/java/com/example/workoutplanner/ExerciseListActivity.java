package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.workoutplanner.database.models.Exercise;

public class ExerciseListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
    }
}