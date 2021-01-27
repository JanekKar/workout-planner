package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
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

    @Query("DELETE from workout_set")
    void deleteAll();

    @Query("SELECT * FROM workout_set")
    LiveData<List<WorkoutSet>> getAll();

    @Query("SELECT * FROM workout_set where workoutId=:id")
    LiveData<List<WorkoutSet>> getSets(long id);

    @Query("SELECT distinct e.exerciseId FROM workout_set ws, set_table st, exercise e WHERE ws.setId = st.setId and st.exerciseId = e.exerciseId and workoutId = :workoutId")
    LiveData<List<Long>> getExercises(long workoutId);

}
