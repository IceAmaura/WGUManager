package edu.band148.wgumanager.model.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import edu.band148.wgumanager.model.Assessment;
import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.Instructor;
import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.model.dao.AssessmentDao;
import edu.band148.wgumanager.model.dao.CourseDao;
import edu.band148.wgumanager.model.dao.InstructorDao;
import edu.band148.wgumanager.model.dao.TermDao;

@Database(entities = {Term.class, Course.class, Instructor.class, Assessment.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract InstructorDao instructorDao();
    public abstract AssessmentDao assessmentDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabaseInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            "app_database").fallbackToDestructiveMigration().
                            addCallback(roomCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final TermDao termDao;
        private final CourseDao courseDao;
        private final InstructorDao instructorDao;
        private final AssessmentDao assessmentDao;

        PopulateDbAsync(AppDatabase db) {
            termDao = db.termDao();
            courseDao = db.courseDao();
            instructorDao = db.instructorDao();
            assessmentDao = db.assessmentDao();
        }

        //Construct initial data
        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 2; i++) {
                Term tempTerm = new Term();
                tempTerm.termTitle = "Term " + (i + 1);
                tempTerm.termStart = "01-01-2021";
                tempTerm.termEnd = "06-01-2021";
                termDao.insertAll(tempTerm);
                for (int j = 0; j < 2; j++) {
                    Course tempCourse = new Course();
                    tempCourse.termUID = i + 1;
                    tempCourse.courseTitle = "Course " + (j + 1) + " - Term " + (i + 1);
                    tempCourse.startDate = "01-01-2021";
                    tempCourse.endDate = "06-01-2021";
                    tempCourse.status = "In Progress";
                    tempCourse.note = "Test Note";
                    courseDao.insertAll(tempCourse);
                    for (int k = 0; k < 2; k++) {
                        Assessment tempAssessment = new Assessment();
                        Instructor tempInstructor = new Instructor();
                        if (i == 0) {
                            tempAssessment.courseUID = j + 1;
                            tempInstructor.courseUID = j + 1;
                        } else {
                            tempAssessment.courseUID = j + 3;
                            tempInstructor.courseUID = j + 3;
                        }

                        tempAssessment.assessmentTitle = "Test " + (k + 1);
                        tempAssessment.assessmentType = (k == 0) ? "Objective" : "Performance";
                        tempAssessment.startDate = "01-01-2021";
                        tempAssessment.endDate = "06-01-2021";
                        assessmentDao.insertAll(tempAssessment);

                        tempInstructor.instructorName = (k == 0) ? "John Doe" : "Jill Johnson";
                        tempInstructor.instructorPhone = "555-555-5555";
                        tempInstructor.instructorEmail = "example@example.com";
                        instructorDao.insertAll(tempInstructor);
                    }
                }
            }

            return null;
        }
    }
}
