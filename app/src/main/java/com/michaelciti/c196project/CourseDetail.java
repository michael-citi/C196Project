package com.michaelciti.c196project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import model.Course;
import model.Term;
import tools.DBHelper;
import static android.support.constraint.Constraints.TAG;

public class CourseDetail extends AppCompatActivity implements OnItemSelectedListener {

    // CourseDetail variables
    ArrayList<String> termArrayList = new ArrayList<>();
    ArrayList<Term> termList = new ArrayList<>();
    Course tempCourse;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;

    // View ID's
    Spinner termSpinner, statusSpinner;
    EditText courseTitle, courseDescription, courseNotes;
    TextView startDateText, endDateText;
    Button startDateBtn, endDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        termSpinner = findViewById(R.id.termSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        courseTitle = findViewById(R.id.courseTitleInput);
        courseDescription = findViewById(R.id.courseDescriptionInput);
        courseNotes = findViewById(R.id.noteTextInput);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        startDateBtn = findViewById(R.id.startDateButton);
        endDateBtn = findViewById(R.id.endDateButton);

        termSpinner.setOnItemSelectedListener(this);
        statusSpinner.setOnItemSelectedListener(this);

        populateTermList();
        setTermAdapter();
        populateFields();
    }

    public void confirmDetails(View v) {
        String errorMsg = validateDetails();
        if (errorMsg.equals("None")) {
            insertDetailSQL();
        } else {
            showError(errorMsg);
        }
    }

