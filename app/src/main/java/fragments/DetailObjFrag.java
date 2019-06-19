package fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.michaelciti.c196project.R;
import model.Objective;

public class DetailObjFrag extends DialogFragment {
    private static final String TAG = "DetailObjFragment";
    private static final String OBJECTIVE_KEY = "Objective";
    private Objective objective;

    TextView title, time, type, description, notes;
    Button shareBtn;

    public DetailObjFrag() {
        // Required empty public constructor
    }

    public static DetailObjFrag newInstance(Objective objective) {
        DetailObjFrag frag = new DetailObjFrag();
        Bundle args = new Bundle(1);
        args.putParcelable(OBJECTIVE_KEY, objective);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null || !(args.containsKey(OBJECTIVE_KEY))) {
            Log.d(TAG, "Objective object is null");
            throw new NullPointerException();
        } else {
            objective = args.getParcelable(OBJECTIVE_KEY);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_obj, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.doTitle);
        time = view.findViewById(R.id.doTime);
        type = view.findViewById(R.id.doType);
        description = view.findViewById(R.id.doDescription);
        notes = view.findViewById(R.id.doNotes);
        shareBtn = view.findViewById(R.id.shareBtn);

        title.setText(objective.getTitle());
        time.setText(objective.getTime());
        type.setText(objective.getType());
        description.setText(objective.getDescription());
        notes.setText(objective.getNotes());

        shareBtn.setOnClickListener(shareView -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String subject = objective.getTitle() + " Notes";
            String message = objective.getNotes();
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(sharingIntent, "Share via:"));
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        objective = null;
    }
}
