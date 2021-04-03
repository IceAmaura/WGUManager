package edu.band148.wgumanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Assessment {
    @PrimaryKey(autoGenerate = true)
    public int assessmentUID;
    public int courseUID;
    public String assessmentTitle;
    public String assessmentType;
    public String assessmentStart;
    public String assessmentEnd;
}
