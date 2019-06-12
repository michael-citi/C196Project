package com.michaelciti.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.ArrayList;
import model.Course;
import model.Term;
import tools.TermAdapter;

public class ViewTerms extends AppCompatActivity {

    TermAdapter termAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_terms);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = findViewById(R.id.addTermBtn);
        fab.setOnClickListener(view -> addTerm(view));
        RecyclerView recyclerView = findViewById(R.id.viewTermRV);
        ArrayList<Term> termArrayList = Term.queryAll(getApplicationContext());
        ArrayList<Course> courseArrayList = Course.queryAll(getApplicationContext());
        setTermAdapter(termArrayList, courseArrayList, recyclerView);
    }

    private void setTermAdapter(ArrayList<Term> tList, ArrayList<Course> cList, RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        termAdapter = new TermAdapter(tList, cList);
        recyclerView.setAdapter(termAdapter);
        termAdapter.notifyDataSetChanged();
    }

    public void addTerm(View view) {
        Intent intent = new Intent(this, AddTerm.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        termAdapter.notifyDataSetChanged();
    }
}
