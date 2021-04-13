package edu.band148.wgumanager.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.model.database.AppRepository;

public class TermViewModel extends AndroidViewModel {
    private AppRepository appRepository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel (Application application) {
        super(application);
        appRepository = new AppRepository(application);
        allTerms = appRepository.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() { return allTerms; }

    public void insert(Term term) {appRepository.insertTerm(term);}

    public void delete(Term term) {appRepository.deleteTerm(term);}
}
