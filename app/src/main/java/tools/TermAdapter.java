package tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.michaelciti.c196project.R;
import java.util.ArrayList;
import model.Course;
import model.Term;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private ArrayList<Term> termList;
    private ArrayList<Course> courseList;

    public TermAdapter(ArrayList<Term> terms, ArrayList<Course> courses) {
        termList = terms;
        courseList = courses;
        notifyDataSetChanged();
    }

    public class TermViewHolder extends RecyclerView.ViewHolder {
        public TextView termIdText;
        public TextView title;
        public TextView startDate;
        public TextView endDate;
        public Button delTermBtn;
        public LinearLayout layout;

        public TermViewHolder(View view) {
            super(view);
            termIdText = view.findViewById(R.id.termIdTextView);
            title = view.findViewById(R.id.termTitle);
            startDate = view.findViewById(R.id.termStart);
            endDate = view.findViewById(R.id.termEnd);
            delTermBtn = view.findViewById(R.id.delTermBtn);
            layout = view.findViewById(R.id.nestedCourseLayout);
        }
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.term_content, viewGroup, false);
        return new TermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder viewHolder, int i) {
        Term term = termList.get(i);
        String termId = Integer.toString(term.getTermId());
        viewHolder.termIdText.setText(termId);
        viewHolder.title.setText(term.getTitle());

        if (term.getTermId() == 1) {
            viewHolder.delTermBtn.setEnabled(false);
            viewHolder.startDate.setText("Start Date: None");
            viewHolder.endDate.setText("End Date: None");
        } else {
            viewHolder.startDate.setText("Start Date: " + term.getStartDate());
            viewHolder.endDate.setText("End Date: " + term.getEndDate());
        }

        viewHolder.delTermBtn.setOnClickListener(view -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
            alertBuilder.setMessage("Are you sure you want to delete this Term?");
            alertBuilder.setPositiveButton("Yes", (dialogInterface, j) -> {
                Term.deleteTerm(i + 1, view.getContext());
                termList.remove(i);
                notifyItemRemoved(i);
            });
            alertBuilder.setNegativeButton("No", (dialogInterface, j) -> {
                ((Activity)view.getContext()).finish();
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        });

        for (Course course : courseList) {
            if (course.getTermId() == term.getTermId()) {
                TextView textView = new TextView(viewHolder.layout.getContext());
                textView.setText(course.getTitle());
                textView.setId(course.getCourseId());
                viewHolder.layout.addView(textView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return termList.size();
    }
}
