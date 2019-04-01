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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;
import model.Course;
import tools.DBHelper;
import static android.support.constraint.Constraints.TAG;

public class AddCourseActivity extends AppCompatActivity {

    EditText courseTitle, courseDescription;
    TextView startDateShow, endDateShow;
    Button pickStartDateBtn, pickEndDateBtn;
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;

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
                    startDateShow.setText(year + "-" + month + "-" + day);
                    startYear = year;
                    startMonth = month;
                    startDay = day;
                }
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }

        if (v == pickEndDateBtn) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    endDateShow.setText(year + "-" + month + "-" + day);
                    endYear = year;
                    endMonth = month;
                    endDay = day;
                }
            }, sYear, sMonth, sDay);
            datePickerDialog.show();
        }
    }

    public void saveCourse(View v) {
        String errorMsg = validateData();
        if (errorMsg.equals("None")) {
            insertSQL();
            completeCourse();
        } else {
            showError(errorMsg);
        }
    }

    public void cancelCourse(View v) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Cancel New Course? All course info will be removed and you will return to the main screen.");
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

        Course course = new Course(title, description, startDate, endDate);
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
            final String INSERT_SQL = "INSERT INTO courses (title, description, startDate, expectedEnd) " +
                    "VALUES (?, ?, ?, ?)";
            SQLiteStatement statement = db.compileStatement(INSERT_SQL);
            statement.bindString(1, course.getTitle());
            statement.bindString(2, course.getDescription());
            statement.bindString(3, course.getStartDate());
            statement.bindString(4, course.getExpectedEnd());
            statement.executeInsert();
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void completeCourse() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Course created successfully! You may now view and modify the course details " +
                "on the next screen or you can return to the main welcome screen.");
        alertBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = courseTitle.getText().toString();
                Course course = buildTransferCourse(title);
                courseDetailAct(course);
            }
        });
        alertBuilder.setNegativeButton("Return", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainAct();
            }
        });
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
        } else if (Course.courseComparator(startYear, startMonth, startDay, getApplicationContext()) == 1) {
            errorMsg = "Start Date has scheduling conflict with another course. Please view or modify course list.";
        } else if (Course.courseComparator(endYear, endMonth, endDay, getApplicationContext()) == 1) {
            errorMsg = "End Date has scheduling conflict with another course. Please view or modify course list.";
        } else {
            errorMsg = "None";
        }
        return errorMsg;
    }

    private void showError(String errorMsg) {
        Snackbar error = Snackbar.make(findViewById(R.id.constraintLayout), errorMsg, Snackbar.LENGTH_LONG);
        error.show();
    }

    private Course buildTransferCourse(String title) {
        final String COURSE_QUERY = "SELECT courseId, title, description, startDate, expectedEnd " +
                "FROM courses " +
                "WHERE title = " + title;
        Course course = new Course();
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();
            Cursor cursor = db.rawQuery(COURSE_QUERY, null);
            do {
                course.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
                course.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                course.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                course.setStartDate(cursor.getString(cursor.getColumnIndex("startDate")));
                course.setExpectedEnd(cursor.getString(cursor.getColumnIndex("expectedEnd")));
            } while (cursor.moveToNext());
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        }
        return course;
    }

    private void courseDetailAct(Course course) {
        Intent intent = new Intent(this, CourseDetail.class);
        intent.putExtra("ID", course.getCourseId());
        intent.putExtra("title", course.getTitle());
        intent.putExtra("description", course.getDescription());
        intent.putExtra("start", course.getStartDate());
        intent.putExtra("end", course.getExpectedEnd());
        startActivity(intent);
    }

    private void mainAct() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
