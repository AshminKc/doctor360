package com.example.doctor360.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.doctor360.ChatModel;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "chatdb";
    private static final String TABLE_NAME = "message";
    private static final String ID_COL = "id";
    private static final String PATIENT_ID_COL = "patient_id";
    private static final String DOCTOR_ID_COL = "doctor_id";
    private static final String CHAT_DATE_COL = "chat_date";
    private static final String CHAT_TIME_COL = "chat_time";
    private static final String PATIENT_MESSAGE_COL = "patient_message";
    private static final String DOCTOR_MESSAGE_COL = "doctor_message";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PATIENT_ID_COL + " TEXT,"
                + DOCTOR_ID_COL + " TEXT,"
                + CHAT_DATE_COL + " TEXT,"
                + CHAT_TIME_COL + " TEXT,"
                + PATIENT_MESSAGE_COL + " TEXT,"
                + DOCTOR_MESSAGE_COL + " TEXT)";

        db.execSQL(query);
    }

    public void addNewChat(String patientId, String doctorId, String chatDate, String chatTime, String patMessage, String docMessage) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PATIENT_ID_COL, patientId);
        values.put(DOCTOR_ID_COL, doctorId);
        values.put(CHAT_DATE_COL, chatDate);
        values.put(CHAT_TIME_COL, chatTime);
        values.put(PATIENT_MESSAGE_COL, patMessage);
        values.put(DOCTOR_MESSAGE_COL, docMessage);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    ChatModel getMessageOfSingle(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { PATIENT_ID_COL, DOCTOR_ID_COL, CHAT_DATE_COL, CHAT_TIME_COL,
                        PATIENT_MESSAGE_COL, DOCTOR_MESSAGE_COL }, PATIENT_ID_COL + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ChatModel contact = new ChatModel(cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5), cursor.getString(6));
        // return contact
        return contact;
    }

    public ArrayList<ChatModel> readChatMessage() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME , null);
        ArrayList<ChatModel> courseModalArrayList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                courseModalArrayList.add(new ChatModel
                        (
                                cursorCourses.getString(1),
                                cursorCourses.getString(2),
                                cursorCourses.getString(3),
                                cursorCourses.getString(4),
                                cursorCourses.getString(5),
                                cursorCourses.getString(6)));
            } while (cursorCourses.moveToNext());
        }
        cursorCourses.close();
        return courseModalArrayList;
    }


    public void upadateChatMessage(String patientId, String doctorId, String chatDate, String chatTime, String patMessage, String docMessage, String newDocMessage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PATIENT_ID_COL, patientId);
        values.put(DOCTOR_ID_COL, doctorId);
        values.put(CHAT_DATE_COL, chatDate);
        values.put(CHAT_TIME_COL, chatTime);
        values.put(PATIENT_MESSAGE_COL, patMessage);
        values.put(DOCTOR_MESSAGE_COL, docMessage);

        db.update(TABLE_NAME, values, "patientId=?", new String[]{newDocMessage});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
