package com.yellowbytestudios.chatquest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yellowbytestudios.chatquest.chatmessage.ChatMessage;
import com.yellowbytestudios.chatquest.chatmessage.MessageAdapter;
import com.yellowbytestudios.chatquest.service.ChatMessageService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int SIGN_IN_REQUEST_CODE = 101;
    private DatabaseReference ref;
    private String displayName;
    private RecyclerView listOfMessages;
    private List<ChatMessage> list;
    private MessageAdapter messageAdapter;
    public static boolean active;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInFirebaseUser();
        setupSendClickListener();
        active = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    private void signInFirebaseUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {
            onSuccessfulSignIn();
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(displayName);
            actionBar.setSubtitle("Active 3 days ago");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupMessageListView() {
        listOfMessages = findViewById(R.id.list_of_messages);
        list = new ArrayList<>();
        messageAdapter = new MessageAdapter(list, displayName);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        listOfMessages.setLayoutManager(mLayoutManager);
        listOfMessages.setAdapter(messageAdapter);
    }

    private void setupSendClickListener() {
        ImageView sendButton = findViewById(R.id.send_message);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.input);
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                String message = input.getText().toString();
                if (message.length() > 0) {
                    ref.push().setValue(new ChatMessage(message, displayName));
                    input.setText(""); // Clear the input
                }
            }
        });
    }

    /*private void setupProfilePic(FirebaseUser user) {
        Uri profileUri = user.getPhotoUrl();
        if (profileUri != null) {
            Glide.with(this).load(profileUri).into((ImageView) findViewById(R.id.fab));
        }
    }*/

    private void onSuccessfulSignIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            displayName = user.getDisplayName();
        }
        setupMessageListView();
        setupActionBar();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("newRef");

        // User is already signed in. Therefore, display
        // a welcome Toast
        Toast.makeText(this, "Welcome " + displayName, Toast.LENGTH_LONG).show();

        ref.addChildEventListener(new MyChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                list.add(message);
                messageAdapter.notifyDataSetChanged();
                listOfMessages.scrollToPosition(list.size() - 1);
            }
        });
        getApplicationContext().startService(new Intent(this, ChatMessageService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                onSuccessfulSignIn();
            } else {
                showToast("We couldn't sign you in. Please try again later.");
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
        } else if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
