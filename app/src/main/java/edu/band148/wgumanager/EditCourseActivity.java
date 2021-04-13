package edu.band148.wgumanager;

import android.app.AlarmManager;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.band148.wgumanager.adapter.CourseListAdapter;
import edu.band148.wgumanager.adapter.InstructorListAdapter;
import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.Instructor;
import edu.band148.wgumanager.util.WguManagerUtil;
import edu.band148.wgumanager.viewmodel.CourseViewModel;
import edu.band148.wgumanager.viewmodel.InstructorViewModel;

import static edu.band148.wgumanager.util.WguManagerUtil.setCalendar;
import static edu.band148.wgumanager.util.WguManagerUtil.updateLabel;

public class EditCourseActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private InstructorViewModel instructorViewModel;
    private final Calendar startDateCalendar;
    private final Calendar endDateCalendar;

    public EditCourseActivity() {
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();

        mRecyclerView = findViewById(R.id.instructorList);
        final InstructorListAdapter adapter = new InstructorListAdapter(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ViewModelProvider.AndroidViewModelFactory viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        instructorViewModel =  new ViewModelProvider(this, viewModelFactory).get(InstructorViewModel.class);
        instructorViewModel.getInstructorsByCourse(intent.getIntExtra("courseUID", Integer.MAX_VALUE)).observe(this, new Observer<List<Instructor>>() {
            @Override
            public void onChanged(List<Instructor> instructors) {
                adapter.setInstructors(instructors);
            }
        });

        EditText courseNameEdit = findViewById(R.id.courseNameEditText);
        EditText startDateEdit = findViewById(R.id.startCourseDateEdit);
        EditText endDateEdit = findViewById(R.id.endCourseDateEdit);
        EditText noteEdit = findViewById(R.id.notesTextEdit);

        ImageButton startNotificationButton = findViewById(R.id.startNotificationButton);
        ImageButton endNotificationButton = findViewById(R.id.endNotificationButton);

        if (intent.getBooleanExtra("add", true)) {
            toolbar.setTitle("Add Course");
            LinearLayout instructorLabel = findViewById(R.id.instructorLabelLayout);
            instructorLabel.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            startNotificationButton.setVisibility(View.GONE);
            endNotificationButton.setVisibility(View.GONE);
        } else {
            toolbar.setTitle("Edit Course");
        }

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> stringAdapter = ArrayAdapter.createFromResource(this, R.array.status_array, android.R.layout.simple_spinner_item);
        stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringAdapter);
        if (intent.getStringExtra("status") != null) {
            switch (intent.getStringExtra("status")) {
                case "Completed" :
                    spinner.setSelection(1);
                    break;
                case "Dropped" :
                    spinner.setSelection(2);
                    break;
                case "Plan to Take" :
                    spinner.setSelection(3);
                    break;
            }
        }
        
        if (!intent.getBooleanExtra("add", true)) {
            String courseNameString = intent.getStringExtra("courseTitle");
            String courseStartString = intent.getStringExtra("courseStart");
            String courseEndString = intent.getStringExtra("courseEnd");
            String noteString = intent.getStringExtra("note");
            if (courseNameString != null && !courseNameString.isEmpty()) {
                courseNameEdit.setText(courseNameString);
            }
            if (courseStartString != null && !courseStartString.isEmpty()) {
                setCalendar(startDateCalendar, courseStartString);
                updateLabel(startDateCalendar, startDateEdit);
            }
            if (courseEndString != null && !courseEndString.isEmpty()) {
                setCalendar(endDateCalendar, courseEndString);
                updateLabel(endDateCalendar, endDateEdit);
            }
            if (noteString != null && !noteString.isEmpty()) {
                noteEdit.setText(noteString);
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
                new DatePickerDialog(EditCourseActivity.this, startDate, startDateCalendar.get(Calendar.YEAR),
                        startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        endDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditCourseActivity.this, endDate, endDateCalendar.get(Calendar.YEAR),
                        endDateCalendar.get(Calendar.MONTH), endDateCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            if (courseNameEdit.getText().toString().isEmpty() ||
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
                    resultIntent.putExtra("courseUID", intent.getIntExtra("courseUID", Integer.MAX_VALUE));
                }
                resultIntent.putExtra("termUID", intent.getIntExtra("termUID", Integer.MAX_VALUE));
                resultIntent.putExtra("courseTitle", courseNameEdit.getText().toString());
                resultIntent.putExtra("courseStart", startDateEdit.getText().toString());
                resultIntent.putExtra("courseEnd", endDateEdit.getText().toString());
                resultIntent.putExtra("status", (String)spinner.getSelectedItem());
                resultIntent.putExtra("note", noteEdit.getText().toString());
                setResult(1, resultIntent);
                finish();
            }
        });

        ImageButton shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            if (!noteEdit.getText().toString().trim().isEmpty()) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, noteEdit.getText().toString());
                shareIntent.setType("text/plain");
                Intent chooserIntent = Intent.createChooser(shareIntent, null);
                startActivity(chooserIntent);
            }
        });

        ImageButton imageButton = findViewById(R.id.addButton);
        imageButton.setOnClickListener(v -> {
            Intent newIntent = new Intent(this, EditInstructorActivity.class);
            newIntent.putExtra("add", true);
            newIntent.putExtra("courseUID", intent.getIntExtra("courseUID", Integer.MAX_VALUE));
            startActivity(newIntent);
        });

        startNotificationButton.setOnClickListener(v -> {
            if (startDateEdit.length() > 0) {
                Intent startIntent = new Intent(this, NotificationReceiver.class);
                startIntent.putExtra("notification", "Your course starts today!");
                startIntent.setAction(Long.toString(Calendar.getInstance().getTimeInMillis()));
                PendingIntent startIntentPending = PendingIntent.getBroadcast(this, 0, startIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, startDateCalendar.getTimeInMillis(), startIntentPending);
            } else {
                Toast.makeText(this, "Please select a start date!", Toast.LENGTH_SHORT).show();
            }
        });

        endNotificationButton.setOnClickListener(v -> {
            if (endDateEdit.length() > 0) {
                Intent endIntent = new Intent(this, NotificationReceiver.class);
                endIntent.putExtra("notification", "Your course ends today!");
                endIntent.setAction(Long.toString(Calendar.getInstance().getTimeInMillis()));
                PendingIntent endIntentPending = PendingIntent.getBroadcast(this, 0, endIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, endDateCalendar.getTimeInMillis(), endIntentPending);
            } else {
                Toast.makeText(this, "Please select an end date!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}