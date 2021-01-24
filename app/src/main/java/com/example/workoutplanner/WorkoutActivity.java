package com.example.workoutplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.workoutplanner.database.ViewModels.WorkoutSetViewModel;
import com.example.workoutplanner.database.ViewModels.WorkoutViewModel;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    private long workoutId;
    private Workout workout = null;

    private TextView workoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        workoutName = findViewById(R.id.workout_name);

        workoutId = getIntent().getLongExtra(MainActivity.WORKOUT_ID_EXTRA, -1);


        WorkoutSetViewModel workoutSetViewModel = ViewModelProviders.of(this).get(WorkoutSetViewModel.class);

        WorkoutViewModel workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        LifecycleOwner owner = this;

        workoutViewModel.getWorkouts().observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                for (Workout w: workouts){
                    if(w.getId() == workoutId){
                        workout = w;
                        workoutName.setText(w.getName());

                        workoutSetViewModel.getAllSets(w.getId()).observe(owner, new Observer<List<WorkoutSet>>() {
                            @Override
                            public void onChanged(List<WorkoutSet> workoutSets) {
                                for(WorkoutSet ws : workoutSets){
                                    ws.getSetId();
                                }
                                Log.d("MainActivity", workoutSets+"");
                            }
                        });

                    }
                }
            }
        });


    }

    public void ExerciseInSets(){

    }
}