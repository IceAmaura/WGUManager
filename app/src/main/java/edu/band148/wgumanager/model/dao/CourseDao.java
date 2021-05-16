package edu.band148.wgumanager.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.band148.wgumanager.model.Course;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM course")
    LiveData<List<Course>> getAll();

    @Query("SELECT * from course WHERE (courseTitle LIKE '%' || :search || '%') AND (termUID=:termUID)")
    LiveData<List<Course>> searchCourses(String search, int termUID);

    @Query("SELECT * FROM course WHERE courseUID=:UID")
    Course findByUID(int UID);

    @Query("SELECT * FROM course WHERE termUID=:termUID")
    LiveData<List<Course>> findByTerm(int termUID);

    @Query("SELECT COUNT(courseUID) FROM course WHERE termUID=:termUID")
    LiveData<Integer> getCourseCountByTerm(int termUID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Course... course);

    @Delete
    void delete(Course course);
}
