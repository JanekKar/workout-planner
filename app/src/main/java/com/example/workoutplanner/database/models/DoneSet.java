package com.example.workoutplanner.database.models;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity
public class DoneSet {
    @PrimaryKey(autoGenerate = true)
    private long doneSetId;
    //    private long workoutId;
    private int doneReps;
    @Embedded
    private Set set;
    @Embedded
    private Workout workout;
    private Date date;

    public DoneSet(Workout workout, Set set, Date date, int doneReps) {
        this.doneReps = doneReps;
        this.set = set;
        this.workout = workout;
        this.date = date;
    }

    public long getDoneSetId() {
        return doneSetId;
    }

    public void setDoneSetId(long doneSetId) {
        this.doneSetId = doneSetId;
    }

    public int getDoneReps() {
        return doneReps;
    }

    public void setDoneReps(int doneReps) {
        this.doneReps = doneReps;
    }

    public Set getSet() {
        return set;
    }

    public void setSet(Set set) {
        this.set = set;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DoneSet{" +
                "id=" + doneSetId +
                ", doneReps=" + doneReps +
                ", set=" + set +
                ", workout=" + workout +
                ", date=" + date +
                '}';
    }
}
