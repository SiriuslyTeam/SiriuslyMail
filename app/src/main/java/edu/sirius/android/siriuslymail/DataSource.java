package edu.sirius.android.siriuslymail;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSource {
    private List<Message> messages = new ArrayList<>();
    private final Map<Long, Message> idToMessage = new HashMap<>();

    private static DataSource dataSource = new DataSource();
    static DataSource getInstance() {
        return dataSource;
    }

    Message getMessage(long id) {
        return idToMessage.get(id);
    }

    List<Message> getMessages() {
        return messages;
    }

    void updateFromDatabase(Context context, String folder) {
        messages = DataBaseHelper.readFolder(context, folder);
        idToMessage.clear();
        for (Message m : messages)
            idToMessage.put(m.id, m);
    }

    int getCount() {
        return messages.size();
    }
}
