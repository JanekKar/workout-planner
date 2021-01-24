package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "set_table")
public class Set {
    @PrimaryKey(autoGenerate = true)
    private long setId;
    private long exerciseId;
    private int numberOfRepsToDO;
    private int aditonalWeight;

    public Set(long exerciseId, int numberOfRepsToDO, int aditonalWeight) {
        this.exerciseId = exerciseId;
        this.numberOfRepsToDO = numberOfRepsToDO;
        this.aditonalWeight = aditonalWeight;
    }

    public long getSetId() {
        return setId;
    }

    public void setSetId(long setId) {
        this.setId = setId;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getNumberOfRepsToDO() {
        return numberOfRepsToDO;
    }

    public void setNumberOfRepsToDO(int numberOfRepsToDO) {
        this.numberOfRepsToDO = numberOfRepsToDO;
    }

    public int getAditonalWeight() {
        return aditonalWeight;
    }

    public void setAditonalWeight(int aditonalWeight) {
        this.aditonalWeight = aditonalWeight;
    }

    @Override
    public String toString() {
        return "Set{" +
                "setId=" + setId +
                ", exerciseId=" + exerciseId +
                ", numberOfRepsToDO=" + numberOfRepsToDO +
                ", aditonalWeight=" + aditonalWeight +
                '}';
    }
}
