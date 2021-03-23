package edu.band148.wgumanager.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Term {
    @PrimaryKey(autoGenerate = true)
    public int termUID;
    public String termTitle;
    public String termStart;
    public String termEnd;
}
