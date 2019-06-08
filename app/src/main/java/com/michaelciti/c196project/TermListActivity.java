package com.michaelciti.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.ArrayList;

import model.Course;
import model.Term;
import tools.TermAdapter;

public class TermListActivity extends AppCompatActivity {

    ArrayList<Term> termArrayList = new ArrayList<>();
    ArrayList<Course> courseArrayList = new ArrayList<>();
    RecyclerView termRecyclerView;
    TermAdapter termAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        termRecyclerView = findViewById(R.id.termRecyclerView);

        FloatingActionButton addTermBtn = findViewById(R.id.addTermBtn);
        addTermBtn.setOnClickListener(view -> addTerm(view));

        termArrayList = Term.queryAll(getApplicationContext());
        courseArrayList = Course.queryAll(getApplicationContext());
        setTermAdapter(termArrayList, courseArrayList, termRecyclerView);
        termAdapter.notifyDataSetChanged();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setTermAdapter(ArrayList<Term> terms, ArrayList<Course> courses, RecyclerView recyclerView) {
        termAdapter = new TermAdapter(terms, courses);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(termAdapter);
    }

    public void addTerm(View v) {
        Intent intent = new Intent(this, AddTermActivity.class);
        startActivity(intent);
    }
    // term removal is handled in the TermAdapter class
}
