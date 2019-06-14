package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import com.michaelciti.c196project.R;
import model.Instructor;

public class DetailInstructFrag extends Fragment {
    private static final String INSTRUCTOR_KEY = "Instructor";
    private static final String COURSE_ID_KEY = "CourseId";
    private static final String CHECKED_KEY = "Checked";
    private Instructor instructor;
    private OnFragmentInteractionListener mListener;
    private double newCourseId;
    boolean isChecked = false;

    EditText name, email, phone;
    CheckBox checkBox;

    public DetailInstructFrag() {
        // Required empty public constructor
    }
    public static DetailInstructFrag newInstance(int courseId, boolean checked) {
        DetailInstructFrag fragment = new DetailInstructFrag();
        Bundle args = new Bundle();
        args.putDouble(COURSE_ID_KEY, courseId);
        args.putBoolean(CHECKED_KEY, checked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            instructor = savedInstanceState.getParcelable(INSTRUCTOR_KEY);
            newCourseId = savedInstanceState.getDouble(COURSE_ID_KEY);
            isChecked = savedInstanceState.getBoolean(CHECKED_KEY);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        instructor = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
