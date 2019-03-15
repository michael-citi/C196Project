package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import tools.DBHelper;
import static android.support.constraint.Constraints.TAG;

public class Objective {

    private int objectiveId;
    private String title;
    private int courseId;
    private String time;
    private String type;

    // main constructor
    public Objective(int objectiveId, String title, int courseId, String time, String type) {
        this.objectiveId = objectiveId;
        this.title = title;
        this.courseId = courseId;
        this.time = time;
        this.type = type;
    }

    // partial constructor
    public Objective(String title, String time, String type) {
        this.title = title;
        this.time = time;
        this.type = type;
    }

    // empty constructor
    public Objective() {
    }

    public static ArrayList<Objective> queryAll(Context context) {
        ArrayList<Objective> objectiveArrayList = new ArrayList<>();
        final String OBJECTIVE_QUERY = "SELECT * FROM objectives";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(OBJECTIVE_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    //TODO: convert timestamp to String object
                    int objectiveId = cursor.getInt(cursor.getColumnIndex("objectiveId"));
                    int courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    // String time =
                    String type = cursor.getString(cursor.getColumnIndex("type"));

                    //TODO: create Objective objects and add to ArrayList
                }
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error while querying terms from database.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return objectiveArrayList;
    }

    public int getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(int objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
