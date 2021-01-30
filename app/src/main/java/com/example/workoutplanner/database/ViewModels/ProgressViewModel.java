package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.ProgressDao;
import com.example.workoutplanner.database.models.Progress;

import java.util.ArrayList;
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

    public void insert(Progress p){
        PlannerDatabase.databaseWriteExecutor.execute(()->{
            dao.insertAll(p);
        });
    }

    public LiveData<List<Progress>> getAll(){
        return progressList;
    }

    public Progress getLast(){
        List<Progress> temp = progressList.getValue();

        if(temp == null)
            return null;

        Progress toReturn = temp.get(0);

        for(Progress p : temp){
            if(p.getDate().after(toReturn.getDate())){
                toReturn = p;
            }
        }
        return toReturn;
    }

    //TODO end progress view model
}
