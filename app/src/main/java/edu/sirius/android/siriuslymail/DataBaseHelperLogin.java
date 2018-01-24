package edu.sirius.android.siriuslymail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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
                "SmtpHost TEXT,"+
                "ImapHost TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersions, int newVersions) {

    }

    static List<User> getUsers(Context context){
        SQLiteDatabase database = getInstance(context).getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Login=?",null);
        List<User> users = new ArrayList<>();
        while (!cursor.isLast()) {
            cursor.moveToNext();
            User user = new User();
            user.setEmail(cursor.getString(0));
            user.setPassword(cursor.getString(1));
            user.setActive(cursor.getLong(2));
            user.setSmtpHost(cursor.getString(3));
            user.setImapHost(cursor.getString(4));
            users.add(user);
        }

        cursor.close();
        return users;
    }

    static User getActive(Context context) {
        SQLiteDatabase database = getInstance(context).getReadableDatabase();
        Cursor cursorId = database.rawQuery("SELECT * FROM  Login WHERE Active=?", new String[]{String.valueOf(1)});
        if (cursorId.getCount() == 0) {
            cursorId.close();
            return null;
        }
        cursorId.moveToNext();
        User user= new User();
        user.setEmail(cursorId.getString(0));
        user.setPassword(cursorId.getString(1));
        user.setActive(cursorId.getLong(2));
        user.setSmtpHost(cursorId.getString(3));
        user.setImapHost(cursorId.getString(4));

        //DataSource.readData(cursor); TODO
        cursorId.close();

        return user;
    }
    static void saveUser(Context context, User user){
        SQLiteDatabase database=getInstance(context).getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("Email",user.getEmail());
            values.put("Password",user.getPassword());
            values.put("Active",user.getActive());
            values.put("ImapHost",user.getImapHost());
            values.put("SmtpHost",user.getSmtpHost());


            database.insert("Login",null,values);
    }
    public static void dropdb(){

    }


}
