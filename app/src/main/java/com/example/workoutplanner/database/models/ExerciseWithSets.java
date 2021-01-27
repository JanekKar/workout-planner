package com.example.workoutplanner.database.models;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;


public class ExerciseWithSets {
    @Embedded
    public Exercise exercise;
    @Relation(
            parentColumn = "exerciseId",
            entityColumn = "exerciseId"
    )
    public LiveData<List<Set>> sets;
}
