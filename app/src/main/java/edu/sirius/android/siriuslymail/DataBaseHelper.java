package edu.sirius.android.siriuslymail;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "Users.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;

    static DataBaseHelper getInstance(Context context) {
        return new DataBaseHelper(context);
    }

    private DataBaseHelper(Context context) {
        super(context, "Message", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Message" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "FromUser TEXT," +
                "ToUser TEXT," +
                "Copy TEXT," +
                "Subject TEXT," +
                "Folder TEXT," +
                "Body TEXT," +
                "Email TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static List<Message> readFolder(Context context, String folder, String email) {
        SQLiteDatabase database = getInstance(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Message WHERE Folder=? AND Email=?", new String[]{folder, email});

        if (cursor.getCount() == 0) {
            cursor.close();
            return new ArrayList<>();
        }

        List<Message> messages = new ArrayList<>();
        while (!cursor.isLast()) {
            cursor.moveToNext();
            Message message = new Message();
            message.id = cursor.getLong(0);
            message.from = cursor.getString((1));
            message.to = cursor.getString((2));
            message.copy = cursor.getString((3));
            message.subject = cursor.getString((4));
            message.folder = cursor.getString((5));
            message.body = cursor.getString((6));
            messages.add(message);
        }

        cursor.close();
        return messages;
    }

    static void insertMany(Context context, List<Message> messages, String email) {
        SQLiteDatabase database = getInstance(context).getWritableDatabase();

        for (Message msg : messages) {
            ContentValues values = new ContentValues();
            values.put("FromUser", msg.from);
            values.put("ToUser", msg.to);
            values.put("Subject", msg.subject);
            values.put("Copy", msg.copy);
            values.put("Body", msg.body);
            values.put("Folder", msg.folder);
            values.put("Email", email);
            database.insert("Message", null, values);
        }
    }

    public static void clearMessages(Context context, String folder, String email) {
        getInstance(context).getWritableDatabase().delete("Message", "Folder=? AND Email=?",
                new String[]{folder, email});
    }
}