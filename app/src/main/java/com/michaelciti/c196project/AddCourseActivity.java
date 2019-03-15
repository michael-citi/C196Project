package com.michaelciti.c196project;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class AddCourseActivity extends AppCompatActivity {

    EditText courseTitle;
    EditText courseDescription;
    DatePicker startDatePicker;
    DatePicker endDatePicker;
    Spinner instructorSpinner;
    Spinner objectiveSpinner;
    Spinner statusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseTitle = findViewById(R.id.courseTitleInput);
        courseDescription = findViewById(R.id.courseDescriptionInput);
        startDatePicker = findViewById(R.id.startDatePicker);
        endDatePicker = findViewById(R.id.endDatePicker);
        instructorSpinner = findViewById(R.id.courseInstructorsSpinner);
        objectiveSpinner = findViewById(R.id.courseObjectivesSpinner);
        statusSpinner = findViewById(R.id.statusSpinner);


    }

//    public void addNewCourse(View view) {
//        String errorMsg = validateCourseData();
//        if (errorMsg.equals("None")) {
//
//        } else {
//            Snackbar error = Snackbar.make(findViewById(R.id.constraintLayout), errorMsg, Snackbar.LENGTH_LONG);
//            error.show();
//        }
//    }

//    private String validateCourseData() {
//        String errorMsg;
//        String title = courseTitle.getText().toString();
//        String description = courseDescription.getText().toString();
//
//        if (title.equals("")) {
//            errorMsg = "Course Title cannot be empty. Please enter a title and try again.";
//            courseTitle.requestFocus();
//        } else if (description.equals("")) {
//            errorMsg = "Course Description cannot be empty. Please enter a description and try again.";
//            courseDescription.requestFocus();
//        } else if () {
//
//        } else if () {
//
//        } else if () {
//
//        } else if () {
//
//        } else {
//            errorMsg = "None";
//        }
//        return errorMsg;
//    }

}
