package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.michaelciti.c196project.R;
import model.Term;

public class DetailTermFrag extends DialogFragment {
    private static final String TAG = "DetailTermFragment";
    private static final String TERM_KEY = "Term";
    private Term term;

    TextView title, startDate, endDate;

    public DetailTermFrag() {
        // Required empty public constructor
    }

    public static DetailTermFrag newInstance(Term term) {
        DetailTermFrag frag = new DetailTermFrag();
        Bundle args = new Bundle(1);
        args.putParcelable(TERM_KEY, term);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null || !(args.containsKey(TERM_KEY))) {
            Log.d(TAG, "Term object is null");
            throw new NullPointerException();
        } else {
            term = args.getParcelable(TERM_KEY);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_term, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.dtTitle);
        startDate = view.findViewById(R.id.dtStartDate);
        endDate = view.findViewById(R.id.dtEndDate);

        title.setText(term.getTitle());
        startDate.setText(term.getStartDate());
        endDate.setText(term.getEndDate());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        term = null;
    }
}