    private void insertDetailSQL() {
        String title = courseTitle.getText().toString();
        String description = courseDescription.getText().toString();
        String start = startDateText.getText().toString();
        String end = endDateText.getText().toString();
        String status = statusSpinner.getSelectedItem().toString();
        int termID = getTermSpinnerID();
        String notes = courseNotes.getText().toString();

        final String INSERT_DETAILS = "INSERT OR REPLACE INTO courses (title, description, startDate, expectedEnd, status, termId, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
            SQLiteStatement statement = db.compileStatement(INSERT_DETAILS);
            statement.bindString(1, title);
            statement.bindString(2, description);
            statement.bindString(3, start);
            statement.bindString(4, end);
            statement.bindString(5, status);
            statement.bindLong(6, termID);
            statement.bindString(7, notes);
            statement.execute();
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        }
        updateJoinTables(title);
    }

    public void detailDatePicker(View v) {
        final Calendar calendar = Calendar.getInstance();
        int sYear = calendar.get(Calendar.YEAR);
        int sMonth = calendar.get(Calendar.MONTH);
        int sDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (v == startDateBtn) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    startDateText.setText(year + "-" + month + "-" + day);
                    startYear = year;
                    startMonth = month;
                    startDay = day;
                }
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }

        if (v == endDateBtn) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    endDateText.setText(year + "-" + month + "-" + day);
                    endYear = year;
                    endMonth = month;
                    endDay = day;
                }
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (view == termSpinner) {
            Snackbar.make(findViewById(R.id.detailConstraintLayout), "You have selected Term: " + termArrayList.get(i), Snackbar.LENGTH_SHORT).show();
        }
        if (view == statusSpinner) {
            Snackbar.make(findViewById(R.id.detailConstraintLayout), "You have selected Course Status: " + String.valueOf(statusSpinner.getSelectedItem()), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void populateTermList() {
        termList = Term.queryAll(getApplicationContext());
        for (Term term : termList) {
            termArrayList.add(term.getTitle());
        }
    }

    private void populateFields() {
        Intent intent = getIntent();
        tempCourse = intent.getParcelableExtra("Course");

        courseTitle.setText(tempCourse.getTitle());
        courseDescription.setText(tempCourse.getDescription());
        startDateText.setText(tempCourse.getStartDate());
        endDateText.setText(tempCourse.getExpectedEnd());
        courseNotes.setText(tempCourse.getNotes());
        statusSpinner.setSelection(getStatusIndex(statusSpinner, tempCourse.getStatus()));
        termSpinner.setSelection(getTermIndex(tempCourse));
    }

    private void setTermAdapter() {
        ArrayAdapter<String> termAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, termArrayList);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termSpinner.setAdapter(termAdapter);
    }

    public void cancelDetails(View v) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Cancel changes? No changes will be saved and you will return to the main screen.");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mainAct();
            }
        });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private String validateDetails() {
        String error;
        String title = courseTitle.getText().toString();
        String description = courseDescription.getText().toString();

        if (title.equals("")) {
            error = "Course title cannot be empty. Please correct this error and try again.";
            courseTitle.requestFocus();
        } else if (description.equals("")) {
            error = "Course description cannot be empty. Please correct this error and try again.";
            courseDescription.requestFocus();
        } else if (startDateText.getText().equals("")) {
            error = "Start Date is required. Please enter a start date.";
            startDateBtn.requestFocus();
        } else if (endDateText.getText().equals("")) {
            error = "Expected End Date is required. Please enter an end date.";
            endDateBtn.requestFocus();
        } else if (Course.courseComparator(startYear, startMonth, startDay, getApplicationContext()) == 1) {
            error = "Start Date has scheduling conflict with another course. Please view or modify course list.";
        } else if (Course.courseComparator(endYear, endMonth, endDay, getApplicationContext()) == 1) {
            error = "End Date has scheduling conflict with another course. Please view or modify course list.";
        } else {
            error = "None";
        }
        return error;
    }


    private void updateJoinTables(String title) {
        // populate courseId
        int courseId = findCourseId(title);
        // error catch if courseId is -1
        if (courseId == -1) {
            showError("Error occurred while resolving courseId value.");
            Log.e(TAG, "Error occurred while resolving courseId value.");
            return;
        }
        // populate course_instructor join table
        instructorJoin(courseId);
        // populate course_objective join table
        objectiveJoin(courseId);
    }

    private int getStatusIndex(Spinner spinner, String name) {
        for (int i = 0; i < spinner.getCount(); ++i) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return 0;
    }

    private int getTermIndex(Course course) {
        for (Term term : termList) {
            if (course.getTermId() == term.getTermId()) {
                return course.getTermId();
            }
        }
        return 0;
    }

    private int getTermSpinnerID() {
        int termID;
        String termName = termSpinner.getSelectedItem().toString();

        for (Term term : termList) {
            if (termName.equalsIgnoreCase(term.getTitle())) {
                termID = term.getTermId();
                return termID;
            }
        }
        return 0;
    }

    private int findCourseId(String title) {
        int courseId = -1;
        final String QUERY_COURSE_ID = "SELECT courseId FROM courses WHERE title = " + title;

        SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_COURSE_ID, null);
        try {
            if (cursor.moveToFirst()) {
                courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
            }
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return courseId;
    }

    private void objectiveJoin(int courseId) {
        final String INSERT_COURSE_OBJECTIVE = "INSERT OR REPLACE INTO course_objective (courseObjectiveId, courseId, objectiveId) " +
                "VALUES ((SELECT courseObjectiveId WHERE courseId = ?), ?, ?)";
        SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        SQLiteStatement oStm = db.compileStatement(INSERT_COURSE_OBJECTIVE);


    }

    private void instructorJoin(int courseId) {
        final String INSERT_COURSE_INSTRUCTOR = "INSERT OR REPLACE INTO course_instructor (courseInstructorId, courseId, instructorId) " +
                "VALUES ((SELECT courseInstructorId WHERE courseId = ?), ?, ?)";
        SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        SQLiteStatement iStm = db.compileStatement(INSERT_COURSE_INSTRUCTOR);


    }

    private void showError(String errorMsg) {
        Snackbar.make(findViewById(R.id.detailConstraintLayout), errorMsg, Snackbar.LENGTH_LONG).show();
    }

    private void mainAct() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do Nothing
    }

}
