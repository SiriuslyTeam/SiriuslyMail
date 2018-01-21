package edu.sirius.android.siriuslymail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by User on 20.01.2018.
 */

public class DataBaseHelperLogin extends SQLiteOpenHelper {

    static DataBaseHelperLogin getInstance(Context context) {
        return new DataBaseHelperLogin(context);
    }
    public DataBaseHelperLogin(Context context) {
        super(context, "Login", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Login"+
                "(Email TEXT," +
                "Password TEXT,"+
                "Active INTEGER,"+
                "Host TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersions, int newVersions) {

    }
    static void readOne(Context context){
        SQLiteDatabase database=getInstance(context).getReadableDatabase();
        Cursor cursorActive=database.rawQuery("SELECT * FROM  Login WHERE Active=?", new String[]{String.valueOf(1)});
        if (cursorActive.getCount() == 0) {
            cursorActive.close();
            return;
        }

        //DataSource.readData(cursor);
        cursorActive.close();
    }
    static void insertMany(Context context, ArrayList<Login> logins){
        SQLiteDatabase database=getInstance(context).getWritableDatabase();


        for (Login log:logins){
            ContentValues values=new ContentValues();
            values.put("Email",log.email);
            values.put("Password",log.password);
            values.put("Host",log.host);
            values.put("Active",log.active);
            database.insert("Login",null,values);
        }
    }

}
