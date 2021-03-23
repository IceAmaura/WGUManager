package edu.band148.wgumanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Course {
    @PrimaryKey(autoGenerate = true)
    public int courseUID;
    public String courseTitle;
    public String startDate;
    public String endDate;
    public String status;
    public String note;
}