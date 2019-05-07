package tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public ViewHolder(View view) {
            super(view);

        }
    }

    @NonNull
    @Override
    public TermAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View coursesView = inflater.inflate(R.layout.term_content, viewGroup, false);

        ViewHolder holder = new ViewHolder(coursesView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.ViewHolder viewHolder, int i) {
        Term term = termList.get(i);

    }

    @Override
    public int getItemCount() {
        return termList.size();
    }
}
