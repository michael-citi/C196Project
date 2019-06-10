package com.michaelciti.c196project;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import model.Term;
import tools.DBHelper;

import static android.support.constraint.Constraints.TAG;

public class AddTermActivity extends AppCompatActivity {

    // AddTermActivity variables
    ArrayList<Term> termArrayList = new ArrayList<>();
    int startYear, startMonth, startDay;
    int endYear, endMonth, endDay;

    // View ID's
    EditText termTitle;
    TextView startDateText, endDateText;
    Button startDateBtn, endDateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startDateText = findViewById(R.id.termStartDateText);
        endDateText = findViewById(R.id.termEndDateText);
        termTitle = findViewById(R.id.termTitleEditText);
        startDateBtn = findViewById(R.id.termStartDateBtn);
        endDateBtn = findViewById(R.id.termEndDateBtn);

        termArrayList = Term.queryAll(getApplicationContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void confirmSave(View v) {
        String errorMsg = validateData();
        if (errorMsg.equals("None")) {
            confirmTerm();
        } else {
            showError(errorMsg);
        }
    }

    private void insertSQL(String title, String start, String end) {
        final String INSERT_TERM = "INSERT INTO terms(title, startDate, endDate) " +
                "VALUES(?, ?, ?)";
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
            SQLiteStatement statement = db.compileStatement(INSERT_TERM);
            statement.bindString(1, title);
            statement.bindString(2, start);
            statement.bindString(3, end);
            statement.execute();
        } catch (SQLException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private String validateData() {
        String errorMsg;
        String title = termTitle.getText().toString();
        String titleError = compareTitle(title);

        if (title.equals("")) {
            errorMsg = "Term title is blank. Please enter a title of your choice.";
            termTitle.requestFocus();
        } else if (titleError.equals("Error")) {
            errorMsg = "Term already exists with the same title. Please change the title and try again.";
            termTitle.requestFocus();
        } else if (startDateText.getText().equals("")) {
            errorMsg = "Start Date is required. Please enter a start date.";
            startDateBtn.requestFocus();
        } else if (endDateText.getText().equals("")) {
            errorMsg = "Expected End Date is required. Please enter an end date.";
            endDateBtn.requestFocus();
        } else if (Term.termComparator(startYear, startMonth, startDay, getApplicationContext()) == 1) {
            errorMsg = "Start Date has scheduling conflict with another Term. Please correct the starting date for this term.";
        } else if (Term.termComparator(endYear, endMonth, endDay, getApplicationContext()) == 1) {
            errorMsg = "End Date has scheduling conflict with another Term. Please correct the ending date for this term.";
        } else {
            errorMsg = "None";
        }
        return errorMsg;
    }

    private void confirmTerm() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Save changes and return to the main menu?");
        alertBuilder.setPositiveButton("Save", (dialogInterface, i) -> {
            String title = termTitle.getText().toString();
            String start = startDateText.getText().toString();
            String end = endDateText.getText().toString();
            insertSQL(title, start, end);
            mainAct();
        });
        alertBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> finish());
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public void cancelTerm(View v) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Cancel new Term? No changes will be saved and you will return to the main screen.");
        alertBuilder.setPositiveButton("Yes", (dialogInterface, i) -> mainAct());
        alertBuilder.setNegativeButton("No", (dialogInterface, i) -> finish());
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void showError(String errorMsg) {
        Snackbar.make(findViewById(R.id.detailConstraintLayout), errorMsg, Snackbar.LENGTH_LONG).show();
    }

    private void mainAct() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private String compareTitle(String title) {
        String error = "None";
        for (Term term : termArrayList) {
            if (term.getTitle().equals(title)) {
                error = "Error";
            }
        }
        return error;
    }

    public void termDatePicker(View v) {
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
}
