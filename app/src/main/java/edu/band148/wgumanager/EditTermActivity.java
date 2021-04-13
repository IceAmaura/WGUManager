package edu.band148.wgumanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.viewmodel.CourseViewModel;
import edu.band148.wgumanager.viewmodel.TermViewModel;

import static edu.band148.wgumanager.util.WguManagerUtil.setCalendar;
import static edu.band148.wgumanager.util.WguManagerUtil.updateLabel;

public class EditTermActivity extends AppCompatActivity {

    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private final Calendar startDateCalendar;
    private final Calendar endDateCalendar;

    public EditTermActivity() {
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        courseViewModel =  new ViewModelProvider(this, viewModelFactory).get(CourseViewModel.class);
        termViewModel =  new ViewModelProvider(this, viewModelFactory).get(TermViewModel.class);

        ImageButton deleteButton = findViewById(R.id.deleteButton);
        if (intent.getBooleanExtra("add", true)) {
            toolbar.setTitle("Add Term");
            deleteButton.setVisibility(View.GONE);
        } else {
            toolbar.setTitle("Edit Term");
        }

        EditText termNameEdit = findViewById(R.id.termNameEditText);
        EditText startDateEdit = findViewById(R.id.startTermDateEdit);
        EditText endDateEdit = findViewById(R.id.endTermDateEdit);
        
        if (!intent.getBooleanExtra("add", true)) {
            String termNameString = intent.getStringExtra("termTitle");
            String termStartString = intent.getStringExtra("termStart");
            String termEndString = intent.getStringExtra("termEnd");
            if (termNameString != null && !termNameString.isEmpty()) {
                termNameEdit.setText(termNameString);
            }
            if (termStartString != null && !termStartString.isEmpty()) {
                setCalendar(startDateCalendar, termStartString);
                updateLabel(startDateCalendar, startDateEdit);
            }
            if (termEndString != null && !termEndString.isEmpty()) {
                setCalendar(endDateCalendar, termEndString);
                updateLabel(endDateCalendar, endDateEdit);
            }
        }

        DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startDateCalendar.set(Calendar.YEAR, year);
                startDateCalendar.set(Calendar.MONTH, month);
                startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(startDateCalendar, startDateEdit);
            }
        };

        DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endDateCalendar.set(Calendar.YEAR, year);
                endDateCalendar.set(Calendar.MONTH, month);
                endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(endDateCalendar, endDateEdit);
            }
        };
        
        startDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTermActivity.this, startDate, startDateCalendar.get(Calendar.YEAR),
                        startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        endDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTermActivity.this, endDate, endDateCalendar.get(Calendar.YEAR),
                        endDateCalendar.get(Calendar.MONTH), endDateCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            if (termNameEdit.getText().toString().isEmpty() ||
                    startDateEdit.getText().toString().isEmpty() ||
                    endDateEdit.getText().toString().isEmpty()) {
                new Builder(this)
                        .setMessage("Please make sure all fields are filled in!")
                        .create()
                        .show();
            } else {
                Intent resultIntent = new Intent();
                if (!intent.getBooleanExtra("add", true)) {
                    resultIntent.putExtra("add", false);
                    resultIntent.putExtra("termUID", intent.getIntExtra("termUID", Integer.MAX_VALUE));
                }
                resultIntent.putExtra("termTitle", termNameEdit.getText().toString());
                resultIntent.putExtra("termStart", startDateEdit.getText().toString());
                resultIntent.putExtra("termEnd", endDateEdit.getText().toString());
                setResult(1, resultIntent);
                finish();
            }
        });

        deleteButton.setOnClickListener(v -> {
            courseViewModel.getCourseCountByTerm(intent.getIntExtra("termUID", Integer.MAX_VALUE)).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer integer) {
                    if (integer == 0) {
                        Term tempTerm = new Term();
                        tempTerm.termUID = intent.getIntExtra("termUID", Integer.MAX_VALUE);
                        termViewModel.delete(tempTerm);
                        finish();
                    } else if (integer > 0) {
                        Toast.makeText(EditTermActivity.this, "Can't delete a term with attached courses!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });
    }
}