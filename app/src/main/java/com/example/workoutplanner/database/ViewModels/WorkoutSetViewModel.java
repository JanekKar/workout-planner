package com.example.workoutplanner.database.ViewModels;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSetViewModel extends AndroidViewModel {
    private LiveData<List<WorkoutSet>> workoutSets;

    public WorkoutSetViewModel(@NonNull Application application) {
        super(application);
        this.workoutSets = PlannerDatabase.getDatabase(application).workoutSetDao().getAll();
    }


    public LiveData<List<WorkoutSet>> getAllWorkoutsSets(){
        return workoutSets;
    }
    public LiveData<List<WorkoutSet>> getAllSets(int workoutId){
        return PlannerDatabase.getDatabase(getApplication()).workoutSetDao().getSets(workoutId);
    }

    public LiveData<List<Integer>> getAllExercises(int workoutId){
        return PlannerDatabase.getDatabase(getApplication()).workoutSetDao().getExercises(workoutId);
    }


}
