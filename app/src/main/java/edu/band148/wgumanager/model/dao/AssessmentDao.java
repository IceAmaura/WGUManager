package edu.band148.wgumanager.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.band148.wgumanager.model.Assessment;

@Dao
public interface AssessmentDao {
    @Query("SELECT * FROM assessment")
    LiveData<List<Assessment>> getAll();

    @Query("SELECT * from assessment WHERE assessmentTitle LIKE '%' || :search || '%' AND courseUID=:courseUID")
    LiveData<List<Assessment>> searchAssessments(String search, int courseUID);

    @Query("SELECT * FROM assessment WHERE assessmentUID=:UID")
    Assessment findByUID(int UID);

    @Query("SELECT * FROM assessment WHERE courseUID=:courseUID")
    LiveData<List<Assessment>> findByCourse(int courseUID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Assessment... assessments);

    @Delete
    void delete(Assessment assessment);
}
