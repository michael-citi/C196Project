package fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import com.michaelciti.c196project.R;
import model.Instructor;
import static android.support.constraint.Constraints.TAG;

public class DetailInstructFrag extends Fragment {
    private static final String INSTRUCTOR_KEY = "Instructor";
    private static final String CHECKED_KEY = "Checked";
    private Instructor instructor;
    boolean isChecked;

    EditText name, email, phone;
    CheckBox checkBox;

    public DetailInstructFrag() {
        // Required empty public constructor
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
            isChecked = args.getBoolean(CHECKED_KEY);
        }
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
        checkBox = view.findViewById(R.id.diCheckBox);

        name.setText(instructor.getName());
        email.setText(instructor.getEmail());
        phone.setText(instructor.getPhone());

        if (isChecked) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
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
