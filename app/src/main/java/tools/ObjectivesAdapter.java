package tools;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.michaelciti.c196project.R;
import java.util.ArrayList;
import model.Objective;

public class ObjectivesAdapter extends RecyclerView.Adapter<ObjectivesAdapter.ObjectivesViewHolder> {

    private ArrayList<Objective> objectiveArrayList;

    public ObjectivesAdapter(ArrayList<Objective> objectives) {
        objectiveArrayList = objectives;
    }

    public class ObjectivesViewHolder extends RecyclerView.ViewHolder {

        public ObjectivesViewHolder(View view) {
            super(view);
        }
    }

    @NonNull
    @Override
    public ObjectivesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.objective_content, viewGroup, false);
        return new ObjectivesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectivesViewHolder objectivesViewHolder, int i) {
        Objective objective = objectiveArrayList.get(i);
    }

    @Override
    public int getItemCount() {
        return objectiveArrayList.size();
    }
}
