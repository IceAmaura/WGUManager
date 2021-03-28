package edu.band148.wgumanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.band148.wgumanager.CourseActivity;
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

        public CourseViewHolder(View itemView, CourseListAdapter adapter) {
            super(itemView);
            CourseItemViewText = itemView.findViewById(R.id.course);
            this.courseListAdapter = adapter;
        }

        @Override
        public void onClick(View v) {
            //TODO
            Intent intent = new Intent(context, CourseActivity.class);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}
