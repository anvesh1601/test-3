package com.anveshreddy.transchat10.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anveshreddy.transchat10.Adapters.MyFotosAdapter;
import com.anveshreddy.transchat10.EditProfile;
import com.anveshreddy.transchat10.FollowersActivity;
import com.anveshreddy.transchat10.MainActivity;
import com.anveshreddy.transchat10.Model.Post;
import com.anveshreddy.transchat10.Model.User;
import com.anveshreddy.transchat10.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class ProfileFragment extends Fragment {

    ImageView image_profile, options;
    TextView posts, followers, following, fullname, bio, username;
    Button edit_profile;
    DatabaseReference reference;
    FirebaseUser fuser;
String s1;
String profileid;
    private List<String> mySaves;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    TextView tv1,tv2,tv3;
    private RecyclerView recyclerView;
    private MyFotosAdapter myFotosAdapter;
    private List<Post> postList;

    private RecyclerView recyclerView_saves;
    private MyFotosAdapter myFotosAdapter_saves;
    private List<Post> postList_saves;

    ImageButton my_fotos, saved_fotos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");


        image_profile = view.findViewById(R.id.image_profile);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);

        edit_profile = view.findViewById(R.id.edit_profile);
        username = view.findViewById(R.id.username);
        my_fotos = view.findViewById(R.id.my_fotos);
        saved_fotos = view.findViewById(R.id.saved_fotos);
        options = view.findViewById(R.id.options);
options.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent i= new Intent(getContext(), MainActivity.class);
        startActivity(i);

    }
});

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
        myFotosAdapter = new MyFotosAdapter(getContext(), postList);
        recyclerView.setAdapter(myFotosAdapter);

        recyclerView_saves = view.findViewById(R.id.recycler_view_save);
        recyclerView_saves.setHasFixedSize(true);
        LinearLayoutManager mLayoutManagers = new GridLayoutManager(getContext(), 3);
        recyclerView_saves.setLayoutManager(mLayoutManagers);
        postList_saves = new ArrayList<>();
        myFotosAdapter_saves = new MyFotosAdapter(getContext(), postList_saves);
        recyclerView_saves.setAdapter(myFotosAdapter_saves);

        recyclerView.setVisibility(View.VISIBLE);
        recyclerView_saves.setVisibility(View.GONE);



        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                s1=user.getId();
                username.setText(user.getUsername());
                if (user.getImageURL().equals("Default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(ProfileFragment.this).load(user.getImageURL()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (profileid.equals(firebaseUser.getUid())){
            edit_profile.setText("Edit Profile");
        } else {
            checkFollow();
            saved_fotos.setVisibility(View.GONE);
        }

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btn = edit_profile.getText().toString();

                if (btn.equals("Edit Profile")){

                    startActivity(new Intent(getContext(), EditProfile.class));

                } else if (btn.equals("follow")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                } else if (btn.equals("following")){

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(profileid).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                            .child("followers").child(firebaseUser.getUid()).removeValue();

                }
            }
        });


        my_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView_saves.setVisibility(View.GONE);
            }
        });

        saved_fotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                recyclerView_saves.setVisibility(View.VISIBLE);
            }
        });


        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "followers");
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileid);
                intent.putExtra("title", "following");
                startActivity(intent);
            }
        });
        userInfo();
        ReadFollower();
        ReadFollowing();
        ReadNoOfPosts();
        myFotos();
        mySaves();
        return view;
    }
    private void userInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                User user = dataSnapshot.getValue(User.class);

                Glide.with(getContext()).load(user.getImageURL()).into(image_profile);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkFollow(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()){
                    edit_profile.setText("following");
                } else{
                    edit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ReadFollower(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Follow").child(profileid).child("followers");
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        followers.setText(snapshot.getChildrenCount()+"");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }
    private void ReadFollowing(){
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Follow").child(profileid).child("following");
reference1.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dsnapshot) {
        following.setText(dsnapshot.getChildrenCount()+"");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }
    private void ReadNoOfPosts(){
DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Posts");
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        int i=0;
        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
            Post post = snapshot.getValue(Post.class);
            if (post.getPublisher().equals(profileid)){
                i++;
            }
        }
        posts.setText(""+i);

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
    }
    private void myFotos(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getPublisher().equals(profileid)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                myFotosAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mySaves(){
        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mySaves.add(snapshot.getKey());
                }
                readSaves();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readSaves(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList_saves.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);

                    for (String id : mySaves) {
                        if (post.getPostid().equals(id)) {
                            postList_saves.add(post);
                        }
                    }
                }
                myFotosAdapter_saves.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}