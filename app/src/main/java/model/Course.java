package model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;
import tools.DBHelper;
import static android.support.constraint.Constraints.TAG;

public class Course implements Parcelable {

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

    // partial constructor for AddCourse activity
    public Course(String title, String description, String startDate, String expectedEnd) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.expectedEnd = expectedEnd;
    }

    public Course() {}

    // parcel Course constructor
    private Course(Parcel parcel) {

        courseId = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
        startDate = parcel.readString();
        expectedEnd = parcel.readString();
        status = parcel.readString();
        termId = parcel.readInt();
        notes = parcel.readString();
    }

    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>(){

        @Override
        public Course createFromParcel(Parcel parcel) {
            return new Course(parcel);
        }

        @Override
        public Course[] newArray(int i) {
            return new Course[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(courseId);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(startDate);
        parcel.writeString(expectedEnd);
        parcel.writeString(status);
        parcel.writeInt(termId);
        parcel.writeString(notes);
    }

    public static ArrayList<Course> queryAll(Context context) {
        ArrayList<Course> courseArrayList = new ArrayList<>();
        final String COURSE_QUERY = "SELECT * FROM courses";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(COURSE_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                    String endDate = cursor.getString(cursor.getColumnIndex("expectedEnd"));
                    String status = cursor.getString(cursor.getColumnIndex("status"));
                    int termId = cursor.getInt(cursor.getColumnIndex("termId"));
                    String notes = cursor.getString(cursor.getColumnIndex("notes"));

                    Course tempCourse = new Course(courseId, title, description, startDate, endDate, status, termId, notes);
                    courseArrayList.add(tempCourse);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return courseArrayList;
    }

    public static void deleteCourse(int courseId, Context context) {
        final String REMOVE_COURSE ="DELETE FROM courses WHERE courseId = " + courseId;
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL(REMOVE_COURSE);
    }

    // Lookup for any courses with a specific termId
    public static boolean checkForCourses(Context context, int termId) {
        boolean isFound = false;
        final String QUERY_COURSES = "SELECT termId FROM courses " +
                "WHERE termId = " + termId;
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY_COURSES, null);
        try {
            if (cursor.moveToFirst()) {
                isFound = true;
            }
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isFound;
    }

    // Comparison needed to detect whether a course already exists with the same courseId
    public static boolean courseCompare(Context context, int courseId) {
        boolean isFound = false;
        final String QUERY_COURSES = "SELECT courseId FROM courses " +
                "WHERE courseId = " + courseId;
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY_COURSES, null);
        try {
            if (cursor.moveToFirst()) {
                isFound = true;
            }
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isFound;
    }

    @Override
    public int describeContents() {
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
