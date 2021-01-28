package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.DoneSetDao;
import com.example.workoutplanner.database.models.DoneSet;

import java.util.Date;
import java.util.List;

public class DoneSetViewModel extends AndroidViewModel {
    private final DoneSetDao dao;
    private final PlannerDatabase db;

    public DoneSetViewModel(@NonNull Application application) {
        super(application);
        db = PlannerDatabase.getDatabase(application);
        dao = db.doneSetDao();
    }

    public LiveData<List<DoneSet>> getAll(){
        return dao.selectAll();
    }

    public void insert(DoneSet ds){
        PlannerDatabase.databaseWriteExecutor.execute(()->{
            dao.insertAll(ds);
        });
    }

    public LiveData<List<DoneSet>> get(Date beginning, Date end, long exerciseId, long workoutId){
        return dao.select(beginning, end, exerciseId, workoutId);
    }

    public LiveData<List<DoneSet>> get(Date beginning, Date end, long workoutId){
        return dao.select(beginning, end, workoutId);
    }

    public LiveData<List<DoneSet>> get(Date beginning, Date end){
        return dao.select(beginning, end);
    }
}
