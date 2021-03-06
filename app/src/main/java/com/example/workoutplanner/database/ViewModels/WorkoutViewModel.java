package com.example.workoutplanner.database.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.NewWorkoutActivity;
import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.WorkoutDao;
import com.example.workoutplanner.database.models.Workout;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {

    private final LiveData<List<Workout>> workouts;
    private final LiveData<List<Workout>> workoutsActive;
    private final WorkoutDao workoutDao;


    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        this.workouts = database.workOutDao().getAll();
        workoutDao = database.workOutDao();
        workoutsActive = workoutDao.getAllActive();
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workoutsActive;
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return workouts;
    }


    public void addWorkout(Workout workout) {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            long test = workoutDao.insert(workout);
            Log.d("MainActivity", test + "");

            NewWorkoutActivity.workoutID = test;
        });
    }

    public void update(Workout w){
        PlannerDatabase.databaseWriteExecutor.execute(()->{
            workoutDao.update(w);
        });
    }


}
