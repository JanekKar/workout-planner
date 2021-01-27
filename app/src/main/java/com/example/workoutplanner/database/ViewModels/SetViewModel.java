package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.NewWorkoutActivity;
import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.daos.SetDao;
import com.example.workoutplanner.database.models.Set;

import java.util.List;

public class SetViewModel extends AndroidViewModel {
    private final SetDao setDao;
    private final LiveData<List<Set>> sets;

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

    public void addSets(List<Set> setList) {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            long[] idArr = new long[setList.size()];
            for (int i = 0; i < setList.size(); i++) {
                idArr[i] = setDao.insert(setList.get(i));
            }
            NewWorkoutActivity.setsId = idArr;
        });
    }

    public LiveData<List<Set>> getSets(long[] setIds) {
        return PlannerDatabase.getDatabase(getApplication()).setDao().getSets(setIds);
    }
}
