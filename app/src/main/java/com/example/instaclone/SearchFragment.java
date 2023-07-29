package com.example.instaclone;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instaclone.Adapter.TagAdapter;
import com.example.instaclone.Adapter.UserAdapter;
import com.example.instaclone.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> mUsers;
    private UserAdapter userAdapter;
    private EditText search_bar;

    private RecyclerView recyclerViewTags;
    private List<String> mHashTags;
    private List<String> mHashTagsCount;
    private TagAdapter tagAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_search, container, false);
        recyclerViewTags = view.findViewById(R.id.s_taglist);
        recyclerViewTags.setHasFixedSize(true);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));

        mHashTags = new ArrayList<>();
        mHashTagsCount = new ArrayList<>();
        tagAdapter = new TagAdapter(getContext() , mHashTags , mHashTagsCount);
        recyclerViewTags.setAdapter(tagAdapter);



        recyclerViewTags.setAdapter(tagAdapter);
        recyclerView=view.findViewById(R.id.s_userlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        search_bar=view.findViewById(R.id.s_search);
        mUsers=new ArrayList<>();
        userAdapter=new UserAdapter(getContext(),mUsers,true);
        recyclerView.setAdapter(userAdapter);
        readUsers();
        readTags();
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return view;
    }

    private void searchUser(String S) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").startAt(S).endAt(S + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void readTags() {

        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mHashTags.clear();
                mHashTagsCount.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mHashTags.add(snapshot.getKey());
                    mHashTagsCount.add(snapshot.getChildrenCount() + "");
                }

                tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
        public void readUsers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                for (DataSnapshot datasnapshot : snapshot.getChildren()){
                    User user = datasnapshot.getValue(User.class);
                    Log.i("recycler123", "readUsers"+user.toString());
                    mUsers.add(user);
                }
                Log.i("recycler123", "readUsers"+mUsers.size());
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void filter (String text) {
        List<String> mSearchTags = new ArrayList<>();
        List<String> mSearchTagsCount = new ArrayList<>();

        for (String s : mHashTags) {
            if (s.toLowerCase().contains(text.toLowerCase())){
                mSearchTags.add(s);
                mSearchTagsCount.add(mHashTagsCount.get(mHashTags.indexOf(s)));
            }
        }

        tagAdapter.filter(mSearchTags , mSearchTagsCount);
    }
}