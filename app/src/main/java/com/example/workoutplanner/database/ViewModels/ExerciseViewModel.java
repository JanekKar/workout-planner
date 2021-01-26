package com.example.workoutplanner.database.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.models.Exercise;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {

    private LiveData<List<Exercise>> exercises;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        exercises = PlannerDatabase.getDatabase(application).exerciseDao().getAll();
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    public LiveData<List<Exercise>> getExercisesById(Long[] objects) {
        return PlannerDatabase.getDatabase(getApplication()).exerciseDao().getExercisesById(objects);
    }
}
