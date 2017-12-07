package com.yellowbytestudios.chatquest.chatmessage;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yellowbytestudios.chatquest.R;

import java.util.List;

/**
 * Created by Robert on 20-Nov-17.
 * <p>
 * RecyclerView adapter for message display.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private final View.OnClickListener mOnClickListener = new ChatMessageClickListener();
    private static final int MY_MESSAGE = 0;
    private static final int FRIEND_MESSAGE = 1;
    private List<ChatMessage> messageList;
    private String userName;

    public MessageAdapter(List<ChatMessage> list, String userName) {
        messageList = list;
        this.userName = userName;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, date;

        MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.message_text);
            date = itemView.findViewById(R.id.message_time);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case MY_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message, parent, false);
                view.setOnClickListener(mOnClickListener);
                return new MyViewHolder(view);
            case FRIEND_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
                view.setOnClickListener(mOnClickListener);
                return new MyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatMessage mess = messageList.get(position);
        holder.title.setText(mess.getMessageText());
        holder.date.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", mess.getMessageTime()));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList != null) {
            ChatMessage message = messageList.get(position);
            if (message != null) {
                if (message.getMessageUser().equals(userName)) {
                    return MY_MESSAGE;
                } else {
                    return FRIEND_MESSAGE;
                }
            }
        }
        return 0;
    }
}
