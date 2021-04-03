package edu.band148.wgumanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Course {
    @PrimaryKey(autoGenerate = true)
    public int courseUID;
    public int termUID;
    public String courseTitle;
    public String courseStart;
    public String courseEnd;
    public String status;
    public String note;
}