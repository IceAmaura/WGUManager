package edu.band148.wgumanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.band148.wgumanager.model.Instructor;
import edu.band148.wgumanager.model.Instructor;
import edu.band148.wgumanager.model.database.AppRepository;

public class InstructorViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<List<Instructor>> allInstructors;

    public InstructorViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allInstructors = appRepository.getAllInstructors();
    }

    public LiveData<List<Instructor>> getAllInstructors() { return allInstructors; }

    public LiveData<List<Instructor>> getInstructorsByCourse(int courseUID) {
        return appRepository.getInstructorsByCourse(courseUID);
    }

    public void insert(Instructor instructor) {appRepository.insertInstructor(instructor);}
}
