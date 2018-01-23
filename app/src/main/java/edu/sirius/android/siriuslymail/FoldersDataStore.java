package edu.sirius.android.siriuslymail;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Folder;


public class FoldersDataStore {
    private  List<Folder> folders;
    private static final FoldersDataStore ourInstance = new FoldersDataStore();

    public static FoldersDataStore getInstance() {
        return ourInstance;
    }

    private FoldersDataStore() {
    }
    public List<Folder> getFolders() {
        return folders;
    }

    public  void setFolders(List<Folder> folders1) {
        folders = folders1;
    }
    public List<String> returnAllFoldersNames(){
        List<String> folders1 = new ArrayList<>();
        for(Folder i:folders){
            folders1.add(i.getFullName());
        }
        return folders1;
    }
}
