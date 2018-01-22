package edu.sirius.android.siriuslymail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import static edu.sirius.android.siriuslymail.PostService.SUCCESS_LOAD_MESSAGES;
import static edu.sirius.android.siriuslymail.PostServiceActions.GET_MESSAGES_ACTION;

public class MessagesFragment extends Fragment {

    private static final String EXTRA_ID = "id";
    public static final String EXTRA_FOLDER_NAME = "EXTRA_FOLDER_NAME";
    RecyclerView messagesRecyclerView;
    private String folderName;
    MessagesAdapter messagesAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getExtras().getBoolean(SUCCESS_LOAD_MESSAGES);
            if (success) {
                List<Message> messages = DataSource.getInstance().getMessages(folderName);
                messagesAdapter.setMessages(messages);
            } else {
                Toast toast = Toast.makeText(context, "can not load", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    };

    public MessagesFragment() {
    }

    public static MessagesFragment newInstance(String folderName) {
        MessagesFragment f = new MessagesFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_FOLDER_NAME, folderName);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_layout, container, false);
        folderName = getArguments().getString(EXTRA_FOLDER_NAME);
        messagesRecyclerView = rootView.findViewById(R.id.messages_recyclerview);
        messagesAdapter = new MessagesAdapter(getActivity());
        PostServiceActions.getMessages(getActivity(), folderName);
        messagesRecyclerView.setAdapter(messagesAdapter);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(GET_MESSAGES_ACTION));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    private static class MessageViewHolder extends RecyclerView.ViewHolder {

        long id;

        final LinearLayout linearLayout;
        private final TextView textItemFrom;
        private final TextView textItemTopic;
        private final TextView textItemFullMessage;

        MessageViewHolder(final View messageView) {
            super(messageView);

            this.linearLayout = messageView.findViewById(R.id.item_linear_layout);
            this.textItemFrom = messageView.findViewById(R.id.item_from);
            this.textItemTopic = messageView.findViewById(R.id.item_topic);
            this.textItemFullMessage = messageView.findViewById(R.id.item_full_message);
        }

        void bind(Message message) {
            id = message.id;
            textItemFrom.setText(message.from);
            textItemTopic.setText(message.subject);
            textItemFullMessage.setText(message.body);
        }
    }

    private static class MessagesAdapter extends RecyclerView.Adapter {

        List<Message> messages = Collections.emptyList();
        private final Activity activity;

        private MessagesAdapter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return new MessageViewHolder(v);
        }

        void setMessages(List<Message> messages) {
            this.messages = messages;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
            messageViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ReadActivity.class);

                    intent.putExtra(EXTRA_ID, messageViewHolder.id);
                    activity.startActivity(intent);
                }
            });

            final Message message = messages.get(position);
            messageViewHolder.bind(message);
        }

        @Override
        public int getItemCount() {
            return messages.size();
        }
    }
}
