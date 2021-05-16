package edu.band148.wgumanager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.band148.wgumanager.adapter.TermListAdapter;
import edu.band148.wgumanager.model.Assessment;
import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.Instructor;
import edu.band148.wgumanager.model.Report;
import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.viewmodel.AssessmentViewModel;
import edu.band148.wgumanager.viewmodel.CourseViewModel;
import edu.band148.wgumanager.viewmodel.InstructorViewModel;
import edu.band148.wgumanager.viewmodel.TermViewModel;

public class HomeActivity extends AppCompatActivity {
    private static final int CREATE_FILE = 1;
    private ViewModelProvider.AndroidViewModelFactory viewModelFactory;

    PdfFont titleFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("WGU Manager");
        setSupportActionBar(toolbar);

        CardView cardView = findViewById(R.id.schedulerCard);
        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(this, TermActivity.class);
            startActivity(intent);
        });

        CardView reportView = findViewById(R.id.reportCard);
        reportView.setOnClickListener(v -> {
            createReportFile();
        });

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
    }

    private void createReportFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "school_report.pdf");

        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                gatherData(uri);
            }
        }
    }

    private void generatePdf(Uri uri, Report report) {
        try {
            PdfWriter writer = new PdfWriter(getContentResolver().openOutputStream(uri));
            try (Document document = new Document(new PdfDocument(writer))) {
                titleFont = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
                document.setFont(titleFont);

                Calendar calendar = Calendar.getInstance();
                String dateFormat = "MM-dd-yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                document.add(new Paragraph(new Text("School Report - " + simpleDateFormat.format(calendar.getTime()))).setFontSize(24));

                document.add(new Paragraph(new Text("Terms")).setFontSize(20));
                Table termTable = new Table(3);
                termTable.addCell(new Cell(1, 1).add("Term Title"));
                termTable.addCell(new Cell(1, 1).add("Term Start"));
                termTable.addCell(new Cell(1, 1).add("Term End"));
                report.terms.forEach(term -> {
                    termTable.addCell(new Cell(1, 1).add(term.termTitle));
                    termTable.addCell(new Cell(1, 1).add(term.termStart));
                    termTable.addCell(new Cell(1, 1).add(term.termEnd));
                });
                document.add(termTable);

                document.add(new Paragraph(new Text("Courses")).setFontSize(20));
                Table courseTable = new Table(3);
                courseTable.addCell(new Cell(1, 1).add("Course Title"));
                courseTable.addCell(new Cell(1, 1).add("Course Start"));
                courseTable.addCell(new Cell(1, 1).add("Course End"));
                report.courses.forEach(course -> {
                    courseTable.addCell(new Cell(1, 1).add(course.courseTitle));
                    courseTable.addCell(new Cell(1, 1).add(course.courseStart));
                    courseTable.addCell(new Cell(1, 1).add(course.courseEnd));
                });
                document.add(courseTable);

                document.add(new Paragraph(new Text("Assessments")).setFontSize(20));
                Table assessmentTable = new Table(3);
                assessmentTable.addCell(new Cell(1, 1).add("Assessment Title"));
                assessmentTable.addCell(new Cell(1, 1).add("Assessment Start"));
                assessmentTable.addCell(new Cell(1, 1).add("Assessment End"));
                report.assessments.forEach(assessment -> {
                    assessmentTable.addCell(new Cell(1, 1).add(assessment.assessmentTitle));
                    assessmentTable.addCell(new Cell(1, 1).add(assessment.assessmentStart));
                    assessmentTable.addCell(new Cell(1, 1).add(assessment.assessmentEnd));
                });
                document.add(assessmentTable);
                
                document.close();
                Toast.makeText(this, "Report saved!", Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "IO error accessing fonts", Toast.LENGTH_SHORT).show();
        }
    }

    private void gatherData(Uri uri) {
        Report report = new Report();
        TermViewModel termViewModel =  new ViewModelProvider(this, viewModelFactory).get(TermViewModel.class);
        CourseViewModel courseViewModel =  new ViewModelProvider(this, viewModelFactory).get(CourseViewModel.class);
        InstructorViewModel instructorViewModel =  new ViewModelProvider(this, viewModelFactory).get(InstructorViewModel.class);
        AssessmentViewModel assessmentViewModel =  new ViewModelProvider(this, viewModelFactory).get(AssessmentViewModel.class);

        //Create wait observe chain on query results
        termViewModel.getAllTerms().observe(this, new Observer<List<Term>>() {
            @Override
            public void onChanged(List<Term> terms) {
                report.terms = terms;
                courseViewModel.getAllCourses().observe(HomeActivity.this, new Observer<List<Course>>() {
                    @Override
                    public void onChanged(List<Course> courses) {
                        report.courses = courses;
                        instructorViewModel.getAllInstructors().observe(HomeActivity.this, new Observer<List<Instructor>>() {
                            @Override
                            public void onChanged(List<Instructor> instructors) {
                                report.instructors = instructors;
                                assessmentViewModel.getAllAssessments().observe(HomeActivity.this, new Observer<List<Assessment>>() {
                                    @Override
                                    public void onChanged(List<Assessment> assessments) {
                                        report.assessments = assessments;
                                        generatePdf(uri, report);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}