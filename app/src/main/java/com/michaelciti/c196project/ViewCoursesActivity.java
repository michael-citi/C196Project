package com.michaelciti.c196project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import model.Course;
import tools.CoursesAdapter;

public class ViewCoursesActivity extends AppCompatActivity {

    ArrayList<Course> courses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView courseList = findViewById(R.id.coursesView);
        courses = Course.queryAll(getApplicationContext());
        CoursesAdapter adapter = new CoursesAdapter(courses);
        courseList.setAdapter(adapter);
        courseList.setLayoutManager(new LinearLayoutManager(this));

    }

}
