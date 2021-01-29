package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.ProgressDao;
import com.example.workoutplanner.database.models.Progress;

import java.util.List;

public class ProgressViewModel extends AndroidViewModel {

    private final LiveData<List<Progress>> progressList;
    private final ProgressDao dao;

    public ProgressViewModel(@NonNull Application application) {
        super(application);
        PlannerDatabase db = PlannerDatabase.getDatabase(application);
        progressList = db.progressDao().getAll();
        dao = db.progressDao();
    }

    //TODO end progress view model
}
