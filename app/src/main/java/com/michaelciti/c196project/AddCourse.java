package com.michaelciti.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static android.support.constraint.Constraints.TAG;
import android.database.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import model.Course;
import tools.DBHelper;

public class AddCourse extends AppCompatActivity {

    private static final String KEY = "Course";
    Course pkgCourse;
    ArrayList<Course> courseArrayList = new ArrayList<>();
    EditText title, description;
    TextView startDate, endDate;
    Button startDateBtn, endDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.addCourseTitleET);
        description = findViewById(R.id.addCourseDescET);
        startDate = findViewById(R.id.acStartDateTV);
        endDate = findViewById(R.id.acEndDateTV);
        startDateBtn = findViewById(R.id.acStartDateBtn);
        endDateBtn = findViewById(R.id.acEndDateBtn);

        pkgCourse = null;
        courseArrayList = Course.queryAll(getApplicationContext());
    }

    public void saveCourse(View view) {
        String test = title.getText().toString();
        if (!(compareCourseNames(test))) {
            pkgCourse = insertSQL();
            if (pkgCourse != null) {
                courseBuilder(pkgCourse);
            } else {
                throw new NullPointerException("pkgCourse object was null.");
            }
        } else {
            MainActivity.showError(view, "Another Course already exists with that name.");
        }
    }

    private void courseBuilder(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You will now be able to complete the course creation process " +
                "on the next screen.");
        builder.setNeutralButton("OK", (dialogInterface, i) -> {
                Intent intent = new Intent(this, CourseDetailActivity.class);
                intent.putExtra(KEY, course);
                startActivity(intent);
        });
        builder.create().show();
    }

    private Course insertSQL() {
        String courseTitle = title.getText().toString();
        String courseDesc = description.getText().toString();
        String start = startDate.getText().toString();
        String end = endDate.getText().toString();
        Course tempCourse = new Course(courseTitle, courseDesc, start, end);
        final String INSERT_COURSE = "INSERT INTO courses(title, description, startDate, expectedEnd) " +
                "VALUES(?, ?, ?, ?)";
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();
            SQLiteStatement stm = db.compileStatement(INSERT_COURSE);
            stm.bindString(1, tempCourse.getTitle());
            stm.bindString(2, tempCourse.getDescription());
            stm.bindString(3, tempCourse.getStartDate());
            stm.bindString(4, tempCourse.getExpectedEnd());
            stm.executeInsert();
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return tempCourse;
    }

    public void cancelCourse(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cancel new course? No changes will be saved.");
        builder.setPositiveButton("Cancel", (dialogInterface, i) -> goHome());
        builder.setNegativeButton("Oops!", (dialogInterface, i) -> finish());
        builder.create().show();
    }

    public void courseDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (view == startDateBtn) {
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, sYear, sMonth, sDay) -> {
                String date = sYear + "-" + (sMonth + 1) + "-" + sDay;
                startDate.setText(date);
            }, year, month, day);
            dialog.show();
        }
        if (view == endDateBtn) {
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, sYear, sMonth, sDay) -> {
                String date = sYear + "-" + (sMonth + 1) + "-" + sDay;
                endDate.setText(date);
            }, year, month, day);
            dialog.show();
        }
    }

    private boolean compareCourseNames(String title) {
        boolean isFound = false;
        for (Course course : courseArrayList) {
            if (course.getTitle().equalsIgnoreCase(title)) {
                isFound = true;
            }
        }
        return isFound;
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
