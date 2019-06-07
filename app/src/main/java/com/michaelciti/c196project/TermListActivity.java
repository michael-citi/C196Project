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
import model.Term;
import tools.TermAdapter;

public class TermListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.termRecyclerView);

        FloatingActionButton addTermBtn = findViewById(R.id.addTermBtn);
        addTermBtn.setOnClickListener(view -> addTerm(view));

        ArrayList<Term> termArrayList = Term.queryAll(getApplicationContext());
        setTermAdapter(termArrayList, recyclerView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setTermAdapter(ArrayList<Term> terms, RecyclerView recyclerView) {
        TermAdapter adapter = new TermAdapter(terms);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void addTerm(View v) {
        Intent intent = new Intent(this, AddTermActivity.class);
        startActivity(intent);
    }
    // term removal is handled in the TermAdapter class
}
