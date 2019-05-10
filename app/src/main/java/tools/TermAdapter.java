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

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {

    private List<Term> termList;

    public TermAdapter(List<Term> terms) {
        termList = terms;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView startDate;
        public TextView endDate;
        public Button delTermBtn;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.termTitle);
            startDate = view.findViewById(R.id.termStart);
            endDate = view.findViewById(R.id.termEnd);
            delTermBtn = view.findViewById(R.id.delTermBtn);
        }
    }

    @NonNull
    @Override
    public TermAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.term_content, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.ViewHolder viewHolder, int i) {
        Term term = termList.get(i);
        viewHolder.title.setText(term.getTitle());
        viewHolder.startDate.setText(term.getStartDate());
        viewHolder.endDate.setText(term.getEndDate());
    }

    @Override
    public int getItemCount() {
        return termList.size();
    }
}
