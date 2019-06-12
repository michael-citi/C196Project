package com.michaelciti.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.SQLException;
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
import java.util.ArrayList;
import java.util.Calendar;
import static android.support.constraint.Constraints.TAG;
import model.Term;
import tools.DBHelper;

public class AddTerm extends AppCompatActivity {

    Button startDateBtn, endDateBtn;
    EditText termTitle;
    TextView startDate, endDate;
    ArrayList<Term> termArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        termTitle = findViewById(R.id.termTitleEditText);
        startDate = findViewById(R.id.addTermStartDateTextView);
        endDate = findViewById(R.id.addTermEndDateTextView);
        startDateBtn = findViewById(R.id.addTermStartDateBtn);
        endDateBtn = findViewById(R.id.addTermEndDateBtn);

        termArrayList = Term.queryAll(getApplicationContext());
    }

    public void saveTerm(View view) {
        String error = validateTerm();
        if (error.equalsIgnoreCase("None")) {
            confirmDialog();
            MainActivity.showError(view, "Saved new term successfully!");
        } else {
            MainActivity.showError(view, error);
        }
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm changes and save your new term?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            insertSQL();
            finish();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> finish());
        builder.create().show();
    }

    public void cancelTerm(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cancel new term? No changes will be saved.");
        builder.setPositiveButton("Cancel", (dialogInterface, i) -> goHome());
        builder.setNegativeButton("Oops!", (dialogInterface, i) -> finish());
        builder.create().show();
    }

    private void insertSQL() {
        String title = termTitle.getText().toString();
        String start = startDate.getText().toString();
        String end = endDate.getText().toString();
        final String INSERT_TERM = "INSERT INTO terms(title, startDate, endDate) " +
                "VALUES(?, ?, ?)";
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
            SQLiteStatement statement = db.compileStatement(INSERT_TERM);
            statement.bindString(1, title);
            statement.bindString(2, start);
            statement.bindString(3, end);
            statement.executeInsert();
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    public void termDatePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (view == startDateBtn) {
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
                String date = year + "-" + (month + 1) + "-" + day;
                startDate.setText(date);
            }, year, month, day);
            dialog.show();
        }
        if (view == endDateBtn) {
            DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
                String date = year + "-" + (month + 1) + "-" + day;
                endDate.setText(date);
            }, year, month, day);
            dialog.show();
        }
    }

    private String validateTerm() {
        String errorMsg = "None";
        String title = termTitle.getText().toString();

        for (Term term : termArrayList) {
            if (term.getTitle().equalsIgnoreCase(title)) {
                errorMsg = "A Term already exists with that title. Try again.";
                return errorMsg;
            }
        }
        return errorMsg;
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
