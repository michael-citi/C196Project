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
import tools.CoursesAdapter;

public class ViewCourses extends AppCompatActivity {

    CoursesAdapter coursesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.addCourseFab);
        fab.setOnClickListener(view -> addCourse(view));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.viewCoursesRV);
        ArrayList<Course> courseArrayList = Course.queryAll(getApplicationContext());
        setCoursesAdapter(courseArrayList, recyclerView);
    }

    private void setCoursesAdapter(ArrayList<Course> cList, RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        coursesAdapter = new CoursesAdapter(cList);
        recyclerView.setAdapter(coursesAdapter);
        coursesAdapter.notifyDataSetChanged();
    }

    public void addCourse(View view) {
        Intent intent = new Intent(this, AddCourse.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        coursesAdapter.notifyDataSetChanged();
    }
}
