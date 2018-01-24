package edu.sirius.android.siriuslymail;

import android.content.Context;

/**
 * Created by User on 24.01.2018.
 */

public class FoldersManager {
    private final Context context;
    private static FoldersManager foldersManager;
    public FoldersManager(Context context) {
        this.context = context;
    }

    public static void initLogin(Context context) {
        foldersManager = new FoldersManager(context);
    }
    static FoldersManager getInstance() {
        return foldersManager;
    }

    public FolderNameWithEmail getFolder(String email){
        return DataBaseHelperFolder.getFolder(context,email);
    }

    public void insertFolder(FolderNameWithEmail folders) {
        DataBaseHelperFolder.insertFolder(context, folders);
    }
    public void clear(){
        DataBaseHelperLogin.dropdb();
    }
}
