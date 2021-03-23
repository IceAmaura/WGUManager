package edu.band148.wgumanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Instructor {
    @PrimaryKey(autoGenerate = true)
    public int instructorUID;
    public int courseUID;
    public String instructorName;
    public String instructorPhone;
    public String instructorEmail;
}
