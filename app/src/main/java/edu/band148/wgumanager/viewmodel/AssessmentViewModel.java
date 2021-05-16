package edu.band148.wgumanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.band148.wgumanager.model.Assessment;
import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.model.database.AppRepository;

public class AssessmentViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel(Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allAssessments = appRepository.getAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() { return allAssessments; }

    public LiveData<List<Assessment>> searchAssessments(String search, int courseUID) {
        return appRepository.searchAssessments(search, courseUID);
    }

    public LiveData<List<Assessment>> getAssessmentsByCourse(int courseUID) {
        return appRepository.getAssessmentsByCourse(courseUID);
    }

    public void insert(Assessment assessment) {appRepository.insertAssessment(assessment);}
}
