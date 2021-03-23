package edu.band148.wgumanager.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.band148.wgumanager.model.Course;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM course")
    LiveData<List<Course>> getAll();

    @Query("SELECT * FROM course WHERE courseUID=:UID")
    Course findByUID(int UID);

    @Insert
    void insertAll(Course... course);

    @Delete
    void delete(Course course);
}
