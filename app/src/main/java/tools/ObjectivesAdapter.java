package tools;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.michaelciti.c196project.R;
import java.util.ArrayList;
import fragments.DetailObjFrag;
import model.Objective;

public class ObjectivesAdapter extends RecyclerView.Adapter<ObjectivesAdapter.ObjectivesViewHolder> {

    private ArrayList<Objective> objectiveArrayList;

    public ObjectivesAdapter(ArrayList<Objective> objectives) {
        objectiveArrayList = objectives;
    }

    public class ObjectivesViewHolder extends RecyclerView.ViewHolder {

        public TextView objTitle;
        public TextView objType;
        public Button objDetailBtn;
        public Button objDeleteBtn;

        public ObjectivesViewHolder(View view) {
            super(view);
            objTitle = view.findViewById(R.id.objTitleText);
            objType = view.findViewById(R.id.objTypeText);
            objDetailBtn = view.findViewById(R.id.objDetailsBtn);
            objDeleteBtn = view.findViewById(R.id.objDeleteBtn);
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
    public void onBindViewHolder(@NonNull ObjectivesViewHolder viewHolder, int i) {
        Objective objective = objectiveArrayList.get(i);
        viewHolder.objTitle.setText(objective.getTitle());
        viewHolder.objType.setText(objective.getType());

        viewHolder.objDetailBtn.setOnClickListener(view -> {
            DetailObjFrag frag = DetailObjFrag.newInstance(objective);
            FragmentManager fm = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
            frag.show(fm, "detail_obj_frag");
        });

        viewHolder.objDeleteBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Are you sure you want to delete this Assessment?");
            builder.setPositiveButton("Yes", (dialogInterface, j) -> {
                Objective.deleteObjective(view, objective, view.getContext());
                objectiveArrayList.remove(i);
                notifyItemRemoved(i);
            });
            builder.setNegativeButton("No", (dialogInterface, i1) -> {
                ((Activity)view.getContext()).finish();
            });
            builder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return objectiveArrayList.size();
    }
}
