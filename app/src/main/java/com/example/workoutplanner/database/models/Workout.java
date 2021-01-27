package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "workout")
public class Workout {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int weekDay;
    private String name;

    public Workout(int weekDay, String name) {
        this.weekDay = weekDay;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", weekDay=" + weekDay +
                ", name='" + name + '\'' +
                '}';
    }
}
