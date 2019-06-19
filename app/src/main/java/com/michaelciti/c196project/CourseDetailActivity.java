package com.michaelciti.c196project;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import fragments.DetailInstructFrag;
import fragments.DetailObjFrag;
import model.Course;
import model.Instructor;
import model.Objective;
import model.Term;
import tools.DBHelper;

public class CourseDetailActivity extends AppCompatActivity implements OnItemSelectedListener {

    // ArrayLists and variables
    private static final String TAG = "CourseDetailActivity";
    private static final String COURSE_KEY = "Course";
    private static final String NOTIFICATION_TITLE = "Title";
    private static final String NOTIFICATION_MESSAGE = "Message";
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
    TextView startDate, endDate;
    Button startDateBtn, endDateBtn, instructAddBtn, instructDetailBtn, instructSelectBtn;
    Button objectiveAddBtn, objectiveDetailBtn, objectiveSelectBtn;
    Switch notifyCourseDate;
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
        notifyCourseDate = findViewById(R.id.dcEndDateSwitch);

        // methods to build the activity
        assignClickListeners();
        populateTempCourse();
        populateArrayLists();
        setSpinnerListeners();
        setSpinnerAdapters();
        populateFields();
        populateSelectedItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_course_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String subject = tempCourse.getTitle() + " Notes";
                String message = tempCourse.getNotes();
                if (message.equalsIgnoreCase("")) {
                    Log.d(TAG, "Unable to share empty string.");
                } else {
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(sharingIntent, "Share via: "));
                }
        }
        return super.onOptionsItemSelected(item);
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
        notifyCourseDate.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                detailTimePicker(notifyCourseDate);
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
        if (!(tempCourse.getNotes().equalsIgnoreCase(""))) {
            notesText.setText(tempCourse.getNotes());
        }
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
        updateSQL(courseId);
    }

    private void updateSQL(int courseId) {
        final String UPDATE_OBJECTIVES = "UPDATE objectives SET courseId = ? WHERE objectiveId = ?";
        final String UPDATE_INSTRUCTORS = "UPDATE instructors SET courseId = ? WHERE instructorId = ?";
        SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        for (Objective objective : selectedObjectives) {
            SQLiteStatement stm = db.compileStatement(UPDATE_OBJECTIVES);
            stm.bindDouble(1, courseId);
            stm.bindDouble(2, objective.getObjectiveId());
            stm.execute();
        }
        for (Instructor instructor : selectedInstructors) {
            SQLiteStatement stm = db.compileStatement(UPDATE_INSTRUCTORS);
            stm.bindDouble(1, courseId);
            stm.bindDouble(2, instructor.getInstructorId());
            stm.execute();
        }
    }

    public void cancelDetails(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cancel Changes?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            Intent intent = new Intent(this, ViewCourses.class);
            startActivity(intent);
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
            String name = instructorSpinner.getSelectedItem().toString();
            DetailInstructFrag frag = DetailInstructFrag.newInstance(getInstructor(name));
            FragmentManager fm = getSupportFragmentManager();
            frag.show(fm, "detail_instruct_frag");
        }
        if (view == objectiveDetailBtn) {
            String name = objectiveSpinner.getSelectedItem().toString();
            DetailObjFrag frag = DetailObjFrag.newInstance(getObjective(name));
            FragmentManager fm = getSupportFragmentManager();
            frag.show(fm, "detail_objective_frag");
        }
    }

    public void showSelected(View view) {
        StringBuilder stringBuilder = new StringBuilder();
        if (view == instructSelectBtn) {
            for (Instructor instructor : selectedInstructors) {
                stringBuilder.append(instructor.getName()).append("\n")
                        .append(instructor.getEmail()).append(", ").append(instructor.getPhone())
                        .append("\n\n");
            }
        }
        if (view == objectiveSelectBtn) {
            for (Objective objective : selectedObjectives) {
                stringBuilder.append(objective.getTitle()).append(", ").append(objective.getType())
                        .append("\n").append(objective.getDescription()).append("\n\n");
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(stringBuilder);
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
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, mYear, mMonth, mDay) -> {
                String date = mYear + "-" + (mMonth + 1) + "-" + mDay;
                startDate.setText(date);
            }, year, month, day);
            dialog.show();
        }
        if (view == endDateBtn) {
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, mYear, mMonth, mDay) -> {
                String date = mYear + "-" + (mMonth + 1) + "-" + mDay;
                endDate.setText(date);
            }, year, month, day);
            dialog.show();
        }
    }

    public void detailTimePicker(View view) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(this, (timePickerDialog, sHour, sMinute) -> {
            Calendar sCal = (Calendar) calendar.clone();
            sCal.set(Calendar.HOUR, sHour);
            sCal.set(Calendar.MINUTE, sMinute);
            sCal.set(Calendar.SECOND, 0);
            sCal.set(Calendar.MILLISECOND, 0);
            String time = sHour + ":" + sMinute + ":00";
            setAlarm(sCal, time);
        }, hour, minute, false);
        dialog.setTitle("Select Notification Time");
        dialog.show();
    }

    private void setAlarm(Calendar calendar, String time) {
        // configure Intent data
        Intent startIntent = new Intent(this, NotifyMe.class);
        startIntent.putExtra(NOTIFICATION_TITLE, "Course Starting");
        startIntent.putExtra(NOTIFICATION_MESSAGE, "Your course is starting today.");
        Intent endIntent = new Intent(this, NotifyMe.class);
        endIntent.putExtra(NOTIFICATION_TITLE, "Course is Ending");
        endIntent.putExtra(NOTIFICATION_MESSAGE, "Your course is ending today.");
        // build PendingIntent objects for Alarm
        PendingIntent stPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent endPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, endIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm setup
        AlarmManager sam = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager eam = (AlarmManager) getSystemService(ALARM_SERVICE);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
        SimpleDateFormat stf = new SimpleDateFormat("HH:MM:SS");
        try {
            String notifyTime = stf.parse(time).toString();
            Date start = sdf.parse(tempCourse.getStartDate() + " " + notifyTime);
            Date end = sdf.parse(tempCourse.getExpectedEnd() + " " + notifyTime);
            // setting start date alarm
            Calendar sCalendar = (Calendar) calendar.clone();
            sCalendar.setTime(start);
            sam.set(AlarmManager.RTC_WAKEUP, sCalendar.getTimeInMillis(), stPendingIntent);
            // setting end date alarm
            Calendar eCalendar = (Calendar) calendar.clone();
            eCalendar.setTime(end);
            eam.set(AlarmManager.RTC_WAKEUP, eCalendar.getTimeInMillis(), endPendingIntent);
        } catch (ParseException ex) {
            Log.d(TAG, ex.getMessage());
        }
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
