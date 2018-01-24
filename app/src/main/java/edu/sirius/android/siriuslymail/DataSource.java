package edu.sirius.android.siriuslymail;

import android.content.Context;

import java.util.List;

public class DataSource {

    private final Context context;
    private static DataSource dataSource;

    public DataSource(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        dataSource = new DataSource(context);
    }

    public static DataSource getInstance() {
        return dataSource;
    }


    public List<Message> getMessages(String folder) {
        return DataBaseHelper.readFolder(context, folder, UsersManager.getInstance().getActiveUser().getEmail());
    }

    public void saveMessages(List<Message> messages) {
        DataBaseHelper.insertMany(context, messages, UsersManager.getInstance().getActiveUser().getEmail());
    }

    public void clearMessages(String folder) {
        // TODO
        DataBaseHelper.clearMessages(context,folder, UsersManager.getInstance().getActiveUser().getEmail());
    }

    public void dropDb() {
        // TODO
    }

}
