package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.workoutplanner.database.models.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Exercise... exercises);

    @Delete
    void delete(Exercise exercise);

    @Query("SELECT * FROM EXERCISE WHERE exerciseId=:id")
    Exercise getExercise(long id);

    @Query("SELECT * FROM EXERCISE")
    LiveData<List<Exercise>> getAll();

    @Query("DELETE FROM EXERCISE")
    void deleteAll();

    @Query("SELECT * FROM exercise where exerciseId in (:idList)")
    LiveData<List<Exercise>> getExercisesById(Long[] idList);
}
