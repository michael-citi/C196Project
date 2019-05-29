package tools;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.michaelciti.c196project.R;
import java.util.List;
import model.Course;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CourseViewHolder> {

    private List<Course> courseList;

    public CoursesAdapter(List<Course> courses) {
        courseList = courses;
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName;
        public TextView courseDescription;
        public TextView courseStartDate;
        public TextView courseEndDate;
        public Button delCourseBtn;

        public CourseViewHolder(View view) {
            super(view);
            courseName = view.findViewById(R.id.courseName);
            courseDescription = view.findViewById(R.id.courseDescription);
            courseStartDate = view.findViewById(R.id.courseStartDate);
            courseEndDate = view.findViewById(R.id.courseEndDate);
            delCourseBtn = view.findViewById(R.id.delCourseBtn);
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

        viewHolder.courseName.setText("Course Name: " + course.getTitle());
        viewHolder.courseDescription.setText("Course Description: " + course.getDescription());
        viewHolder.courseStartDate.setText("Start Date: " + course.getStartDate());
        viewHolder.courseEndDate.setText("Expected End Date: " + course.getExpectedEnd());

        viewHolder.delCourseBtn.setOnClickListener(view -> {
            Course.deleteCourse(i, viewHolder.delCourseBtn.getContext());
            courseList.remove(i);
            notifyItemRemoved(i);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
