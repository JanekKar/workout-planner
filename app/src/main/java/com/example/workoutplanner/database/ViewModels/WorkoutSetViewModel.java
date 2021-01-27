package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.WorkoutSetDao;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.util.List;

public class WorkoutSetViewModel extends AndroidViewModel {
    private final LiveData<List<WorkoutSet>> workoutSets;
    private final WorkoutSetDao workoutSetDao;

    public WorkoutSetViewModel(@NonNull Application application) {
        super(application);
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        this.workoutSets = database.workoutSetDao().getAll();
        this.workoutSetDao = database.workoutSetDao();
    }


    public LiveData<List<WorkoutSet>> getAllWorkoutsSets() {
        return workoutSets;
    }

    public LiveData<List<WorkoutSet>> getAllSets(long workoutId) {
        return PlannerDatabase.getDatabase(getApplication()).workoutSetDao().getSets(workoutId);
    }

    public LiveData<List<Long>> getAllExercises(long workoutId) {
        return PlannerDatabase.getDatabase(getApplication()).workoutSetDao().getExercises(workoutId);
    }

    public void connectSetToWorkout(WorkoutSet ws) {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            workoutSetDao.insertAll(ws);
        });
    }


}
