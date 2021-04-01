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

import edu.band148.wgumanager.CourseActivity;
import edu.band148.wgumanager.EditTermActivity;
import edu.band148.wgumanager.R;
import edu.band148.wgumanager.model.Term;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.TermViewHolder> {
    private final LayoutInflater mInflater;
    private final Context context;
    private List<Term> mTermList;

    public TermListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.termlist_item, parent, false);
        return new TermViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        if (mTermList != null) {
            final Term current = mTermList.get(position);
            holder.term = current;
            holder.TermItemViewText.setText(current.termTitle);
        }
    }

    @Override
    public int getItemCount() {
        if (mTermList != null) {
            return mTermList.size();
        }
        return 0;
    }

    public void setTerms(List<Term> terms) {
        mTermList = terms;
        notifyDataSetChanged();
    }

    class TermViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final TextView TermItemViewText;
        final TermListAdapter termListAdapter;
        public Term term = new Term();

        public TermViewHolder(View itemView, TermListAdapter adapter) {
            super(itemView);
            TermItemViewText = itemView.findViewById(R.id.term);
            this.termListAdapter = adapter;
            itemView.setOnClickListener(this::onClick);
            itemView.setOnLongClickListener(this::onLongClick);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, CourseActivity.class);
            intent.putExtra("termUID", term.termUID);
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            Intent intent = new Intent(context, EditTermActivity.class);
            intent.putExtra("add", false);
            intent.putExtra("termUID", term.termUID);
            intent.putExtra("termTitle", term.termTitle);
            intent.putExtra("termStart", term.termStart);
            intent.putExtra("termEnd", term.termEnd);
            ((Activity)termListAdapter.getContext()).startActivityForResult(intent, 1);
            return true;
        }
    }

    public Context getContext() {
        return context;
    }
}
