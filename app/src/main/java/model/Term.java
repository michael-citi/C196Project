package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import com.michaelciti.c196project.MainActivity;
import java.util.ArrayList;
import tools.DBHelper;
import static android.support.constraint.Constraints.TAG;

public class Term {

    private int termId;
    private String title;
    private String startDate;
    private String endDate;

    // main constructor
    public Term(int termId, String title, String startDate, String endDate) {
        this.termId = termId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ArrayList<Term> queryAll(Context context) {
        ArrayList<Term> termArrayList = new ArrayList<>();
        final String TERM_QUERY = "SELECT * FROM terms";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(TERM_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    int termId = cursor.getInt(cursor.getColumnIndex("termId"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                    String endDate = cursor.getString(cursor.getColumnIndex("endDate"));

                    Term term = new Term (termId, title, startDate, endDate);
                    termArrayList.add(term);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error while querying terms from database.");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return termArrayList;
    }

    public static void deleteTerm(View view, int termId, Context context) {
        if (Course.checkForCourses(context, termId)) {
            MainActivity.showError(view, "Unable to delete term. Active course(s) still remain under that term.");
        } else {
            final String UPDATE_TERM_ID = "UPDATE courses SET termId = 1" +
                    " WHERE termId = " + termId;
            final String REMOVE_TERM ="DELETE FROM terms WHERE termId = " + termId;
            SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
            db.execSQL(UPDATE_TERM_ID);
            db.execSQL(REMOVE_TERM);
            MainActivity.showError(view, "Term successfully deleted.");
        }
    }

    // getters & setters
    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
