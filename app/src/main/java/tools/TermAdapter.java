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





}
