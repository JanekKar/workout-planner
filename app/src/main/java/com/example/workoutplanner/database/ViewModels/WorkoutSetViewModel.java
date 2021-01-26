package com.example.workoutplanner.database.ViewModels;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.WorkoutSetDao;
import com.example.workoutplanner.database.models.Exercise;
import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSetViewModel extends AndroidViewModel {
    private LiveData<List<WorkoutSet>> workoutSets;
    private WorkoutSetDao workoutSetDao;

    public WorkoutSetViewModel(@NonNull Application application) {
        super(application);
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        this.workoutSets = database.workoutSetDao().getAll();
        this.workoutSetDao = database.workoutSetDao();
    }


    public LiveData<List<WorkoutSet>> getAllWorkoutsSets(){
        return workoutSets;
    }

    public LiveData<List<WorkoutSet>> getAllSets(long workoutId){
        return PlannerDatabase.getDatabase(getApplication()).workoutSetDao().getSets(workoutId);
    }

    public LiveData<List<Long>> getAllExercises(long workoutId){
        return PlannerDatabase.getDatabase(getApplication()).workoutSetDao().getExercises(workoutId);
    }

    public void connectSetToWorkout(WorkoutSet ws){
        PlannerDatabase.databaseWriteExecutor.execute(()->{
            workoutSetDao.insertAll(ws);
        });
    }


}
