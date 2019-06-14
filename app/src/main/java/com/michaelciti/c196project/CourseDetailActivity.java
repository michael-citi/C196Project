package com.michaelciti.c196project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import static android.support.constraint.Constraints.TAG;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import model.Course;
import model.Instructor;
import model.Objective;
import model.Term;
import tools.DBHelper;

public class CourseDetailActivity extends AppCompatActivity implements OnItemSelectedListener {

    // Arraylists and variables
    private static final String COURSE_KEY = "Course";
    ArrayList<Term> termArrayList = new ArrayList<>();
    ArrayList<Course> courseArrayList = new ArrayList<>();
    ArrayList<Objective> objectiveArrayList = new ArrayList<>();
    ArrayList<Instructor> instructorArrayList = new ArrayList<>();
    ArrayList<Instructor> selectedInstructors = new ArrayList<>();
    ArrayList<Objective> selectedObjectives = new ArrayList<>();
    ArrayList<String> instructorNameList = new ArrayList<>();
    ArrayList<String> objectiveNameList = new ArrayList<>();
    Course tempCourse;

    // views
    TextView startDate, endDate, notifyStartText, notifyEndText;
    Button startDateBtn, endDateBtn, instructAddBtn, instructDetailBtn, instructSelectBtn;
    Button objectiveAddBtn, objectiveDetailBtn, objectiveSelectBtn;
    Switch notifyStartDate, notifyEndDate;
    EditText titleText, descText, notesText;
    Spinner statusSpinner, termSpinner, instructorSpinner, objectiveSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ViewID Pile
        startDate = findViewById(R.id.dcStartDateText);
        endDate = findViewById(R.id.dcEndDateText);
        startDateBtn = findViewById(R.id.dcStartDateBtn);
        endDateBtn = findViewById(R.id.dcEndDateBtn);
        instructAddBtn = findViewById(R.id.dcAddInstructBtn);
        instructDetailBtn = findViewById(R.id.dcInstructDetailBtn);
        instructSelectBtn = findViewById(R.id.dcViewInstructBtn);
        objectiveAddBtn = findViewById(R.id.dcAddObjectiveBtn);
        objectiveDetailBtn = findViewById(R.id.dcObjectiveDetailBtn);
        objectiveSelectBtn = findViewById(R.id.dcObjectiveViewBtn);
        titleText = findViewById(R.id.dcTitleET);
        descText = findViewById(R.id.dcDescET);
        notesText = findViewById(R.id.dcCourseNotesET);
        statusSpinner = findViewById(R.id.dcStatusSpinner);
        termSpinner = findViewById(R.id.dcTermSpinner);
        instructorSpinner = findViewById(R.id.dcInstructSpinner);
        objectiveSpinner = findViewById(R.id.dcObjectiveSpinner);
        notifyStartDate = findViewById(R.id.dcStartDateSwitch);
        notifyEndDate = findViewById(R.id.dcEndDateSwitch);
        notifyStartText = findViewById(R.id.dcStartDateNotifyText);
        notifyEndText = findViewById(R.id.dcEndDateNotifyText);

