package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.instaclone.Adapter.ChatAdapter;
import com.example.instaclone.Model.Comment;
import com.example.instaclone.Model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalChat extends AppCompatActivity {
    RecyclerView recyclerView;
    ChatAdapter adapter;
    List<Message> messageList;
    Toolbar bar;
    EditText message;
    Button send;
    FirebaseUser firebaseUser;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_chat);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.chat_recycle);
        recyclerView.setHasFixedSize(true);
        messageList=new ArrayList<>();
        adapter=new ChatAdapter(GlobalChat.this,messageList);
        layoutManager=new LinearLayoutManager(GlobalChat.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       layoutManager.setStackFromEnd(true);
       // layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        send=findViewById(R.id.chat_send);
        message=findViewById(R.id.chat_message);
        bar=findViewById(R.id.chat_tollbar);

        setSupportActionBar(bar);
        getSupportActionBar().setTitle("Global Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mssg=message.getText().toString();
                if(!mssg.isEmpty()){

                    sendMessage(mssg);
                    message.setText("");
                }
            }
        });
        getMessages();
    }

    private void getMessages() {
          FirebaseDatabase.getInstance().getReference().child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    messageList.add(message);
                }
                Log.i("messageList","1"+messageList.size());
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendMessage(String mssg){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Message");
        String mssgid=ref.push().getKey();
        HashMap<String,Object> map=new HashMap<>();
        map.put("id",mssgid);
        map.put("message",mssg);
        map.put("sender",firebaseUser.getUid());
        ref.child(mssgid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(GlobalChat.this,"message Send",Toast.LENGTH_SHORT).show();
                }
                else{

                    Toast.makeText(GlobalChat.this,"Not Sent"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}