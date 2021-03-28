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
import edu.band148.wgumanager.model.Assessment;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.AssessmentViewHolder> {
    private final LayoutInflater mInflater;
    private final Context context;
    private List<Assessment> mAssessmentList;

    public AssessmentListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assessmentlist_item, parent, false);
        return new AssessmentViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        if (mAssessmentList != null) {
            final Assessment current = mAssessmentList.get(position);
            holder.AssessmentItemViewText.setText(current.assessmentTitle);
        }
    }

    @Override
    public int getItemCount() {
        if (mAssessmentList != null) {
            return mAssessmentList.size();
        }
        return 0;
    }

    public void setAssessments(List<Assessment> assessments) {
        mAssessmentList = assessments;
        notifyDataSetChanged();
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final TextView AssessmentItemViewText;
        final AssessmentListAdapter assessmentListAdapter;

        public AssessmentViewHolder(View itemView, AssessmentListAdapter adapter) {
            super(itemView);
            AssessmentItemViewText = itemView.findViewById(R.id.assessment);
            this.assessmentListAdapter = adapter;
            itemView.setOnLongClickListener(this::onLongClick);
        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}
