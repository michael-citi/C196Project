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
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import model.Course;
import tools.CoursesAdapter;

public class CourseListActivity extends AppCompatActivity {

    ArrayList<Course> courseArrayList;
    RecyclerView recyclerView;
    CoursesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton addCourseFab = findViewById(R.id.addCourseFab);
        addCourseFab.setOnClickListener(view -> addCourse(view));

        recyclerView = findViewById(R.id.courseRecyclerView);
        courseArrayList = Course.queryAll(getApplicationContext());
        setCourseAdapter(courseArrayList, recyclerView);
    }

    private void setCourseAdapter(ArrayList<Course> list, @NotNull RecyclerView recyclerView) {
        adapter = new CoursesAdapter(list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void addCourse(View v) {
        Intent intent = new Intent(this, AddCourseActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // course removal is handled in the CoursesAdapter class

}
