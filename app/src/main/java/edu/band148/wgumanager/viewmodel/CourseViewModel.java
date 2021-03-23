package edu.band148.wgumanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.database.AppRepository;

public class CourseViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<List<Course>> allCourses;

    public CourseViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allCourses = appRepository.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() { return allCourses; }

    public void insert(Course course) {appRepository.insertCourse(course);}
}