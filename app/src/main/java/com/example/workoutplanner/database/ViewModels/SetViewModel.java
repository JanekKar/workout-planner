package com.example.workoutplanner.database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.workoutplanner.database.PlannerDatabase;
import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.util.List;

public class SetViewModel extends AndroidViewModel {
    private LiveData<List<Set>> sets;

    public SetViewModel(@NonNull Application application) {
        super(application);
        this.sets = PlannerDatabase.getDatabase(application).setDao().getAll();
    }
}
