package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "workout_set", primaryKeys = {"workoutId", "setId"})
public class WorkoutSet {
    @ForeignKey(entity = Workout.class, parentColumns = {"workoutId"}, childColumns = {"id"}, onDelete = ForeignKey.CASCADE)
    private long workoutId;
    @ForeignKey(entity = Set.class, parentColumns = {"setId"}, childColumns = {"setId"}, onDelete = ForeignKey.CASCADE)
    private long setId;

    public WorkoutSet(long workoutId, long setId) {
        this.workoutId = workoutId;
        this.setId = setId;
    }

    public long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    @Override
    public String toString() {
        return "WorkoutSet{" +
                "workoutId=" + workoutId +
                ", setId=" + setId +
                '}';
    }
}
