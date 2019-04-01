package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import tools.DBHelper;
import static android.support.constraint.Constraints.TAG;

public class Course {

    private int courseId;
    private String title;
    private String description;
    private String startDate;
    private String expectedEnd;
    private String status;
    private int termId;
    private String notes;

    // main constructor
    public Course(int courseId, String title, String description, String startDate, String expectedEnd, String status, int termId, String notes) {
        this.courseId = courseId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.expectedEnd = expectedEnd;
        this.status = status;
        this.termId = termId;
        this.notes = notes;
    }

    // partial constructor for Course View
    public Course(String title, String description, String startDate, String expectedEnd) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.expectedEnd = expectedEnd;
    }

    // empty constructor
    public Course() {
    }

    public static ArrayList<Course> queryAll(Context context) {
        ArrayList<Course> courseArrayList = new ArrayList<>();
        final String COURSE_QUERY = "SELECT * FROM courses";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(COURSE_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    int courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                    String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                    String status = cursor.getString(cursor.getColumnIndex("status"));
                    int termId = cursor.getInt(cursor.getColumnIndex("termId"));
                    String notes = cursor.getString(cursor.getColumnIndex("notes"));

                    Course tempCourse = new Course(courseId, title, description, startDate, endDate, status, termId, notes);
                    courseArrayList.add(tempCourse);
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error while querying courses from database.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return courseArrayList;
    }

    public static int checkStartDate(int courseId) {

        return 0;
    }

    public static int checkEndDate(int courseId) {

        return 0;
    }

    // getters & setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getExpectedEnd() {
        return expectedEnd;
    }

    public void setExpectedEnd(String expectedEnd) {
        this.expectedEnd = expectedEnd;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
