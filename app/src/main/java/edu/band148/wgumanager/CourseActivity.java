package edu.band148.wgumanager;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.band148.wgumanager.adapter.CourseListAdapter;
import edu.band148.wgumanager.model.Assessment;
import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.viewmodel.CourseViewModel;

public class CourseActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private CourseListAdapter mAdapter;
    private CourseViewModel courseViewModel;
    private CoordinatorLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Courses");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mRecyclerView = findViewById(R.id.courseList);
        final CourseListAdapter adapter = new CourseListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        courseViewModel =  new ViewModelProvider(this, viewModelFactory).get(CourseViewModel.class);
        ImageButton searchButton = findViewById(R.id.searchButton);
        FloatingActionButton fab = findViewById(R.id.fab);
        int termUID = intent.getIntExtra("termUID", Integer.MAX_VALUE);
        if (getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_SEARCH)) {
            Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);
            if (appData != null) {
                termUID = appData.getInt("termUID");
            }
            String query = getIntent().getStringExtra(SearchManager.QUERY);
            courseViewModel.searchCourses(query, termUID).observe(this, new Observer<List<Course>>() {
                @Override
                public void onChanged(List<Course> courses) {
                    adapter.setCourses(courses);
                }
            });
            searchButton.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        } else {
            courseViewModel.getCoursesByTerm(termUID).observe(this, new Observer<List<Course>>() {
                @Override
                public void onChanged(List<Course> courses) {
                    adapter.setCourses(courses);
                }
            });
            int finalTermUID = termUID;
            searchButton.setOnClickListener(v -> {
                onSearchRequested(finalTermUID);
            });
        }

        fab.setOnClickListener(v -> {
            Intent newIntent = new Intent(this, EditCourseActivity.class);
            newIntent.putExtra("add", true);
            newIntent.putExtra("termUID", intent.getIntExtra("termUID", Integer.MAX_VALUE));
            startActivityForResult(newIntent, 1);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Course tempCourse = new Course();
            if (!data.getBooleanExtra("add", true)) {
                tempCourse.courseUID = data.getIntExtra("courseUID", Integer.MAX_VALUE);
            }
            tempCourse.termUID = data.getIntExtra("termUID", Integer.MAX_VALUE);
            tempCourse.courseTitle = data.getStringExtra("courseTitle");
            tempCourse.courseStart = data.getStringExtra("courseStart");
            tempCourse.courseEnd = data.getStringExtra("courseEnd");
            tempCourse.status = data.getStringExtra("status");
            tempCourse.note = data.getStringExtra("note");
            courseViewModel.insert(tempCourse);
        }
    }

    public boolean onSearchRequested(int termUID) {
        Bundle appData = new Bundle();
        appData.putInt("termUID", termUID);
        startSearch(null, false, appData, false);
        return true;
    }
}