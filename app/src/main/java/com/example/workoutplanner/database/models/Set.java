package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "set_table")
public class Set {
    @PrimaryKey(autoGenerate = true)
    private int setId;
    private int exerciseId;
    private int numberOfRepsToDO;

    public Set(int exerciseId, int numberOfRepsToDO) {
        this.exerciseId = exerciseId;
        this.numberOfRepsToDO = numberOfRepsToDO;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getNumberOfRepsToDO() {
        return numberOfRepsToDO;
    }

    public void setNumberOfRepsToDO(int numberOfRepsToDO) {
        this.numberOfRepsToDO = numberOfRepsToDO;
    }
}
