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

public class Objective implements Parcelable {

    private int objectiveId;
    private int courseId;
    private String title;
    private String time;
    private String type;
    private String description;
    private String notes;

    // main constructor
    public Objective(int objectiveId, int courseId, String title, String time, String type, String description, String notes) {
        this.objectiveId = objectiveId;
        this.courseId = courseId;
        this.title = title;
        this.time = time;
        this.type = type;
        this.description = description;
        this.notes = notes;
    }

    // parcel Objective constructor
    private Objective(Parcel parcel) {
        objectiveId = parcel.readInt();
        courseId = parcel.readInt();
        title = parcel.readString();
        time = parcel.readString();
        type = parcel.readString();
        description = parcel.readString();
        notes = parcel.readString();
    }

    public static final Parcelable.Creator<Objective> CREATOR = new Parcelable.Creator<Objective>(){

        @Override
        public Objective createFromParcel(Parcel parcel) {
            return new Objective(parcel);
        }

        @Override
        public Objective[] newArray(int i) {
            return new Objective[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(objectiveId);
        parcel.writeInt(courseId);
        parcel.writeString(title);
        parcel.writeString(time);
        parcel.writeString(type);
        parcel.writeString(description);
        parcel.writeString(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static ArrayList<Objective> queryAll(Context context) {
        ArrayList<Objective> objectiveArrayList = new ArrayList<>();
        final String OBJECTIVE_QUERY = "SELECT * FROM objectives";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(OBJECTIVE_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int objectiveId = cursor.getInt(cursor.getColumnIndex("objectiveId"));
                    int courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String time = cursor.getString(cursor.getColumnIndex("time"));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String description = cursor.getString(cursor.getColumnIndex("description"));
                    String notes = cursor.getString(cursor.getColumnIndex("notes"));

                    Objective objective = new Objective(objectiveId, courseId, title, time, type, description, notes);
                    objectiveArrayList.add(objective);

                } while (cursor.moveToNext());
            }
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return objectiveArrayList;
    }

    public static boolean compareTimes(String time1, Context context) {
        boolean conflictResult = false;
        final String QUERY_TIMES = "SELECT time FROM objectives WHERE time = " + time1;
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(QUERY_TIMES, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    conflictResult = true;
                } while (cursor.moveToNext());
            }
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return  conflictResult;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
