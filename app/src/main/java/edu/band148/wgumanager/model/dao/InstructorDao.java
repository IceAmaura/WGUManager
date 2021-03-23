package edu.band148.wgumanager.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import edu.band148.wgumanager.model.Instructor;

@Dao
public interface InstructorDao {
    @Query("SELECT * FROM instructor")
    LiveData<List<Instructor>> getAll();

    @Query("SELECT * FROM instructor WHERE instructorUID=:UID")
    Instructor findByUID(int UID);

    @Insert
    void insertAll(Instructor... instructors);

    @Delete
    void delete(Instructor instructor);
}
