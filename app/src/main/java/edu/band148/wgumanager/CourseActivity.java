package edu.band148.wgumanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.band148.wgumanager.adapter.CourseListAdapter;
import edu.band148.wgumanager.model.Course;
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
        courseViewModel.getCoursesByTerm(intent.getIntExtra("termUID", 1)).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                adapter.setCourses(courses);
            }
        });
    }
}