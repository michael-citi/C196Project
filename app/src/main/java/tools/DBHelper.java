package tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "C196";

    private static final String TABLE_1 = "terms";
        private static final String TABLE_1_COL_1 = "termId";
        private static final String TABLE_1_COL_2 = "title";
        private static final String TABLE_1_COL_3 = "startDate";
        private static final String TABLE_1_COL_4 = "endDate";
    private static final String TABLE_2 = "courses";
        private static final String TABLE_2_COL_1 = "courseId";
        private static final String TABLE_2_COL_2 = "title";
        private static final String TABLE_2_COL_3 = "startDate";
        private static final String TABLE_2_COL_4 = "expectedEnd";
        private static final String TABLE_2_COL_5 = "status";
        private static final String TABLE_2_COL_6 = "termId";
        private static final String TABLE_2_COL_7 = "notes";
    private static final String TABLE_3 = "instructors";
        private static final String TABLE_3_COL_1 = "instructorId";
        private static final String TABLE_3_COL_2 = "courseId";
        private static final String TABLE_3_COL_3 = "name";
        private static final String TABLE_3_COL_4 = "phone";
        private static final String TABLE_3_COL_5 = "email";
    private static final String TABLE_4 = "objectives";
        private static final String TABLE_4_COL_1 = "objectiveId";
        private static final String TABLE_4_COL_2 = "courseId";
        private static final String TABLE_4_COL_3 = "title";
        private static final String TABLE_4_COL_4 = "time";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table 1 "TERMS"
        db.execSQL("CREATE TABLE " + TABLE_1 + " ("
                + TABLE_1_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
                + TABLE_1_COL_2 + " TEXT NOT NULL, "
                + TABLE_1_COL_3 + " DATE NOT NULL, "
                + TABLE_1_COL_4 + " DATE NOT NULL)"
                // no FK
        );

        // create table 2 "COURSES"
        db.execSQL("CREATE TABLE " + TABLE_2 + " ("
                + TABLE_2_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
                + TABLE_2_COL_2 + " TEXT NOT NULL, "
                + TABLE_2_COL_3 + " DATE NOT NULL, "
                + TABLE_2_COL_4 + " DATE NOT NULL, "
                + TABLE_2_COL_5 + " TEXT NOT NULL, "
                + TABLE_2_COL_6 + " INTEGER, "
                + TABLE_2_COL_7 + " TEXT DEFAULT '', "
                // FK 'courses.termId' referencing 'terms.termId'
                + "FOREIGN KEY (" + TABLE_2_COL_6 + ") REFERENCES " + TABLE_1 + " (" + TABLE_1_COL_1 + "))"
        );

        // create table 3 "INSTRUCTORS"
        db.execSQL("CREATE TABLE " + TABLE_3 + " ("
                + TABLE_3_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
                + TABLE_3_COL_2 + " INTEGER, "
                + TABLE_3_COL_3 + " TEXT NOT NULL, "
                + TABLE_3_COL_4 + " TEXT NOT NULL, "
                + TABLE_3_COL_5 + " TEXT NOT NULL, "
                // FK 'instructors.courseId' referencing 'courses.courseId'
                + "FOREIGN KEY (" + TABLE_3_COL_2 + ") REFERENCES " + TABLE_2 + " (" + TABLE_2_COL_1 + "))"
        );

        // create table 4 "OBJECTIVES"
        db.execSQL("CREATE TABLE " + TABLE_4 + " ("
                + TABLE_4_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
                + TABLE_4_COL_2 + " INTEGER, "
                + TABLE_4_COL_3 + " TEXT NOT NULL, "
                + TABLE_4_COL_4 + " DATETIME NOT NULL, "
                // FK 'objectives.courseId' referencing 'courses.courseId'
                + "FOREIGN KEY (" + TABLE_4_COL_2 + ") REFERENCES " + TABLE_2 + " (" + TABLE_2_COL_1 + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_4);
        onCreate(db);
    }
}
