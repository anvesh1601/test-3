package com.anveshreddy.transchat10.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anveshreddy.transchat10.Adapters.Message_User_Adapter;
import com.anveshreddy.transchat10.Adapters.UserAdapter;
import com.anveshreddy.transchat10.Model.User;
import com.anveshreddy.transchat10.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends Fragment {
    private RecyclerView recyclerView;

    private Message_User_Adapter userAdapter;
    private List<User> mUsers;
    private List<String> followingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_message, container, false);

        recyclerView = view.findViewById(R.id.message_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();
        checkFollowing();
       return view;
    }
    private void checkFollowing(){
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }

                readUsers();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();



                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    for (String id : followingList){
                        if (user.getId().equals(id)) {
                            mUsers.add(user);
                        }
                    }

                }


                userAdapter = new Message_User_Adapter(getContext(), mUsers,true,true);
                try {
                    recyclerView.setAdapter(userAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}