package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private long exerciseId;

    private String name;
    private String type;
    private boolean aditiona_weight;

    public Exercise(String name, String type, boolean aditiona_weight) {
        this.name = name;
        this.type = type;
        this.aditiona_weight = aditiona_weight;
    }

    public long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAditiona_weight() {
        return aditiona_weight;
    }

    public void setAditiona_weight(boolean aditiona_weight) {
        this.aditiona_weight = aditiona_weight;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exerciseId=" + exerciseId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", aditiona_weight=" + aditiona_weight +
                '}';
    }
}
