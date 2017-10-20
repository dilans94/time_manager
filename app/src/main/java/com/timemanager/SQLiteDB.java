package com.timemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;



public class SQLiteDB extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "time_manager";
    public static int DATABASE_VERSION = 1;
    private static final String ACTIVITY_LOG = "activity_log";
    private static final String USER_PROFILE = "user_profile";

    private static final String LOG_ID = "id";

    private static final String LOG_TITLE = "title";
    private static final String LOG_DATE = "date";
    private static final String LOG_ACTIVITY_TYPE = "activity_type";
    private static final String LOG_DURATION = "duration";
    private static final String LOG_COMMENT = "comment";
    private static final String LOG_PLACE = "place";
    private static final String LOG_PHOTO = "photo";
    private static final String LOG_LATITUDE = "latitude";
    private static final String LOG_LONGITUDE = "longitude";

    private static final String PROFILE_ID = "p_id";

    private static final String PROFILE_EMAIL = "p_email";
    private static final String PROFILE_COMMENT = "p_comment";
    private static final String PROFILE_NAME = "p_name";
    private static final String PROFILE_GENDER = "p_gender";
    private static final String ACTIVITY_LOG_TABLE = "CREATE TABLE " +ACTIVITY_LOG + "(" + LOG_ID + " INTEGER PRIMARY KEY," + LOG_TITLE+ " TEXT," + LOG_DATE + " TEXT," + LOG_ACTIVITY_TYPE + " TEXT," + LOG_DURATION + " TEXT," + LOG_COMMENT + " TEXT," + LOG_PLACE + " TEXT," + LOG_PHOTO + " TEXT," + LOG_LATITUDE + " TEXT," + LOG_LONGITUDE + " TEXT" + ")";

    private static final String PROFILE = "CREATE TABLE " + USER_PROFILE + "(" +PROFILE_NAME + " TEXT," + PROFILE_EMAIL + " TEXT," + PROFILE_COMMENT + " TEXT," + PROFILE_GENDER + " TEXT" + ")";

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ACTIVITY_LOG_TABLE);
        db.execSQL(PROFILE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_LOG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE);
        onCreate(db);

    }


    public void createActivityLog(ActivitiesModal modal) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOG_TITLE, modal.getTitle());
        contentValues.put(LOG_DATE, modal.getDate());
        contentValues.put(LOG_ACTIVITY_TYPE, modal.getActivity_type());
        contentValues.put(LOG_DURATION, modal.getTotal_duration());
        contentValues.put(LOG_COMMENT, modal.getUser_comment());
        contentValues.put(LOG_PLACE, modal.getPlace());
        contentValues.put(LOG_PHOTO, modal.getPhoto());
        contentValues.put(LOG_LATITUDE, modal.getLatitude());
        contentValues.put(LOG_LONGITUDE, modal.getLongitude());
        long value=db.insert(ACTIVITY_LOG, null, contentValues);
    }

    public Cursor returnActivityLog() {
        SQLiteDatabase db = this.getReadableDatabase();
        String requestQuery = "Select * from " + ACTIVITY_LOG;
        Cursor cursor = db.rawQuery(requestQuery, null);
        return cursor;
    }

    public void deleteLog(int ID) {
        SQLiteDatabase db=this.getReadableDatabase();
        db.delete(ACTIVITY_LOG,LOG_ID+"=?",new String[]{Integer.toString(ID)});

    }

    public void updateLog(ActivitiesModal activitiesModal,int ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOG_TITLE, activitiesModal.getTitle());
        contentValues.put(LOG_DATE, activitiesModal.getDate());
        contentValues.put(LOG_ACTIVITY_TYPE, activitiesModal.getActivity_type());
        contentValues.put(LOG_DURATION, activitiesModal.getTotal_duration());
        contentValues.put(LOG_COMMENT, activitiesModal.getUser_comment());
        contentValues.put(LOG_PLACE, activitiesModal.getPlace());
        contentValues.put(LOG_PHOTO, activitiesModal.getPhoto());
        contentValues.put(LOG_LATITUDE, activitiesModal.getLatitude());
        contentValues.put(LOG_LONGITUDE, activitiesModal.getLongitude());
        db.update(ACTIVITY_LOG, contentValues, LOG_ID+"=?",new String[]{Integer.toString(ID)});
    }

    public void generateUser(UserProfile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PROFILE_NAME, profile.getU_name());

        contentValues.put(PROFILE_EMAIL, profile.getU_email());
        contentValues.put(PROFILE_COMMENT, profile.getU_comment());
        contentValues.put(PROFILE_GENDER, profile.getU_gender());

        db.insert(USER_PROFILE, null, contentValues);
    }
}
