package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import tools.DBHelper;
import static android.support.constraint.Constraints.TAG;

public class Instructor {

    private int instructorId;
    private String name;
    private String phone;
    private String email;

    // main constructor
    public Instructor(int instructorId, String name, String phone, String email) {
        this.instructorId = instructorId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // partial constructor
    public Instructor(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    // empty constructor
    public Instructor() {
    }

    public static ArrayList<Instructor> queryAll(Context context) {
        ArrayList<Instructor> instructorArrayList = new ArrayList<>();
        final String INSTRUCT_QUERY = "SELECT * FROM instructors";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(INSTRUCT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    int instId = cursor.getInt(cursor.getColumnIndex("instructorId"));
                    String instName = cursor.getString(cursor.getColumnIndex("name"));
                    String instPhone = cursor.getString(cursor.getColumnIndex("phone"));
                    String instEmail = cursor.getString(cursor.getColumnIndex("email"));

                    Instructor instructor = new Instructor(instId, instName, instPhone, instEmail);
                    instructorArrayList.add(instructor);
                }
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
