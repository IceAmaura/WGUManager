package edu.band148.wgumanager;

import android.app.AlarmManager;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import static edu.band148.wgumanager.util.WguManagerUtil.setCalendar;
import static edu.band148.wgumanager.util.WguManagerUtil.updateLabel;

public class EditAssessmentActivity extends AppCompatActivity {

    private final Calendar startDateCalendar;
    private final Calendar endDateCalendar;

    public EditAssessmentActivity() {
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("add", true)) {
            toolbar.setTitle("Add Assessment");
        } else {
            toolbar.setTitle("Edit Assessment");
        }

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> stringAdapter = ArrayAdapter.createFromResource(this, R.array.test_array, android.R.layout.simple_spinner_item);
        stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringAdapter);
        if (intent.getStringExtra("assessmentType") != null && intent.getStringExtra("assessmentType").equals("Performance")) {
            spinner.setSelection(1);
        }

        EditText assessmentNameEdit = findViewById(R.id.assessmentNameEditText);
        EditText startDateEdit = findViewById(R.id.startAssessmentDateEdit);
        EditText endDateEdit = findViewById(R.id.endAssessmentDateEdit);

        ImageButton startNotificationButton = findViewById(R.id.startNotificationButton);
        ImageButton endNotificationButton = findViewById(R.id.endNotificationButton);
        
        if (!intent.getBooleanExtra("add", true)) {
            String assessmentNameString = intent.getStringExtra("assessmentTitle");
            String assessmentStartString = intent.getStringExtra("assessmentStart");
            String assessmentEndString = intent.getStringExtra("assessmentEnd");
            if (assessmentNameString != null && !assessmentNameString.isEmpty()) {
                assessmentNameEdit.setText(assessmentNameString);
            }
            if (assessmentStartString != null && !assessmentStartString.isEmpty()) {
                setCalendar(startDateCalendar, assessmentStartString);
                updateLabel(startDateCalendar, startDateEdit);
            }
            if (assessmentEndString != null && !assessmentEndString.isEmpty()) {
                setCalendar(endDateCalendar, assessmentEndString);
                updateLabel(endDateCalendar, endDateEdit);
            }
        } else {
            startNotificationButton.setVisibility(View.GONE);
            endNotificationButton.setVisibility(View.GONE);
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
                new DatePickerDialog(EditAssessmentActivity.this, startDate, startDateCalendar.get(Calendar.YEAR),
                        startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        endDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditAssessmentActivity.this, endDate, endDateCalendar.get(Calendar.YEAR),
                        endDateCalendar.get(Calendar.MONTH), endDateCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            if (assessmentNameEdit.getText().toString().isEmpty() ||
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
                    resultIntent.putExtra("assessmentUID", intent.getIntExtra("assessmentUID", Integer.MAX_VALUE));
                }
                resultIntent.putExtra("courseUID", intent.getIntExtra("courseUID", Integer.MAX_VALUE));
                resultIntent.putExtra("assessmentTitle", assessmentNameEdit.getText().toString());
                resultIntent.putExtra("assessmentType", (String)spinner.getSelectedItem());
                resultIntent.putExtra("assessmentStart", startDateEdit.getText().toString());
                resultIntent.putExtra("assessmentEnd", endDateEdit.getText().toString());
                setResult(1, resultIntent);
                finish();
            }
        });

        startNotificationButton.setOnClickListener(v -> {
            if (startDateEdit.length() > 0) {
                Intent startIntent = new Intent(this, NotificationReceiver.class);
                startIntent.putExtra("notification", "Your assessment starts today!");
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
                endIntent.putExtra("notification", "Your assessment ends today!");
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