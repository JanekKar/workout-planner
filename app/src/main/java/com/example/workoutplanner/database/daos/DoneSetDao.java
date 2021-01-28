package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.workoutplanner.database.models.DoneSet;

import java.util.Date;
import java.util.List;

@Dao
public interface DoneSetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DoneSet... doneSet);

    @Delete
    void delete(DoneSet doneSet);

    @Query("SELECT * FROM doneset")
    LiveData<List<DoneSet>> selectAll();

    @Query("SELECT * FROM doneset where date>=:beginningOfWeek and date<=:endOfWeek")
    LiveData<List<DoneSet>> select(Date beginningOfWeek, Date endOfWeek);

    @Query("SELECT * FROM doneset where date>=:beginningOfWeek and date<=:endOfWeek and exerciseId = :exerciseID and workoutId = :workoutId")
    LiveData<List<DoneSet>> select(Date beginningOfWeek, Date endOfWeek, long exerciseID, long workoutId );





}
