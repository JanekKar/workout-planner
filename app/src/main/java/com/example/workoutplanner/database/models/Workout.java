package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "workout")
public class Workout implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long workoutId;
    private int weekDay;
    private String name;

    private boolean deleted = false;

    public Workout(int weekDay, String name) {
        this.weekDay = weekDay;
        this.name = name;
    }

    public long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(long workoutId) {
        this.workoutId = workoutId;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + workoutId +
                ", weekDay=" + weekDay +
                ", name='" + name + '\'' +
                '}';
    }
}
