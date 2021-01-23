package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.workoutplanner.database.models.Set;
import com.example.workoutplanner.database.models.Workout;

import java.util.List;

@Dao
public interface WorkOutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Workout... workouts);

    @Delete
    void delete(Workout workout);

    @Query("SELECT * FROM workout")
    LiveData<List<Workout>> getAll();

    @Query("DELETE FROM workout")
    void deleteAll();

}