        // methods to build the activity
        assignClickListeners();
        populateTempCourse();
        populateArrayLists();
        setSpinnerListeners();
        setSpinnerAdapters();
        populateFields();
        populateSelectedItems();
    }

    private void populateTempCourse() {
        if (getIntent() != null) {
            // grab parcelable Course object here
            tempCourse = getIntent().getExtras().getParcelable(COURSE_KEY);
        } else {
            // empty Course object if Parcel fails
            tempCourse = new Course();
        }
    }

    private void populateArrayLists() {
        courseArrayList = Course.queryAll(getApplicationContext());
        termArrayList = Term.queryAll(getApplicationContext());
        objectiveArrayList = Objective.queryAll(getApplicationContext());
        instructorArrayList = Instructor.queryAll(getApplicationContext());
    }

    private void assignClickListeners() {
        // OnCheckedChangeListeners built for the two switch buttons
        notifyStartDate.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                detailTimePicker(notifyStartDate);
            } else {
                notifyStartText.setText(R.string.CD_notify_text);
            }
        });
        notifyEndDate.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                detailTimePicker(notifyEndDate);
            } else {
                notifyEndText.setText(R.string.CD_notify_text);
            }
        });
    }

    private void setSpinnerListeners() {
        termSpinner.setOnItemSelectedListener(this);
        statusSpinner.setOnItemSelectedListener(this);
        objectiveSpinner.setOnItemSelectedListener(this);
        instructorSpinner.setOnItemSelectedListener(this);
    }

    private void setSpinnerAdapters() {
        ArrayList<String> termTitleList = new ArrayList<>();
        for (Term term : termArrayList) {
            termTitleList.add(term.getTitle());
        }
        ArrayAdapter<String> termSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termTitleList);
        termSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(termSpinnerAdapter);

        for (Instructor instructor : instructorArrayList) {
            instructorNameList.add(instructor.getName());
        }
        ArrayAdapter<String> instructorArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, instructorNameList);
        instructorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        instructorSpinner.setAdapter(instructorArrayAdapter);

        for (Objective objective : objectiveArrayList) {
            objectiveNameList.add(objective.getTitle());
        }
        ArrayAdapter<String> objectiveArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, objectiveNameList);
        objectiveArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        objectiveSpinner.setAdapter(objectiveArrayAdapter);
    }

    private void populateFields() {
        titleText.setText(tempCourse.getTitle());
        descText.setText(tempCourse.getDescription());
        startDate.setText(tempCourse.getStartDate());
        endDate.setText(tempCourse.getExpectedEnd());
        for (Term term : termArrayList) {
            if (term.getTermId() == tempCourse.getTermId()) {
                setSpinner(term.getTitle(), termSpinner);
            }
        }
        for (Instructor instructor : instructorArrayList) {
            if (instructor.getCourseId() == tempCourse.getCourseId()) {
                setSpinner(instructor.getName(), instructorSpinner);
            }
        }
        for (Objective objective : objectiveArrayList) {
            if (objective.getCourseId() == tempCourse.getCourseId()) {
                setSpinner(objective.getTitle(), objectiveSpinner);
            }
        }
        setSpinner(tempCourse.getStatus(), statusSpinner);
    }

    private void populateSelectedItems() {
        // clear and populate selected assessments and instructors if any old data is present
        if (selectedObjectives != null) {
            selectedObjectives.clear();
            for (Objective objective : objectiveArrayList) {
                for (int i = 0; i < courseArrayList.size(); i++) {
                    if (objective.getCourseId() == courseArrayList.get(i).getCourseId()) {
                        // add previously attached objectives
                        selectedObjectives.add(objective);
                    }
                }
            }
        }
        if (selectedInstructors != null) {
            selectedInstructors.clear();
            for (Instructor instructor : instructorArrayList) {
                for (int i = 0; i < courseArrayList.size(); i++) {
                    if (instructor.getCourseId() == courseArrayList.get(i).getCourseId()) {
                        // add previously attached instructors
                        selectedInstructors.add(instructor);
                    }
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // do nothing
    }

    public void saveDetails(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save changes to this course?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            insertSQL();
            MainActivity.showError(view, "Course successfully updated!");
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {
            finish();
            MainActivity.showError(view, "Changes to the course have been cancelled.");
        });
        builder.create().show();
    }

    private void insertSQL() {
        int courseId = tempCourse.getCourseId();
        String title = titleText.getText().toString();
        String desc = descText.getText().toString();
        String start = startDate.getText().toString();
        String end = endDate.getText().toString();
        String status = statusSpinner.getSelectedItem().toString();
        int termId = getAssociatedTermID(termSpinner.getSelectedItem().toString());
        String notes = notesText.getText().toString();
        String courseDetail = "UPDATE courses SET " +
                "title = ?, " +
                "description = ?, " +
                "startDate = ?, " +
                "expectedEnd = ?, " +
                "status = ?, " +
                "termId = ?, " +
                "notes = ? " +
                "WHERE courseId = ?";
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
            SQLiteStatement stm = db.compileStatement(courseDetail);
            stm.bindString(1, title);
            stm.bindString(2, desc);
            stm.bindString(3, start);
            stm.bindString(4, end);
            stm.bindString(5, status);
            stm.bindDouble(6, termId);
            stm.bindString(7, notes);
            stm.bindDouble(8, courseId);
            stm.execute();
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    public void cancelDetails(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cancel new course?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {

        });
        builder.setNegativeButton("No", (dialogInterface, i) -> finish());
        builder.create().show();
    }

    public void addDetail(View view) {
        boolean isFound = false;
        if (view == instructAddBtn) {
            String name = instructorSpinner.getSelectedItem().toString();
            for (int i = 0; i < selectedInstructors.size(); i++) {
                if (selectedInstructors.get(i).getName().equalsIgnoreCase(name)) {
                    MainActivity.showError(view, "Instructor: " + name + " is already attached to this course.");
                    isFound = true;
                }
            }
            if (!(isFound)) {
                selectedInstructors.add(getInstructor(name));
                MainActivity.showError(view, "Instructor: " + name + " successfully added to this course.");
            }
        }
        if (view == objectiveAddBtn) {
            String name = objectiveSpinner.getSelectedItem().toString();
            for (int i = 0; i < selectedObjectives.size(); i++) {
                if (selectedObjectives.get(i).getTitle().equalsIgnoreCase(name)) {
                    MainActivity.showError(view, "Assessment: " + name + " is already attached to this course.");
                    isFound = true;
                }
            }
            if (!(isFound)) {
                selectedObjectives.add(getObjective(name));
                MainActivity.showError(view, "Assessment: " + name + " successfully added to this course.");
            }
        }
    }

    public void showDetail(View view) {
        if (view == instructDetailBtn) {
            if (selectedInstructors.size() < 1) {
                MainActivity.showError(view, "You have not added any Instructors to the course yet.");
            } else {

            }
        }
        if (view == objectiveDetailBtn) {
            if (selectedObjectives.size() < 1) {
                MainActivity.showError(view, "You have not added any Assessments to the course yet.");
            } else {

            }
        }
    }

    public void showSelected(View view) {
        String message = "";
        StringBuilder stringBuilder = new StringBuilder();
        if (view == instructSelectBtn) {
            for (Instructor instructor : selectedInstructors) {
                stringBuilder.append(instructor.getName()).append("\n")
                        .append(instructor.getEmail()).append(", ").append(instructor.getPhone())
                        .append("\n").append("\n");
            }
        }
        if (view == objectiveSelectBtn) {
            for (Objective objective : selectedObjectives) {
                stringBuilder.append(objective.getTitle()).append(", ").append(objective.getType())
                        .append("\n").append(objective.getDescription()).append("\n").append("\n");
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setNeutralButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.create().show();
    }

    private Objective getObjective (String name) {
        for (Objective objective : objectiveArrayList) {
            if (objective.getTitle().equalsIgnoreCase(name)) {
                return objective;
            }
        }
        return null;
    }

    private Instructor getInstructor (String name) {
        for (Instructor instructor : instructorArrayList) {
            if (instructor.getName().equalsIgnoreCase(name)) {
                return instructor;
            }
        }
        return null;
    }

    private int getAssociatedTermID(String name) {
        int termId = 1;
        for (Term term : termArrayList) {
            if (term.getTitle().equalsIgnoreCase(name)) {
                termId = term.getTermId();
            }
        }
        return termId;
    }

    public void detailDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (view == startDateBtn) {
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
                String date = year + "-" + (month + 1) + "-" + day;
                startDate.setText(date);
            }, year, month, day);
            dialog.show();
        }
        if (view == endDateBtn) {
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
                String date = year + "-" + (month + 1) + "-" + day;
                endDate.setText(date);
            }, year, month, day);
            dialog.show();
        }
    }

    public void detailTimePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(this, (timePickerDialog, sHour, sMinute) -> {
            String timeText = sHour + ":" + sMinute;
            if (view == notifyStartDate) {
                notifyStartText.setText(timeText);
            }
            if (view == notifyEndDate) {
                notifyEndText.setText(timeText);
            }
        }, hour, minute, false);
        dialog.setTitle("Select Time");
        dialog.show();
    }

    private void setSpinner(String string, Spinner spinner) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)) {
                spinner.setSelection(i);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
