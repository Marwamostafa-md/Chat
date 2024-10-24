package com.example.chat.ChatPage;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.Message;
import com.example.chat.MessageAdapterActivity2;
import com.example.chat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatPageActivity extends AppCompatActivity {
    String receiverid, receivername, senderid, sendername, senderroom, receiverroom;
    DatabaseReference dbReferenceSender, dbReferenceReceiver, userReference;
    ImageView sendbtn;
    EditText message;
    Message messageModel;
    Toolbar toolbar2;
    RecyclerView recyclerView;
    MessageAdapterActivity2 messageAdapterActivity2;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_page);
        context = this;
        sendbtn = findViewById(R.id.sendbtn);
        message = findViewById(R.id.editMess);
        recyclerView = findViewById(R.id.recycler);
        messageAdapterActivity2 = new MessageAdapterActivity2(context);
        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        userReference = FirebaseDatabase.getInstance().getReference().child("users");
        receiverid = getIntent().getStringExtra("id");
        receivername = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(receivername);
        if (receiverid != null) {

            senderroom = FirebaseAuth.getInstance().getUid() + receiverid;
            receiverroom = receiverid + FirebaseAuth.getInstance().getUid();
        }
            dbReferenceSender = FirebaseDatabase.getInstance().getReference().child("Chats").child(senderroom);
            dbReferenceReceiver = FirebaseDatabase.getInstance().getReference().child("Chats").child(receiverroom);

            dbReferenceSender.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    List<Message> messageModelList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Message messageModel = dataSnapshot.getValue(Message.class);
                        messageModelList.add(messageModel);
                    }
                    messageAdapterActivity2.clear();
                    for (Message messageModel : messageModelList) {
                        messageAdapterActivity2.add(messageModel);
                    }
//                Log.d("werwerwerwerwer", String.valueOf(messageModelList.size()));
//                List<MessageModel> list = messageAdaptor.getmessageModelList();
                    messageAdapterActivity2.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            sendbtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    String msg = message.getText().toString();
                    if (!msg.isEmpty()) {
                        Log.d("sdfdsfsdfsdf",msg);
                        SendMessage(msg);
                    } else {
                        Toast.makeText(ChatPageActivity.this, "Something error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            recyclerView.setAdapter(messageAdapterActivity2);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }


    //private void setSupportActionBar(Toolbar toolbar) {
    //  }
    public void SendMessage(String Message) {
        String messagid = UUID.randomUUID().toString();
        long timestamp=System.currentTimeMillis();
        Message messageModel = new Message(messagid, FirebaseAuth.getInstance().getUid(), Message,timestamp);
        messageAdapterActivity2.add(messageModel);
        dbReferenceSender.child(messagid).setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        dbReferenceReceiver.child(messagid).setValue(messageModel);

        recyclerView.scrollToPosition(messageAdapterActivity2.getItemCount() - 1);
        message.setText("");

    }
}