package com.example.workoutplanner.database.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity
public class Progress {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String photoUri;
    private Date date;
    private String description;
    private int doneSets;
    private int doneWokrout;

    public Progress(long id, String photoUri, Date date, String description, int doneSets, int doneWokrout) {
        this.id = id;
        this.photoUri = photoUri;
        this.date = date;
        this.description = description;
        this.doneSets = doneSets;
        this.doneWokrout = doneWokrout;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDoneSets() {
        return doneSets;
    }

    public void setDoneSets(int doneSets) {
        this.doneSets = doneSets;
    }

    public int getDoneWokrout() {
        return doneWokrout;
    }

    public void setDoneWokrout(int doneWokrout) {
        this.doneWokrout = doneWokrout;
    }
}
