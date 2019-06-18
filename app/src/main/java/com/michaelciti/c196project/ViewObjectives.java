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
import model.Objective;
import tools.ObjectivesAdapter;

public class ViewObjectives extends AppCompatActivity {

    ObjectivesAdapter objAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_objectives);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.addObjectiveFab);
        fab.setOnClickListener(view -> addObjective(view));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ArrayList<Objective> objectives = Objective.queryAll(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.objRecyclerView);
        setObjAdapter(objectives, recyclerView);
    }

    private void setObjAdapter(ArrayList<Objective> list, RecyclerView recyclerView){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        objAdapter = new ObjectivesAdapter(list);
        recyclerView.setAdapter(objAdapter);
        objAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        objAdapter.notifyDataSetChanged();
    }

    public void addObjective(View view) {
        Intent intent = new Intent(this, AddObjective.class);
        startActivity(intent);
    }
}
