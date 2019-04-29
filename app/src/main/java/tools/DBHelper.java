package tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    private static final String DATABASE_NAME = "C196";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_1 = "terms";
        private static final String TABLE_1_COL_1 = "termId";
        private static final String TABLE_1_COL_2 = "title";
        private static final String TABLE_1_COL_3 = "startDate";
        private static final String TABLE_1_COL_4 = "endDate";
    private static final String TABLE_2 = "courses";
        private static final String TABLE_2_COL_1 = "courseId";
        private static final String TABLE_2_COL_2 = "title";
        private static final String TABLE_2_COL_3 = "description";
        private static final String TABLE_2_COL_4 = "startDate";
        private static final String TABLE_2_COL_5 = "expectedEnd";
        private static final String TABLE_2_COL_6 = "status";
        private static final String TABLE_2_COL_7 = "termId";
        private static final String TABLE_2_COL_8 = "notes";
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
        private static final String TABLE_4_COL_5 = "type";
        private static final String TABLE_4_COL_6 = "description";

    // private constructor to prevent direct instantiation of database
    // call getInstance() instead
    private DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // singleton instance to prevent database leaks
    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    // allow Foreign Key constraints
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table 1 "TERMS"
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_1 + " ("
                + TABLE_1_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
                + TABLE_1_COL_2 + " TEXT NOT NULL, "
                + TABLE_1_COL_3 + " DATE, "
                + TABLE_1_COL_4 + " DATE)"
        );

        // create table 2 "COURSES"
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_2 + " ("
                + TABLE_2_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
                + TABLE_2_COL_2 + " TEXT NOT NULL, "
                + TABLE_2_COL_3 + " TEXT NOT NULL, "
                + TABLE_2_COL_4 + " DATE NOT NULL, "
                + TABLE_2_COL_5 + " DATE NOT NULL, "
                + TABLE_2_COL_6 + " TEXT NOT NULL DEFAULT 'planning', "
                + TABLE_2_COL_7 + " INTEGER NOT NULL DEFAULT 0, "
                + TABLE_2_COL_8 + " TEXT NOT NULL DEFAULT '', "
                // FK 'courses.termId' referencing 'terms.termId'
                + "FOREIGN KEY (" + TABLE_2_COL_7 + ") REFERENCES " + TABLE_1 + " (" + TABLE_1_COL_1 + "))"
        );

        // create table 3 "INSTRUCTORS"
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_3 + " ("
                + TABLE_3_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
                + TABLE_3_COL_2 + " INTEGER UNIQUE, "
                + TABLE_3_COL_3 + " TEXT NOT NULL, "
                + TABLE_3_COL_4 + " TEXT NOT NULL UNIQUE, "
                + TABLE_3_COL_5 + " TEXT NOT NULL UNIQUE, "
                // FK 'instructors.courseId' referencing 'courses.courseId'
                + "FOREIGN KEY (" + TABLE_3_COL_2 + ") REFERENCES " + TABLE_2 + " (" + TABLE_2_COL_1 + "))"
        );

        // create table 4 "OBJECTIVES"
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_4 + " ("
                + TABLE_4_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
                + TABLE_4_COL_2 + " INTEGER UNIQUE, "
                + TABLE_4_COL_3 + " TEXT NOT NULL, "
                + TABLE_4_COL_4 + " DATETIME UNIQUE, "
                + TABLE_4_COL_5 + " TEXT NOT NULL, "
                + TABLE_4_COL_6 + " TEXT NOT NULL, "
                // FK 'objectives.courseId' referencing 'courses.courseId'
                + "FOREIGN KEY (" + TABLE_4_COL_2 + ") REFERENCES " + TABLE_2 + " (" + TABLE_2_COL_1 + "))"
        );

        // initial Term 0 insert to allow courses to be listed as Unallocated (not in any specific term)
        db.execSQL("INSERT INTO terms (title) VALUES ('Unallocated')");
        // initial Instructor table inserts to allow courses to assign instructors
        db.execSQL("INSERT INTO instructors (name, phone, email) VALUES " +
                "('Amy Antonucci', '555-7854', 'amy.antonucci@wgu.edu'), " +
                "('Rob Shah', '679-0981', 'rob.shah@wgu.edu'), " +
                "('Justin Kendricks', '305-2876', 'justin.kendricks@wgu.edu'), " +
                "('Bob Boberson', '123-4567', 'bob.boberson@wgu.edu')"
        );
        // initial Objective table inserts to manipulate with assigned courses later on
        db.execSQL("INSERT INTO objectives (title, type, description) VALUES " +
                "('FQPG', 'Performance', 'Performance assessment. Add and subtract the same 12 numbers over and over again until the exam proctor falls alseep.'), " +
                "('KGTA', 'Objective', 'Objective assessment. Create the worlds largest sculpture of a buffalo. Add wings for realism.'), " +
                "('OJTF', 'Objective', 'Objective assessment. Write a ten page essay on the inner workings of quick-dry concrete.'), " +
                "('WGCK', 'Performance', 'Performance assessment. Solve all of the math problems--all of them. You have a 1 hour time limit.'), " +
                "('YDCV', 'Performance', 'Performance assessment. Draw a picture of anything you want and call it artistic mastery.'), " +
                "('HKUI', 'Objective', 'Objective assessment. History exam. Read the questions and then re-evaluate your decision to not study for this assessment.')"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_4);
        onCreate(db);
    }
}
