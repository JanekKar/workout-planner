package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.workoutplanner.database.models.Progress;

import java.util.List;

@Dao
public interface ProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Progress... progress);

    @Delete
    void delete(Progress... progresses);

    @Query("SELECT * FROM Progress")
    LiveData<List<Progress>> getAll();

    @Query("SELECT * FROM Progress WHERE id = :id")
    Progress get(long id);

    @Query("SELECT * FROM Progress WHERE id = (SELECT MAX(id) FROM PROGRESS)")
    Progress getLast();
}
