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

import edu.band148.wgumanager.EditInstructorActivity;
import edu.band148.wgumanager.R;
import edu.band148.wgumanager.model.Instructor;

public class InstructorListAdapter extends RecyclerView.Adapter<InstructorListAdapter.InstructorViewHolder> {
    private final LayoutInflater mInflater;
    private final Context context;
    private List<Instructor> mInstructorList;

    public InstructorListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public InstructorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.instructorlist_item, parent, false);
        return new InstructorViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorViewHolder holder, int position) {
        if (mInstructorList != null) {
            final Instructor current = mInstructorList.get(position);
            holder.instructor = current;
            holder.InstructorItemViewText.setText(current.instructorName);
        }
    }

    @Override
    public int getItemCount() {
        if (mInstructorList != null) {
            return mInstructorList.size();
        }
        return 0;
    }

    public void setInstructors(List<Instructor> instructors) {
        mInstructorList = instructors;
        notifyDataSetChanged();
    }

    class InstructorViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public final TextView InstructorItemViewText;
        final InstructorListAdapter instructorListAdapter;
        public Instructor instructor = new Instructor();

        public InstructorViewHolder(View itemView, InstructorListAdapter adapter) {
            super(itemView);
            InstructorItemViewText = itemView.findViewById(R.id.instructor);
            this.instructorListAdapter = adapter;
            itemView.setOnLongClickListener(this::onLongClick);
        }

        @Override
        public boolean onLongClick(View v) {
            Intent intent = new Intent(context, EditInstructorActivity.class);
            intent.putExtra("add", false);
            intent.putExtra("instructorUID", instructor.instructorUID);
            intent.putExtra("courseUID", instructor.courseUID);
            intent.putExtra("instructorName", instructor.instructorName);
            intent.putExtra("instructorPhone", instructor.instructorPhone);
            intent.putExtra("instructorEmail", instructor.instructorEmail);
            ((Activity)instructorListAdapter.getContext()).startActivity(intent);
            return true;
        }
    }

    public Context getContext() {
        return context;
    }
}
