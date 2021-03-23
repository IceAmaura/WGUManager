package edu.band148.wgumanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import edu.band148.wgumanager.adapter.TermListAdapter;
import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.viewmodel.TermViewModel;

public class TermActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private TermListAdapter mAdapter;
    private TermViewModel termViewModel;
    private CoordinatorLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("WGU Manager");
        setSupportActionBar(toolbar);
        mRecyclerView = findViewById(R.id.termList);
        final TermListAdapter adapter = new TermListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        termViewModel =  new ViewModelProvider(this, viewModelFactory).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                adapter.setTerms(terms);
            }
        });
    }
}