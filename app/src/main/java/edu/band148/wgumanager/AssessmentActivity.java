package edu.band148.wgumanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.band148.wgumanager.adapter.AssessmentListAdapter;
import edu.band148.wgumanager.model.Assessment;
import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.viewmodel.AssessmentViewModel;

public class AssessmentActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private AssessmentListAdapter mAdapter;
    private AssessmentViewModel assessmentViewModel;
    private CoordinatorLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Assessments");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mRecyclerView = findViewById(R.id.assessmentList);
        final AssessmentListAdapter adapter = new AssessmentListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        assessmentViewModel =  new ViewModelProvider(this, viewModelFactory).get(AssessmentViewModel.class);
        assessmentViewModel.getAssessmentsByCourse(intent.getIntExtra("courseUID", 1)).observe(this, new Observer<List<Assessment>>() {
            @Override
            public void onChanged(List<Assessment> assessments) {
                adapter.setAssessments(assessments);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent newIntent = new Intent(this, EditAssessmentActivity.class);
            newIntent.putExtra("add", true);
            newIntent.putExtra("courseUID", intent.getIntExtra("courseUID", Integer.MAX_VALUE));
            startActivityForResult(newIntent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Assessment tempAssessment = new Assessment();
            if (!data.getBooleanExtra("add", true)) {
                tempAssessment.assessmentUID = data.getIntExtra("assessmentUID", Integer.MAX_VALUE);
            }
            tempAssessment.courseUID = data.getIntExtra("courseUID", Integer.MAX_VALUE);
            tempAssessment.assessmentTitle = data.getStringExtra("assessmentTitle");
            tempAssessment.assessmentType = data.getStringExtra("assessmentType");
            tempAssessment.assessmentStart = data.getStringExtra("assessmentStart");
            tempAssessment.assessmentEnd = data.getStringExtra("assessmentEnd");
            assessmentViewModel.insert(tempAssessment);
        }
    }
}