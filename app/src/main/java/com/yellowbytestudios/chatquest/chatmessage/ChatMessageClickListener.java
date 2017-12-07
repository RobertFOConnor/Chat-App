package com.yellowbytestudios.chatquest.chatmessage;

import android.view.View;
import android.widget.TextView;

import com.yellowbytestudios.chatquest.R;

/**
 * Created by Robert on 07-Dec-17.
 * <p>
 * Chat message click listener.
 * Shows time of message send on click.
 */

class ChatMessageClickListener implements View.OnClickListener {

    @Override
    public void onClick(final View view) {
        TextView messageTime = view.findViewById(R.id.message_time);
        if (messageTime.getVisibility() == View.VISIBLE) {
            view.findViewById(R.id.message_time).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.message_time).setVisibility(View.VISIBLE);
        }
    }
}
