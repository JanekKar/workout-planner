package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.WorkOutDao;
import com.example.workoutplanner.database.models.Workout;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {

    private LiveData<List<Workout>> workouts;
    private WorkOutDao workoutDao;


    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        this.workouts = database.workOutDao().getAll();
        workoutDao = database.workOutDao();
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workouts;
    }

    public long addWorkout(Workout workout){
        final long[] id = new long[1];
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            id[0] = workoutDao.insert(workout);
        });
        return id[0];
//        PlannerDatabase.getDatabase(getApplication()).workOutDao().insertAll(workout);
    }


}
