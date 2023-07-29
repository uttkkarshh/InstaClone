package com.example.instaclone.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.instaclone.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instaclone.Model.Comment;
import com.example.instaclone.Model.Message;
import com.example.instaclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContext;
    private List<Message> messages;

    private FirebaseUser fUser =FirebaseAuth.getInstance().getCurrentUser();

    public ChatAdapter(Context mContext, List<Message> messages) {
        this.mContext = mContext;
        this.messages = messages;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_item,parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      Message message=messages.get(position);
      holder.message.setText(message.getMessage());
      if(message.getSender().equals(fUser.getUid())){
          holder.grand.setGravity(Gravity.RIGHT);
      }
      else{
         holder.grand.setGravity(Gravity.LEFT);

        }

         FirebaseDatabase.getInstance().getReference().child("Users").child(message.getSender()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user.getImageurl().equals("default")){

                }
                else{
                    if(message.getSender().equals(fUser.getUid()))
                    {   Log.i("hello","sender");
                        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.image);
                    }
                    else{
                        Log.i("hello","reciever");
                        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageLeft);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout grand;
        LinearLayout parent;
        TextView message;
        CircleImageView image;
        CircleImageView imageLeft;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            grand=itemView.findViewById(R.id.message_grand_parent);
            parent=itemView.findViewById(R.id.message_parent);
            message=itemView.findViewById(R.id.message_message);
            image=itemView.findViewById(R.id.message_icon);
            imageLeft=itemView.findViewById(R.id.message_icon_recieved);
        }
    }
}
