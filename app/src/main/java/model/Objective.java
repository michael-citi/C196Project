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
    private int courseId;
    private String title;
    private String time;
    private String type;
    private String description;

    // main constructor
    public Objective(int objectiveId, int courseId, String title, String time, String type, String description) {
        this.objectiveId = objectiveId;
        this.courseId = courseId;
        this.title = title;
        this.time = time;
        this.type = type;
        this.description = description;
    }

    public static ArrayList<Objective> queryAll(Context context) {
        ArrayList<Objective> objectiveArrayList = new ArrayList<>();
        final String OBJECTIVE_QUERY = "SELECT * FROM objectives";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(OBJECTIVE_QUERY, null);
        try {
            while (cursor.moveToNext()) {
                int objectiveId = cursor.getInt(cursor.getColumnIndex("objectiveId"));
                int courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                Objective objective = new Objective(objectiveId, courseId, title, time, type, description);
                objectiveArrayList.add(objective);
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error while querying objectives from database.");
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
