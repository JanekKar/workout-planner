package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "workout_set", primaryKeys = {"workoutId", "setId"})
public class WorkoutSet {
    @ForeignKey(entity = Workout.class, parentColumns = {"workoutId"}, childColumns = {"id"}, onDelete = ForeignKey.CASCADE)
    private int workoutId;
    @ForeignKey(entity = Set.class, parentColumns = {"setId"}, childColumns = {"setId"}, onDelete = ForeignKey.CASCADE)
    private int setId;

    public WorkoutSet(int workoutId, int setId) {
        this.workoutId = workoutId;
        this.setId = setId;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
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
