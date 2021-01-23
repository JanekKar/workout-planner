package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.workoutplanner.database.models.ExerciseWithSets;

import java.util.List;

@Dao
public interface ExerciseWithSetDao {
    @Transaction
    @Query("SELECT * FROM EXERCISE")
    LiveData<List<ExerciseWithSets>> getExerciseWitsSets();
}
