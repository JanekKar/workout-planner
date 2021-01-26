package com.example.workoutplanner.database.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity( tableName = "set_table")
public class Set implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long setId;
    private long exerciseId;
    private int numberOfRepsToDO;
    private int additionalWeight;

    @Ignore
    public static final Parcelable.Creator CREATOR  = new Parcelable.Creator(){
        public Set createFromParcel(Parcel in){
            return new Set(in);
        }

        public Set[] newArray(int size){
            return new Set[size];
        }
    };

    public Set(long exerciseId, int numberOfRepsToDO, int additionalWeight) {
        this.exerciseId = exerciseId;
        this.numberOfRepsToDO = numberOfRepsToDO;
        this.additionalWeight = additionalWeight;
    }

    public Set(Parcel in){
        this.setId = in.readLong();
        this.exerciseId = in.readLong();
        this.numberOfRepsToDO = in.readInt();
        this.additionalWeight = in.readInt();
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

    public int getAdditionalWeight() {
        return additionalWeight;
    }

    public void setAdditionalWeight(int additionalWeight) {
        this.additionalWeight = additionalWeight;
    }

    @Override
    public String toString() {
        return "Set{" +
                "setId=" + setId +
                ", exerciseId=" + exerciseId +
                ", numberOfRepsToDO=" + numberOfRepsToDO +
                ", aditonalWeight=" + additionalWeight +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.setId);
        dest.writeLong(this.exerciseId);
        dest.writeInt(this.numberOfRepsToDO);
        dest.writeInt(this.additionalWeight);
    }
}
