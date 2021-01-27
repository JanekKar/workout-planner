package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.ExerciseDao;
import com.example.workoutplanner.database.models.Exercise;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {

    private final LiveData<List<Exercise>> exercises;
    private final ExerciseDao dao;

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        PlannerDatabase db = PlannerDatabase.getDatabase(application);
        exercises = db.exerciseDao().getAll();
        dao = db.exerciseDao();
    }

    public LiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    public LiveData<List<Exercise>> getExercisesById(Long[] objects) {
        return dao.getExercisesById(objects);
    }

    public void insert(Exercise e) {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            dao.insertAll(e);
        });
    }

    public void update(Exercise e) {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            dao.update(e);
        });
    }

    public void delete(Exercise e) {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            dao.delete(e);
        });
    }
}
