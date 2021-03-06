package edu.band148.wgumanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.band148.wgumanager.AssessmentActivity;
import edu.band148.wgumanager.CourseActivity;
import edu.band148.wgumanager.EditCourseActivity;
import edu.band148.wgumanager.EditTermActivity;
import edu.band148.wgumanager.R;
import edu.band148.wgumanager.model.Course;
import edu.band148.wgumanager.model.Term;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {
    private final LayoutInflater mInflater;
    private final Context context;
    private List<Course> mCourseList;

    public CourseListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.courselist_item, parent, false);
        return new CourseViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        if (mCourseList != null) {
            final Course currentCourse = mCourseList.get(position);
            holder.course = currentCourse;
            holder.CourseItemViewText.setText(currentCourse.courseTitle);
        }
    }

    @Override
    public int getItemCount() {
        if (mCourseList != null) {
            return mCourseList.size();
        }
        return 0;
    }

    public void setCourses(List<Course> courses) {
        mCourseList = courses;
        notifyDataSetChanged();
    }

    class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final TextView CourseItemViewText;
        final CourseListAdapter courseListAdapter;
        public Course course = new Course();

        public CourseViewHolder(View itemView, CourseListAdapter adapter) {
            super(itemView);
            CourseItemViewText = itemView.findViewById(R.id.course);
            this.courseListAdapter = adapter;
            itemView.setOnClickListener(this::onClick);
            itemView.setOnLongClickListener(this::onLongClick);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, AssessmentActivity.class);
            intent.putExtra("courseUID", course.courseUID);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            Intent intent = new Intent(context, EditCourseActivity.class);
            intent.putExtra("add", false);
            intent.putExtra("courseUID", course.courseUID);
            intent.putExtra("termUID", course.termUID);
            intent.putExtra("courseTitle", course.courseTitle);
            intent.putExtra("courseStart", course.courseStart);
            intent.putExtra("courseEnd", course.courseEnd);
            intent.putExtra("status", course.status);
            intent.putExtra("note", course.note);
            ((Activity)courseListAdapter.getContext()).startActivityForResult(intent, 1);
            return true;
        }
    }

    public Context getContext() {
        return context;
    }
}
