package edu.sirius.android.siriuslymail;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 21.01.2018.
 */

public class UsersManager {
    private List<User> logins = new ArrayList<>();
    private final Context context;
    private static UsersManager usersManager;
    private String usersEmail;

    public UsersManager(Context context) {
        this.context = context;
    }

    public static void initLogin(Context context) {
        usersManager = new UsersManager(context);
    }
    static UsersManager getInstance() {
        return usersManager;
    }

   public User getActiveUser(){
        return DataBaseHelperLogin.getActive(context);
    }

    public void saveUser(User users) {
        DataBaseHelperLogin.saveUser(context, users);
    }
    public void clear(){
       DataBaseHelperLogin.dropdb();
    }


    public List<String> getUsersEmail() {
        List<String> emails = new ArrayList<>();
        for (User user : DataBaseHelperLogin.getUsers(context)) {
            emails.add(user.getEmail());
        }
        return emails;
    }

    public void updateActiveUser(String usersEmail) {
        DataBaseHelperLogin.updateActiveUser(context, usersEmail);
    }
}
