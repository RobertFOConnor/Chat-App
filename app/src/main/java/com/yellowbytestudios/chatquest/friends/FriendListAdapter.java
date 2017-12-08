package com.yellowbytestudios.chatquest.friends;

/**
 * Created by Robert on 07-Dec-17.
 * <p>
 * Adapter for friends list.
 */

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yellowbytestudios.chatquest.R;

import java.util.List;

/**
 * Created by Robert on 20-Nov-17.
 * <p>
 * RecyclerView adapter for message display.
 */

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

    private final View.OnClickListener mOnClickListener = new FriendListClickListener();
    private Context context;
    private List<User> friendList;

    public FriendListAdapter(List<User> list, Context context) {
        this.context = context;
        friendList = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView name;
        TextView uid;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            uid = itemView.findViewById(R.id.uid);
            profilePic = itemView.findViewById(R.id.profile_pic);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new FriendListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = friendList.get(position);
        holder.name.setText(user.getName());
        holder.uid.setText(user.getUid());
        Glide.with(context).load(Uri.parse(user.getProfilePicture())).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
