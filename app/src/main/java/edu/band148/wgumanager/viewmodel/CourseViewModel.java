package edu.band148.wgumanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.model.database.AppRepository;

public class CourseViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<List<Course>> allCourses;

    public CourseViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allCourses = appRepository.getAllCourses();
    }

    public LiveData<List<Course>> getCoursesByTerm(int termUID) {
        return appRepository.getCoursesByTerm(termUID);
    }

    public LiveData<List<Course>> getAllCourses() { return allCourses; }

    public LiveData<List<Course>> searchCourses(String search, int termUID) {
        return appRepository.searchCourses(search, termUID);
    }

    public LiveData<Integer> getCourseCountByTerm(int termUID) {
        return appRepository.getCourseCountByTerm(termUID);
    }

    public void insert(Course course) {appRepository.insertCourse(course);}

    public void delete(Course course) {appRepository.deleteCourse(course);}
}
