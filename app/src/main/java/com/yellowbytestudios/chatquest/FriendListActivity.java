package com.yellowbytestudios.chatquest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yellowbytestudios.chatquest.friends.FriendListAdapter;
import com.yellowbytestudios.chatquest.friends.User;

import java.util.ArrayList;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private final int SIGN_IN_REQUEST_CODE = 101;
    private List<User> list;
    private FriendListAdapter friendAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        signInFirebaseUser();
        setupFriendListView();
    }

    private void setupFriendListView() {
        RecyclerView friendList = findViewById(R.id.friend_list);
        list = new ArrayList<>();
        friendAdapter = new FriendListAdapter(list, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        friendList.setLayoutManager(mLayoutManager);
        friendList.setAdapter(friendAdapter);
    }

    private void signInFirebaseUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {
            onSuccessfulSignIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                onSuccessfulSignIn();
            } else {
                finish();
            }
        }
    }

    private void onSuccessfulSignIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            showToast("Welcome " + user.getDisplayName());
            populateFriendList(user);
        }
    }

    private void populateFriendList(FirebaseUser user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.child(user.getUid()).setValue(new User(user.getUid(), user.getDisplayName(), getProfilePicUri(user)));
        ref.addChildEventListener(new MyChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addUserToList(dataSnapshot.getValue(User.class));
            }
        });
    }

    private void addUserToList(User user) {
        list.add(user);
        friendAdapter.notifyDataSetChanged();
    }

    private String getProfilePicUri(FirebaseUser user) {
        String profilePic = "";
        if (user.getPhotoUrl() != null) {
            profilePic = user.getPhotoUrl().toString();
        }
        return profilePic;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    showToast("You have been signed out.");
                    finish();
                }
            });
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
