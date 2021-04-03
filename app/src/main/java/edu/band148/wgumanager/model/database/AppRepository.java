package edu.band148.wgumanager.model.database;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import edu.band148.wgumanager.model.Assessment;
import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.Instructor;
import edu.band148.wgumanager.model.Term;
import edu.band148.wgumanager.model.dao.AssessmentDao;
import edu.band148.wgumanager.model.dao.CourseDao;
import edu.band148.wgumanager.model.dao.InstructorDao;
import edu.band148.wgumanager.model.dao.TermDao;

public class AppRepository {
    private final TermDao termDao;
    private final CourseDao courseDao;
    private final InstructorDao instructorDao;
    private final AssessmentDao assessmentDao;
    private final LiveData<List<Term>> allTerms;
    private final LiveData<List<Course>> allCourses;
    private final LiveData<List<Instructor>> allInstructors;
    private final LiveData<List<Assessment>> allAssessments;
    
    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabaseInstance(application);
        termDao = db.termDao();
        courseDao = db.courseDao();
        instructorDao = db.instructorDao();
        assessmentDao = db.assessmentDao();
        allTerms = termDao.getAll();
        allCourses = courseDao.getAll();
        allInstructors = instructorDao.getAll();
        allAssessments = assessmentDao.getAll();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<Instructor>> getAllInstructors() {
        return allInstructors;
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

    public LiveData<List<Course>> getCoursesByTerm(int termUID) {
        return courseDao.findByTerm(termUID);
    }

    public LiveData<List<Assessment>> getAssessmentsByCourse(int courseUID) {
        return assessmentDao.findByCourse(courseUID);
    }

    public LiveData<List<Instructor>> getInstructorsByCourse(int courseUID) {
        return instructorDao.findByCourse(courseUID);
    }
    
    public void insertTerm(Term term) {
        new insertTermAsyncTask(termDao).execute(term);
    }

    public void insertCourse(Course course) {
        new insertCourseAsyncTask(courseDao).execute(course);
    }

    public void insertInstructor(Instructor instructor) {
        new insertInstructorAsyncTask(instructorDao).execute(instructor);
    }

    public void insertAssessment(Assessment assessment) {
        new insertAssessmentAsyncTask(assessmentDao).execute(assessment);
    }

    private static class insertTermAsyncTask extends AsyncTask<Term, Void, Void> {
        private TermDao asyncTermDao;

        insertTermAsyncTask(TermDao termDao) {
            asyncTermDao = termDao;
        }

        @Override
        protected Void doInBackground(final Term... terms) {
            asyncTermDao.insertAll(terms);
            return null;
        }
    }

    private static class insertCourseAsyncTask extends AsyncTask<Course, Void, Void> {
        private CourseDao asyncCourseDao;

        insertCourseAsyncTask(CourseDao courseDao) {
            asyncCourseDao = courseDao;
        }

        @Override
        protected Void doInBackground(final Course... courses) {
            asyncCourseDao.insertAll(courses);
            return null;
        }
    }

    private static class insertInstructorAsyncTask extends AsyncTask<Instructor, Void, Void> {
        private InstructorDao asyncInstructorDao;

        insertInstructorAsyncTask(InstructorDao instructorDao) {
            asyncInstructorDao = instructorDao;
        }

        @Override
        protected Void doInBackground(final Instructor... instructors) {
            asyncInstructorDao.insertAll(instructors);
            return null;
        }
    }

    private static class insertAssessmentAsyncTask extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDao asyncAssessmentDao;

        insertAssessmentAsyncTask(AssessmentDao assessmentDao) {
            asyncAssessmentDao = assessmentDao;
        }

        @Override
        protected Void doInBackground(final Assessment... assessments) {
            asyncAssessmentDao.insertAll(assessments);
            return null;
        }
    }
}
