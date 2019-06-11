package model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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

    public static int termComparator(int year, int month, int day, Context context) {
        int startYear, startMonth, startDay;
        int endYear, endMonth, endDay;
        final String TERM_QUERY = "SELECT startDate, endDate, " +
                "strftime('%Y', startDate) as sYEAR, " +
                "strftime('%m', startDate) as sMONTH, " +
                "strftime('%d', startDate) as sDAY, " +
                "strftime('%Y', endDate) as eYEAR, " +
                "strftime('%m', endDate) as eMONTH, " +
                "strftime('%d', endDate) as eDAY " +
                "FROM terms";
        SQLiteDatabase db = DBHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(TERM_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    startYear = cursor.getInt(cursor.getColumnIndex("sYEAR"));
                    startMonth = cursor.getInt(cursor.getColumnIndex("sMONTH"));
                    startDay = cursor.getInt(cursor.getColumnIndex("sDAY"));
                    endYear = cursor.getInt(cursor.getColumnIndex("eYEAR"));
                    endMonth = cursor.getInt(cursor.getColumnIndex("eMONTH"));
                    endDay = cursor.getInt(cursor.getColumnIndex("eDAY"));

                    if ((year >= startYear) && (year <= endYear) && (month >= startMonth) && (month <= endMonth) && (day >= startDay) && (day <= endDay)) {
                        Log.i(TAG, "Course date range set within another active course date range.");
                        return 1;
                    }
                } while (cursor.moveToNext());
            }
        } catch (SQLException ex) {
            Log.d(TAG, ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public static void deleteTerm(int termId, Context context) {
        final String UPDATE_TERM_ID = "UPDATE courses SET termId = 1" +
                " WHERE termId = " + termId;
        final String REMOVE_TERM ="DELETE FROM terms WHERE termId = " + termId;
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        db.execSQL(UPDATE_TERM_ID);
        db.execSQL(REMOVE_TERM);
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
