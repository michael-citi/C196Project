package com.michaelciti.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;

public class AddCourseActivity extends AppCompatActivity {

    EditText courseTitle;
    EditText courseDescription;
    EditText startDatePicker;
    EditText endDatePicker;
    Spinner instructorSpinner;
    Spinner objectiveSpinner;
    Spinner statusSpinner;
    TextView addCITextView;
    Button addCIBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseTitle = findViewById(R.id.courseTitleInput);
        courseDescription = findViewById(R.id.courseDescriptionInput);
        startDatePicker = findViewById(R.id.startDateInput);
        endDatePicker = findViewById(R.id.endDateInput);
        instructorSpinner = findViewById(R.id.courseInstructorsSpinner);
        objectiveSpinner = findViewById(R.id.courseObjectivesSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);
        addCITextView = findViewById(R.id.addCITextView);
        addCIBtn = findViewById(R.id.addCIBtn);

        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(v);
            }
        });

        endDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate(v);
            }
        });

    }

    private void selectDate(View view) {
        if (view == startDatePicker) {
            final Calendar calendar = Calendar.getInstance();
            int sYear = calendar.get(Calendar.YEAR);
            int sMonth = calendar.get(Calendar.MONTH);
            int sDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    startDatePicker.setText((month + 1) + "/" + dayOfMonth + "/" + year);

                }
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }
        if (view == endDatePicker) {
            final Calendar calendar = Calendar.getInstance();
            int sYear = calendar.get(Calendar.YEAR);
            int sMonth = calendar.get(Calendar.MONTH);
            int sDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    startDatePicker.setText((month + 1) + "/" + dayOfMonth + "/" + year);

                }
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }
    }

    public void addNewCourse(View view) {
        String errorMsg = validateCourseData();
        if (errorMsg.equals("None")) {


        returnToMain();
        } else {
            Snackbar error = Snackbar.make(findViewById(R.id.constraintLayout), errorMsg, Snackbar.LENGTH_LONG);
            error.show();
        }
    }

    private String validateCourseData() {
        String errorMsg;
        String title = courseTitle.getText().toString();
        String description = courseDescription.getText().toString();
        String startDate = startDatePicker.getText().toString();
        String endDate = endDatePicker.getText().toString();

        if (title.equals("")) {
            errorMsg = "Course Title cannot be empty. Please enter a title and try again.";
            courseTitle.requestFocus();
        } else if (description.equals("")) {
            errorMsg = "Course Description cannot be empty. Please enter a description and try again.";
            courseDescription.requestFocus();
        } else if (startDate.equals("")) {
            errorMsg = "Start date cannot be empty. Please select a start date for the course.";
            startDatePicker.requestFocus();
        } else if (endDate.equals("")) {
            errorMsg = "Expected end date cannot be empty. Please select an expected end date for the course.";
            endDatePicker.requestFocus();
        } else {
            errorMsg = "None";
        }
        return errorMsg;
    }

    private void returnToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
