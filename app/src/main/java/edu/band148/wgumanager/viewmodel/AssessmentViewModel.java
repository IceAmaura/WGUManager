package edu.band148.wgumanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.band148.wgumanager.model.Assessment;
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

    public void insert(Assessment assessment) {appRepository.insertAssessment(assessment);}
}
