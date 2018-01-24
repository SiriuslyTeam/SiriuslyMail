package edu.sirius.android.siriuslymail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.01.2018.
 */

class DataBaseHelperFolder extends SQLiteOpenHelper {
    static DataBaseHelperFolder getInstance(Context context) {
        return new DataBaseHelperFolder(context);
    }

    private DataBaseHelperFolder(Context context) {
        super(context, "post", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Folder" +
                "(Email TEXT," +
                "FolderName);"
        );
    }
    static void insertFolder(Context context, FolderNameWithEmail folderNameWithEmail){
        SQLiteDatabase database=getInstance(context).getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("Email", folderNameWithEmail.getEmailFolder());
        values.put("FolderName", folderNameWithEmail.getFolderName());
        database.insert("FolderNameWithEmail",null,values);
    }
    static List<FolderNameWithEmail> getFolder(Context context, String email){
        SQLiteDatabase database = getInstance(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM  Folder WHERE Email=?", new String[]{email});
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }
        List<FolderNameWithEmail> folderNameWithEmailList=new ArrayList<>();
        while (!cursor.isLast()) {
            cursor.moveToNext();
            FolderNameWithEmail folderNameWithEmail = new FolderNameWithEmail();
            folderNameWithEmail.setEmail(cursor.getString(0));
            folderNameWithEmail.setFolderName(cursor.getString(1));
            folderNameWithEmailList.add(folderNameWithEmail);
        }
        //DataSource.readData(cursor); TODO
        cursor.close();

        return folderNameWithEmailList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
