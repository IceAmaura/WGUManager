package edu.band148.wgumanager;

import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import edu.band148.wgumanager.model.Instructor;
import edu.band148.wgumanager.viewmodel.CourseViewModel;
import edu.band148.wgumanager.viewmodel.InstructorViewModel;

import static edu.band148.wgumanager.util.WguManagerUtil.setCalendar;
import static edu.band148.wgumanager.util.WguManagerUtil.updateLabel;

public class EditInstructorActivity extends AppCompatActivity {

    private InstructorViewModel instructorViewModel;
    private final Calendar startDateCalendar;
    private final Calendar endDateCalendar;

    public EditInstructorActivity() {
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_instructor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("add", true)) {
            toolbar.setTitle("Add Instructor");
        } else {
            toolbar.setTitle("Edit Instructor");
        }

        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        instructorViewModel =  new ViewModelProvider(this, viewModelFactory).get(InstructorViewModel.class);

        EditText instructorNameEdit = findViewById(R.id.instructorNameEditText);
        EditText phoneEdit = findViewById(R.id.instructorPhoneEditText);
        EditText emailEdit = findViewById(R.id.instructorEmailEditText);
        
        if (!intent.getBooleanExtra("add", true)) {
            String instructorNameString = intent.getStringExtra("instructorName");
            String instructorEmailString = intent.getStringExtra("instructorEmail");
            String instructorPhoneString = intent.getStringExtra("instructorPhone");
            if (instructorNameString != null && !instructorNameString.isEmpty()) {
                instructorNameEdit.setText(instructorNameString);
            }
            if (instructorEmailString != null && !instructorEmailString.isEmpty()) {
                emailEdit.setText(instructorEmailString);
            }
            if (instructorPhoneString != null && !instructorPhoneString.isEmpty()) {
                phoneEdit.setText(instructorPhoneString);
            }
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            if (instructorNameEdit.getText().toString().isEmpty() ||
                    phoneEdit.getText().toString().isEmpty() ||
                    emailEdit.getText().toString().isEmpty()) {
                new Builder(this)
                        .setMessage("Please make sure all fields are filled in!")
                        .create()
                        .show();
            } else {
                Instructor tempInstructor = new Instructor();
                if (!intent.getBooleanExtra("add", true)) {
                    tempInstructor.instructorUID = intent.getIntExtra("instructorUID", Integer.MAX_VALUE);
                }
                tempInstructor.courseUID = intent.getIntExtra("courseUID", Integer.MAX_VALUE);
                tempInstructor.instructorName = instructorNameEdit.getText().toString();
                tempInstructor.instructorPhone = phoneEdit.getText().toString();
                tempInstructor.instructorEmail = emailEdit.getText().toString();
                instructorViewModel.insert(tempInstructor);
                finish();
            }
        });
    }
}