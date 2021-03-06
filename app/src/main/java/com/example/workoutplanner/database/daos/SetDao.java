package com.example.workoutplanner.database.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.workoutplanner.database.models.Set;

import java.util.List;

@Dao
public interface SetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Set... sets);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Set sets);

    @Delete
    void delete(Set set);

    @Query("SELECT * FROM set_table")
    LiveData<List<Set>> getAll();

    @Query("SELECT * FROM set_table WHERE setId=:id")
    Set getSet(long id);

    @Query("DELETE FROM set_table")
    void deleteAll();

    @Query("SELECT * FROM set_table WHERE setId IN (:setIds)")
    LiveData<List<Set>> getSets(long[] setIds);
}
