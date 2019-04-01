package com.michaelciti.c196project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;

public class AddCourseActivity extends AppCompatActivity {

    EditText courseTitle;
    EditText courseDescription;
    TextView startDateShow;
    TextView endDateShow;
    Button pickStartDateBtn;
    Button pickEndDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        courseTitle = findViewById(R.id.courseTitleInput);
        courseDescription = findViewById(R.id.courseDescriptionInput);
        startDateShow = findViewById(R.id.startDateShow);
        endDateShow = findViewById(R.id.endDateShow);
        pickStartDateBtn = findViewById(R.id.startDateButton);
        pickEndDateBtn = findViewById(R.id.endDateButton);
    }

    public void datePicker(View v) {
        final Calendar calendar = Calendar.getInstance();
        int sYear = calendar.get(Calendar.YEAR);
        int sMonth = calendar.get(Calendar.MONTH);
        int sDay = calendar.get(Calendar.DAY_OF_MONTH);

        if (v == pickStartDateBtn) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    startDateShow.setText(month + "-" + day + "-" + year);
                }
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }

        if (v == pickEndDateBtn) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    endDateShow.setText(month + "-" + day + "-" + year);
                }
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }
    }

    public void saveCourse(View v) {
        String errorMsg = validateData();
        if (errorMsg.equals("None")) {
            insertSQL();
        } else {
            showError(errorMsg);
        }
    }

    public void cancelCourse(View v) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Cancel New Course? All course info will be cancelled and you will return to the main screen.");
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

    private void insertSQL() {
        String title = courseTitle.getText().toString();
        String description = courseDescription.getText().toString();
        String startDate = startDateShow.getText().toString();
        String endDate = endDateShow.getText().toString();
    }

    private String validateData() {
        String errorMsg;
        String title = courseTitle.getText().toString();
        String description = courseDescription.getText().toString();

        if (title.equals("") || title.equals("Enter course title here")) {
            errorMsg = "Course title cannot be empty. Please correct this error and try again.";
            courseTitle.requestFocus();
        } else if (description.equals("") || description.equals("Enter course description here")) {
            errorMsg = "Course description cannot be empty. Please correct this error and try again.";
            courseDescription.requestFocus();
        } else if (startDateShow.getText().equals("")) {
            errorMsg = "Start Date is required. Please enter a start date.";
            pickStartDateBtn.requestFocus();
        } else if (endDateShow.getText().equals("")) {
            errorMsg = "Expected End Date is required. Please enter an end date.";
            pickEndDateBtn.requestFocus();
        } else {
            errorMsg = "None";
        }
        return errorMsg;
    }

    private void showError(String errorMsg) {
        Snackbar error = Snackbar.make(findViewById(R.id.constraintLayout), errorMsg, Snackbar.LENGTH_LONG);
        error.show();
    }

    private void mainAct() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
