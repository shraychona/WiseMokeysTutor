package com.shray.wisemonkeystutor.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shray.wisemonkeystutor.BaseActivity;
import com.shray.wisemonkeystutor.R;
import com.shray.wisemonkeystutor.adapter.ChatAdapter;
import com.shray.wisemonkeystutor.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends BaseActivity {

    String chatName, tutorName, studentName;
    DatabaseReference mReference;
    RecyclerView mRecyclerView;
    ChatAdapter mChatAdapter;
    EditText messageArea;
    ImageView sendButton;
    List<Chat> messageList = new ArrayList<>();
    private Toolbar chatToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mRecyclerView = (RecyclerView) findViewById(R.id.chatList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageArea = (EditText) findViewById(R.id.messageArea);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        chatToolbar= (Toolbar) findViewById(R.id.chatToolbar);
        setSupportActionBar(chatToolbar);

        Intent getIntent = getIntent();
        chatName = getIntent.getStringExtra("chatref");
        tutorName = getIntent.getStringExtra("tutorName");
        studentName = getIntent.getStringExtra("studentName");

        mReference = FirebaseDatabase.getInstance().getReference().child("messages").child(chatName);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageArea.getText().toString().trim();
                if (!message.equals("")) {
                    String key = mReference.push().getKey();
                    mReference.child(key).child("Name").setValue(tutorName);
                    mReference.child(key).child("Message").setValue(message);
                }
            }
        });
        messageList.clear();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                try {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        messageList.add(new Chat(childSnapshot.child("Name").getValue().toString(),
                                childSnapshot.child("Message").getValue().toString()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    messageList.clear();
                }
                messageArea.setText("");
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                mChatAdapter = new ChatAdapter(ChatActivity.this, messageList);
                mRecyclerView.setAdapter(mChatAdapter);
                mRecyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater=getMenuInflater();
        mMenuInflater.inflate(R.menu.chat_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menuOffers) {
            startActivity(new Intent(ChatActivity.this, OffersActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
