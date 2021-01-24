package com.example.workoutplanner.database.models;

import androidx.room.Entity;

@Entity(primaryKeys = {"exerciseID", "setId"})
public class ExerciseSetCrossRef {
    private long exerciseId;
    private long setId;
}
