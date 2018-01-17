package edu.sirius.android.siriuslymail;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessagesFragment extends Fragment {

    private static final String EXTRA_ID = "id";

    RecyclerView messagesRecyclerView;

    public MessagesFragment() {}

    public void refreshMessages() {
        DataSource.getInstance().getMessages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_layout, container, false);

        final DataSource dataSource = DataSource.getInstance();
        messagesRecyclerView = rootView.findViewById(R.id.messages_recyclerview);

        messagesRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
                return new MessageViewHolder(v);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                final MessageViewHolder messageViewHolder = (MessageViewHolder) holder;
                messageViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ReadActivity.class);

                        intent.putExtra(EXTRA_ID, messageViewHolder.id);
                        startActivity(intent);
                    }
                });

                final Message message = dataSource.getMessage(position);
                messageViewHolder.bind(message);
            }

            @Override
            public int getItemCount() {
                return dataSource.getCount();
            }
        });
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        return rootView;
    }

    private class MessageViewHolder extends RecyclerView.ViewHolder {

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
}
