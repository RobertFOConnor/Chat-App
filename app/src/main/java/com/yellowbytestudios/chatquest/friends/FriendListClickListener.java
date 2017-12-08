package com.yellowbytestudios.chatquest.friends;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yellowbytestudios.chatquest.ChatRoomActivity;
import com.yellowbytestudios.chatquest.R;

import static com.yellowbytestudios.chatquest.ChatRoomActivity.FRIEND_UID_KEY;


/**
 * Created by Robert on 08-Dec-17.
 */

public class FriendListClickListener implements View.OnClickListener {

    @Override
    public void onClick(final View view) {
        Context context = view.getContext();
        Intent chatIntent = new Intent(context, ChatRoomActivity.class);
        chatIntent.putExtra(FRIEND_UID_KEY, ((TextView) view.findViewById(R.id.uid)).getText());
        view.getContext().startActivity(chatIntent);
    }
}
