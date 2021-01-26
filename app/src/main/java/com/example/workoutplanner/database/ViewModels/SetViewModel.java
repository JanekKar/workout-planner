package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.SetDao;
import com.example.workoutplanner.database.models.Set;

import java.util.List;

public class SetViewModel extends AndroidViewModel {
    private final SetDao setDao;
    private LiveData<List<Set>> sets;

    public SetViewModel(@NonNull Application application) {
        super(application);
        PlannerDatabase database = PlannerDatabase.getDatabase(application);
        this.sets = database.setDao().getAll();
        setDao = database.setDao();
    }

    public LiveData<List<Set>> getAllSets() {
        return this.sets;
    }

    public void addSet(Set set) {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            setDao.insertAll(set);
        });
    }
}
