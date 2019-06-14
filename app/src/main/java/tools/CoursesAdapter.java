package tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.michaelciti.c196project.CourseDetailActivity;
import com.michaelciti.c196project.R;
import java.util.ArrayList;
import model.Course;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private ArrayList<Course> courseList;

    public CoursesAdapter(ArrayList<Course> courses) {
        courseList = courses;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName;
        public TextView courseDescription;
        public TextView courseStartDate;
        public TextView courseEndDate;
        public Button delCourseBtn;
        public Button detailsBtn;

        public CourseViewHolder(View view) {
            super(view);
            courseName = view.findViewById(R.id.courseName);
            courseDescription = view.findViewById(R.id.courseDescription);
            courseStartDate = view.findViewById(R.id.courseStartDate);
            courseEndDate = view.findViewById(R.id.courseEndDate);
            delCourseBtn = view.findViewById(R.id.delCourseBtn);
            detailsBtn = view.findViewById(R.id.detailsButton);
        }
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.course_content, viewGroup, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder viewHolder, int i) {
        Course course = courseList.get(i);
        final String name = "Course Name: " + course.getTitle();
        final String desc = "Description: " + course.getDescription();
        final String start = "Begin: " + course.getStartDate();
        final String expectEnd = "Expected End: " + course.getExpectedEnd();

        viewHolder.courseName.setText(name);
        viewHolder.courseDescription.setText(desc);
        viewHolder.courseStartDate.setText(start);
        viewHolder.courseEndDate.setText(expectEnd);

        viewHolder.delCourseBtn.setOnClickListener(view -> {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
            alertBuilder.setMessage("Are you sure you want to delete this Course?");
            alertBuilder.setPositiveButton("Yes", (dialogInterface, j) -> {
                Course.deleteCourse(course.getCourseId(), view.getContext());
                courseList.remove(i);
                notifyItemRemoved(i);
            });
            alertBuilder.setNegativeButton("No", (dialogInterface, j) -> {
                ((Activity)view.getContext()).finish();
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        });

        viewHolder.detailsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CourseDetailActivity.class);
            intent.putExtra("Course", course);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
