package edu.band148.wgumanager.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.band148.wgumanager.model.Assessment;
import edu.band148.wgumanager.model.Instructor;

@Dao
public interface AssessmentDao {
    @Query("SELECT * FROM assessment")
    LiveData<List<Assessment>> getAll();

    @Query("SELECT * FROM assessment WHERE assessmentUID=:UID")
    Assessment findByUID(int UID);

    @Insert
    void insertAll(Assessment... assessments);

    @Delete
    void delete(Assessment assessment);
}
