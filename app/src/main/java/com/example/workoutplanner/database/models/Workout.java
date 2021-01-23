package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;


@Entity(tableName = "workout")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int weekDay;
    private int setId;

    public Workout(int weekDay, int setId) {
        this.weekDay = weekDay;
        this.setId = setId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }
}
