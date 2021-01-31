package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.workoutplanner.database.models.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Workout... workouts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Workout workouts);

    @Delete
    void delete(Workout workout);

    @Update
    void update(Workout workout);

    @Query("SELECT * FROM workout where deleted=0 order by weekDay")
    LiveData<List<Workout>> getAllActive();

    @Query("SELECT * FROM workout order by weekDay")
    LiveData<List<Workout>> getAll();

    @Query("DELETE FROM workout")
    void deleteAll();

    @Query("SELECT * FROM workout WHERE workoutId=(SELECT max(workoutId) FROM workout)")
    Workout getLast();
}