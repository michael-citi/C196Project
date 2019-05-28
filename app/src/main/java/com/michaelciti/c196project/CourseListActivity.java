package com.michaelciti.c196project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import java.util.ArrayList;
import model.Course;
import tools.CoursesAdapter;

public class CourseListActivity extends AppCompatActivity {

    private CoursesAdapter adapter;
    ArrayList<Course> courseArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.courseRecyclerView);

        courseArrayList = Course.queryAll(getApplicationContext());
        setCourseAdapter(courseArrayList, recyclerView);
    }

    private void setCourseAdapter(ArrayList<Course> list, RecyclerView recyclerView) {
        adapter = new CoursesAdapter(list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void remCourse(View v) {
        
        adapter.notifyDataSetChanged();
    }

}
