package com.example.workoutplanner.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.models.Workout;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {

    private LiveData<List<Workout>> workouts;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        this.workouts = PlannerDatabase.getDatabase(application).workOutDao().getAll();
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workouts;
    }
}
