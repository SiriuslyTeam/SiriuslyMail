package edu.sirius.android.siriuslymail;

import android.provider.ContactsContract;

/**
 * Created by User on 24.01.2018.
 */

public class FolderNameWithEmail {
    private String email;
    private String folderName;
    FolderNameWithEmail(){}
    public String getEmailFolder() {
        return email;
    }
    public String getFolderName(){return folderName;}
    public FolderNameWithEmail(String email, String folderName) {
        this.email = email;
        this.folderName = folderName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFolderName(String folderName){this.folderName=folderName;}

}
