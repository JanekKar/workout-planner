package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.workoutplanner.database.models.Workout;
import com.example.workoutplanner.database.models.WorkoutSet;

import java.util.List;

@Dao
public interface WorkoutSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(WorkoutSet... ws);

    @Query("SELECT * FROM workout_set")
    LiveData<List<WorkoutSet>> getAll();

    @Query("SELECT * FROM workout_set where workoutId=:id")
    LiveData<List<WorkoutSet>> getSets(int id);

    @Query("SELECT distinct e.exerciseId FROM workout_set ws, set_table st, exercise e WHERE ws.setId = st.setId and st.exerciseId = e.exerciseId and workoutId = :workoutId")
    LiveData<List<Integer>> getExercises(int workoutId);

}
