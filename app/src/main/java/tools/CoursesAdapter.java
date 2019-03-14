package tools;

import android.content.Context;
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

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private List<Course> courseList;

    public CoursesAdapter(List<Course> courses) {
        courseList = courses;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView courseName;
        public TextView courseDescription;
        public TextView courseStartDate;
        public TextView courseEndDate;
        public Button notesBtn;

        public ViewHolder(View view) {
            super(view);
            courseName = view.findViewById(R.id.courseName);
            courseDescription = view.findViewById(R.id.courseDescription);
            courseStartDate = view.findViewById(R.id.courseStartDate);
            courseEndDate = view.findViewById(R.id.courseEndDate);
            notesBtn = view.findViewById(R.id.notesButton);
        }
    }

    @NonNull
    @Override
    public CoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View coursesView = inflater.inflate(R.layout.course_content, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(coursesView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesAdapter.ViewHolder viewHolder, int i) {
        Course course = courseList.get(i);

        TextView courseTitle = viewHolder.courseName;
        courseTitle.setText("Course Name: " + course.getTitle());

        TextView courseDescription = viewHolder.courseDescription;
        courseDescription.setText("Course Description: " + course.getDescription());

        TextView courseStartDate = viewHolder.courseStartDate;
        courseStartDate.setText("Start Date: " + course.getStartDate());

        TextView courseEndDate = viewHolder.courseEndDate;
        courseEndDate.setText("Expected End Date: " + course.getExpectedEnd());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
