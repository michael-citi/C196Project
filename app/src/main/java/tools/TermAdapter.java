package tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.michaelciti.c196project.R;
import java.util.List;
import model.Term;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private List<Term> termList;

    public TermAdapter(List<Term> terms) {
        termList = terms;
    }

    public class TermViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView startDate;
        public TextView endDate;
        public Button delTermBtn;

        public TermViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.termTitle);
            startDate = view.findViewById(R.id.termStart);
            endDate = view.findViewById(R.id.termEnd);
            delTermBtn = view.findViewById(R.id.delTermBtn);
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
        viewHolder.title.setText(term.getTitle());
        viewHolder.startDate.setText("Start Date: " + term.getStartDate());
        viewHolder.endDate.setText("End Date: " + term.getEndDate());

        viewHolder.delTermBtn.setOnClickListener(view -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(viewHolder.delTermBtn.getContext());
            alertBuilder.setMessage("Are you sure you want to delete this Term?");
            alertBuilder.setPositiveButton("Yes", (dialogInterface, j) -> {
                Term.deleteTerm(i, viewHolder.delTermBtn.getContext());
                termList.remove(i);
                notifyItemRemoved(i);
            });
            alertBuilder.setNegativeButton("No", (dialogInterface, j) -> {
                ((Activity)viewHolder.delTermBtn.getContext()).finish();
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return termList.size();
    }
}
