package edu.sirius.android.siriuslymail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;

/**
 * Created by User on 24.01.2018.
 */

class DataBaseHelperFolder extends SQLiteOpenHelper {
    static DataBaseHelperFolder getInstance(Context context) {
        return new DataBaseHelperFolder(context);
    }

    private DataBaseHelperFolder(Context context) {
        super(context, "Folder", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Folder" +
                "(Email TEXT," +
                "FolderName);"
        );
    }

    static void insertFolder(Context context, Folder folder, String email) {
        SQLiteDatabase database = getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Email", email);
        values.put("FolderName", folder.getName());
        database.insert("Folder", null, values);
    }

    static List<String> getFolder(Context context, String email) {
        SQLiteDatabase database = getInstance(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  Folder WHERE Email=?", new String[]{email});
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        List<String> folderList = new ArrayList<>();
        while (!cursor.isLast()) {
            cursor.moveToNext();
            String folder;
            folder = cursor.getString(1);
            folderList.add(folder);
        }
        //DataSource.readData(cursor); TODO
        cursor.close();

        return folderList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
