package edu.band148.wgumanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    public int assessmentUID;
    public int courseUID;
    public String assessmentType;
    public String startDate;
    public String endDate;
}
