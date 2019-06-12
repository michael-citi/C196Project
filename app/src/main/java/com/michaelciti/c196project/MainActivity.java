package com.michaelciti.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void viewCourses(View view) {
        Intent intent = new Intent(this, ViewCourses.class);
        startActivity(intent);
    }

    public void viewTerms(View view) {
        Intent intent = new Intent(this, ViewTerms.class);
        startActivity(intent);
    }

    public void viewObjectives(View view) {
        Intent intent = new Intent(this, ViewObjectives.class);
        startActivity(intent);
    }

    public static void showError(View view, String errorMessage) {
        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
    }
}
