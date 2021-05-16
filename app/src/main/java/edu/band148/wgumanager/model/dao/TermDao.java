package edu.band148.wgumanager.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import edu.band148.wgumanager.model.Term;

@Dao
public interface TermDao {
    @Query("SELECT * FROM term")
    LiveData<List<Term>> getAll();

    @Query("SELECT * from term WHERE termTitle LIKE '%' || :search || '%'")
    LiveData<List<Term>> searchTerms(String search);

    @Query("SELECT * FROM term WHERE termUID=:UID")
    Term findByUID(int UID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Term... term);

    @Delete
    void delete(Term term);
}
