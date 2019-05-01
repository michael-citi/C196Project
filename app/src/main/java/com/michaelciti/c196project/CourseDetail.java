package com.michaelciti.c196project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
            confirmDetails();
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

        final String INSERT_DETAILS = "INSERT OR REPLACE INTO courses (courseId, title, description, startDate, expectedEnd, status, termId, notes) " +
                "VALUES ((SELECT courseId FROM courses WHERE title = ?), ?, ?, ?, ?, ?, ?, ?)";
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
            SQLiteStatement statement = db.compileStatement(INSERT_DETAILS);
            statement.bindString(1, title);
            statement.bindString(2, title);
            statement.bindString(3, description);
            statement.bindString(4, start);
            statement.bindString(5, end);
            statement.bindString(6, status);
            statement.bindLong(7, termID);
            statement.bindString(8, notes);
            statement.execute();
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    public void detailDatePicker(View v) {
        final Calendar calendar = Calendar.getInstance();
        int sYear = calendar.get(Calendar.YEAR);
        int sMonth = calendar.get(Calendar.MONTH);
        int sDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (v == startDateBtn) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                startDateText.setText(year + "-" + month + "-" + day);
                startYear = year;
                startMonth = month;
                startDay = day;
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }

        if (v == endDateBtn) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                endDateText.setText(year + "-" + month + "-" + day);
                endYear = year;
                endMonth = month;
                endDay = day;
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
            Snackbar.make(findViewById(R.id.detailConstraintLayout), "You have selected Course Status: " + statusSpinner.getSelectedItem(), Snackbar.LENGTH_SHORT).show();
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

    private void confirmDetails() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Confirm changes and Return to the main menu? Choosing to stay will still save your changes.");
        alertBuilder.setPositiveButton("Return", (dialogInterface, i) -> mainAct());
        alertBuilder.setNegativeButton("Stay", (dialogInterface, i) -> finish());
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public void cancelDetails(View v) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Cancel changes? No changes will be saved and you will return to the main screen.");
        alertBuilder.setPositiveButton("Yes", (dialogInterface, i) -> mainAct());
        alertBuilder.setNegativeButton("No", (dialogInterface, i) -> finish());
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
