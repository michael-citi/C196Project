package com.michaelciti.c196project;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import model.Objective;
import tools.DBHelper;

public class ObjectiveDetailActivity extends AppCompatActivity {

    private static final String NOTIFICATION_TITLE = "Title";
    private static final String NOTIFICATION_MESSAGE = "Message";
    private static final String TAG = "ObjectiveDetailActivity";
    Objective objective;
    EditText title, notes, description;
    Spinner typeSpinner;
    TextView goalDateText;
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
    SimpleDateFormat stf = new SimpleDateFormat("HH:MM:SS.SSS");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objective_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.objTitleEditText);
        notes = findViewById(R.id.detailNotesEditText);
        description = findViewById(R.id.objDescriptionEditText);
        typeSpinner = findViewById(R.id.detObjSpinner);
        goalDateText = findViewById(R.id.goalDateTextView);

        populateFields();
    }

    private void populateObjective() {
        if (getIntent() != null) {
            // grab Parcelable Objective object here
            objective = getIntent().getExtras().getParcelable("Objective");
        } else {
            // empty Objective object if Parcel retrieval fails
            objective = new Objective();
        }
    }

    private void populateFields() {
        populateObjective();
        title.setText(objective.getTitle());
        notes.setText(objective.getNotes());
        description.setText(objective.getDescription());
        for (int i = 0; i < typeSpinner.getCount(); i++) {
            if (typeSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(objective.getType())) {
                typeSpinner.setSelection(i);
            }
        }
        if (objective.getTime() != null) {
            try {
                Date goalDate = sdf.parse(objective.getTime());
                goalDateText.setText(goalDate.toString());
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void saveObjDetails(View view) {
        String results = validateDetails();
        if (results.equalsIgnoreCase("None")) {
            confirmDialog();
        } else {
            MainActivity.showError(view, results);
        }
    }

    public void cancelObjDetails(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cancel Changes?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            Intent intent = new Intent(this, ViewObjectives.class);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> finish());
        builder.create().show();
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Changes to Assessment?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            updateObjSQL();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> finish());
        builder.create().show();
    }

    private String validateDetails() {
        String errorMsg = "None";
        if (title.getText().toString().equalsIgnoreCase("")) {
            errorMsg = "Title cannot be empty.";
            return errorMsg;
        } else if (description.getText().toString().equalsIgnoreCase("")) {
            errorMsg = "Description cannot be empty.";
            return errorMsg;
        } else {
            return errorMsg;
        }
    }

    private void updateObjSQL() {
        String objTitle = title.getText().toString();
        String objType = typeSpinner.getSelectedItem().toString();
        String objTime = "";
        if (!(goalDateText.getText().toString().equalsIgnoreCase("No Goal Date Selected"))) {
            objTime = goalDateText.getText().toString();
        }
        String objDesc = description.getText().toString();
        String objNotes = notes.getText().toString();
        final String UPDATE_OBJECTIVE = "UPDATE objectives SET " +
                "title = ?, " +
                "type = datetime(?), " +
                "time = ?, " +
                "description = ?, " +
                "notes = ? " +
                "WHERE objectiveId = " + objective.getObjectiveId();
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
            SQLiteStatement stm = db.compileStatement(UPDATE_OBJECTIVE);
            stm.bindString(1, objTitle);
            stm.bindString(2, objType);
            stm.bindString(3, objTime);
            stm.bindString(4, objDesc);
            stm.bindString(5, objNotes);
            stm.execute();
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    public void goalDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, mYear, mMonth, mDay) -> {
            String date = mYear + "-" + (mMonth + 1) + "-" + mDay;
            setGoalDate(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setGoalDate(String date) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, sHour, sMinute) -> {
            String time = sHour + ":" + sMinute + ":00";
            try {
                Date sendTime = stf.parse(time);
                Date dateTime = sdf.parse(date + " " + time);
                goalDateText.setText(dateTime.toString());
                updateSQL(dateTime.toString(), objective.getObjectiveId());
                setAlarm(calendar, sendTime);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    private void updateSQL(String dateTime, int objectiveId) {
        final String UPDATE_OBJECTIVE = "UPDATE objectives SET " +
                "time = datetime(" + dateTime + ") WHERE objectiveId = " + objectiveId;
        SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
        try {
            db.execSQL(UPDATE_OBJECTIVE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void setAlarm(Calendar calendar, Date time) {
        Intent intent = new Intent(this, NotifyMe.class);
        intent.putExtra(NOTIFICATION_TITLE, "Goal Date Reached");
        intent.putExtra(NOTIFICATION_MESSAGE, "Today is the day to complete the assessment!");
        PendingIntent pIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar sCalendar = (Calendar) calendar.clone();
        sCalendar.setTime(time);
        am.set(AlarmManager.RTC_WAKEUP, sCalendar.getTimeInMillis(), pIntent);
    }
}
