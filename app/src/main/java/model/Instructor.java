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

public class Instructor implements Parcelable {

    private static final String TAG = "Instructor.java";
    private int instructorId;
    private int courseId;
    private String name;
    private String phone;
    private String email;

    // main constructor
    public Instructor(int instructorId, int courseId, String name, String phone, String email) {
        this.instructorId = instructorId;
        this.courseId = courseId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // parcel Instructor constructor
    private Instructor(Parcel parcel) {
        instructorId = parcel.readInt();
        courseId = parcel.readInt();
        name = parcel.readString();
        phone = parcel.readString();
        email = parcel.readString();
    }

    public static final Parcelable.Creator<Instructor> CREATOR = new Parcelable.Creator<Instructor>(){

        @Override
        public Instructor createFromParcel(Parcel parcel) {
            return new Instructor(parcel);
        }

        @Override
        public Instructor[] newArray(int i) {
            return new Instructor[0];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(instructorId);
        parcel.writeInt(courseId);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static ArrayList<Instructor> queryAll(Context context) {
        ArrayList<Instructor> instructorArrayList = new ArrayList<>();
        final String INSTRUCT_QUERY = "SELECT * FROM instructors";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(INSTRUCT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int instId = cursor.getInt(cursor.getColumnIndex("instructorId"));
                    int courseId = cursor.getInt(cursor.getColumnIndex("courseId"));
                    String instName = cursor.getString(cursor.getColumnIndex("name"));
                    String instPhone = cursor.getString(cursor.getColumnIndex("phone"));
                    String instEmail = cursor.getString(cursor.getColumnIndex("email"));
                    Instructor instructor = new Instructor(instId, courseId, instName, instPhone, instEmail);
                    instructorArrayList.add(instructor);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error while running query for instructors from database.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return instructorArrayList;
    }

    // getters & setters
    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
