package edu.sirius.android.siriuslymail;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 15.01.2018.
 */

public class DataSource {
        private List<Message>  messages=new ArrayList<>();
    private final Map<Long, Message> idToMessage = new HashMap<>();
    Message getMessage(int position) {
        return messages.get(position);
    }

    List<Message> getMessages(){
        return messages;
    }
    void update(Context context,String folder){
        messages=DataBaseHelper.readFolder(context, folder);
    }
}
