package com.michaelciti.c196project;

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
import android.widget.EditText;
import android.widget.Spinner;
import java.util.ArrayList;
import model.Objective;
import tools.DBHelper;

public class AddObjective extends AppCompatActivity {

    private static final String TAG = "AddObjectiveActivity";
    EditText title, description, notes;
    Spinner typeSpinner;
    ArrayList<Objective> objectiveArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_objective);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.aoObjTitleEdit);
        description = findViewById(R.id.aoObjDescriptionEdit);
        notes = findViewById(R.id.aoObjNotesEdit);
        typeSpinner = findViewById(R.id.aoObjTypeSpinner);

        objectiveArrayList = Objective.queryAll(getApplicationContext());
    }

    public void addNewObjective(View view) {
        String errorMsg = validateObjective();
        if (errorMsg.equalsIgnoreCase("None")) {
            confirmObjective();
            MainActivity.showError(view, "Assessment successfully added!");
        } else {
            MainActivity.showError(view, errorMsg);
        }
    }

    public void cancelNewObjective(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Cancel new Assessment? No changes will be saved.");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> goHome());
        builder.setNegativeButton("No", (dialogInterface, i) -> finish());
        builder.create().show();
    }

    private void confirmObjective() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm changes and save your new Assessment?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            insertSQL();
            finish();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> finish());
        builder.create().show();
    }

    private void insertSQL() {
        String objTitle = title.getText().toString();
        String objType = typeSpinner.getSelectedItem().toString();
        String objDescription = description.getText().toString();
        String objNotes = notes.getText().toString();
        final String INSERT_OBJ = "INSERT INTO objectives(title, type, description, notes) " +
                "VALUES(?, ?, ?, ?)";
        try {
            SQLiteDatabase db = DBHelper.getInstance(getApplicationContext()).getWritableDatabase();
            SQLiteStatement stm = db.compileStatement(INSERT_OBJ);
            stm.bindString(1, objTitle);
            stm.bindString(2, objType);
            stm.bindString(3, objDescription);
            stm.bindString(4, objNotes);
            stm.executeInsert();
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    private String validateObjective() {
        String errorMsg = "None";
        String objTitle = title.getText().toString();
        String objDescription = description.getText().toString();
        for (Objective objective : objectiveArrayList) {
            if (objective.getTitle().equalsIgnoreCase(objTitle)) {
                errorMsg = "An Assessment with title: " + objTitle + " already exists.";
                return errorMsg;
            }
        }
        if (objDescription.equals("")) {
            errorMsg = "Assessment description cannot be empty.";
            return errorMsg;
        }
        return errorMsg;
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
