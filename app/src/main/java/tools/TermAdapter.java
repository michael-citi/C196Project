package tools;

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
    public void onBindViewHolder(@NonNull TermViewHolder termViewHolder, int i) {
        Term term = termList.get(i);
        termViewHolder.title.setText(term.getTitle());
        termViewHolder.startDate.setText("Start Date: " + term.getStartDate());
        termViewHolder.endDate.setText("End Date: " + term.getEndDate());

        termViewHolder.delTermBtn.setOnClickListener(view -> {
            Term.deleteTerm(i, termViewHolder.delTermBtn.getContext());
            termList.remove(i);
            notifyItemRemoved(i);
        });
    }

    @Override
    public int getItemCount() {
        return termList.size();
    }
}
