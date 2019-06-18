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
import model.Instructor;

public class DetailInstructFrag extends DialogFragment {
    private static final String TAG = "DetailInstructorFragment";
    private static final String INSTRUCTOR_KEY = "Instructor";
    private Instructor instructor;

    TextView name, email, phone;

    public DetailInstructFrag() {
        // Required empty public constructor
    }

    public static DetailInstructFrag newInstance(Instructor instructor) {
        DetailInstructFrag frag = new DetailInstructFrag();
        Bundle args = new Bundle();
        args.putParcelable(INSTRUCTOR_KEY, instructor);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args == null || !(args.containsKey(INSTRUCTOR_KEY))) {
            Log.d(TAG, "Instructor object is null.");
            throw new NullPointerException();
        } else {
            instructor = args.getParcelable(INSTRUCTOR_KEY);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_instruct, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.diNameEditText);
        email = view.findViewById(R.id.diEmailEditText);
        phone = view.findViewById(R.id.diPhoneEditText);

        name.setText(instructor.getName());
        email.setText(instructor.getEmail());
        phone.setText(instructor.getPhone());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        instructor = null;
    }
}
