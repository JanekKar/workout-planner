package com.example.workoutplanner.database.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity
public class DoneSet {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long workoutId;
    private int doneReps;
    @Embedded
    private Set set;
    private Date date;

    public DoneSet(long workoutId, Set set, Date date, int doneReps) {
        this.workoutId = workoutId;
        this.set = set;
        this.date = date;
        this.doneReps = doneReps;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(long workoutId) {
        this.workoutId = workoutId;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDoneReps() {
        return doneReps;
    }

    public void setDoneReps(int doneReps) {
        this.doneReps = doneReps;
    }
}
