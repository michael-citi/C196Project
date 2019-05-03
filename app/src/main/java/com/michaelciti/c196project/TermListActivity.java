package com.michaelciti.c196project;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import model.Term;

public class TermListActivity extends AppCompatActivity {

    private ArrayList<Term> termArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addTerm = findViewById(R.id.addTerm);
        addTerm.setOnClickListener(view -> {
            //TODO: Give FAB functionality
        });
        termArrayList = Term.queryAll(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



}
