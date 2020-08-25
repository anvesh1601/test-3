package com.anveshreddy.transchat10.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anveshreddy.transchat10.AddStoryActivity;
import com.anveshreddy.transchat10.Fragments.ProfileFragment;
import com.anveshreddy.transchat10.Model.Story;
import com.anveshreddy.transchat10.Model.User;
import com.anveshreddy.transchat10.R;
import com.anveshreddy.transchat10.StoryActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class StoryAdapter extends  RecyclerView.Adapter<StoryAdapter.ViewHolder>{

    private Context mContext;
    private List<Story> mStory;

    public StoryAdapter(Context mContext, List<Story> mStory) {
        this.mContext = mContext;
        this.mStory = mStory;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.add_story_item, parent, false);
            return new StoryAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.story_item, parent, false);
            return new StoryAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Story story=mStory.get(position);
        userInfo(holder,story.getUserid(),position);
        if(holder.getAdapterPosition()!=0){
            seenStory(holder,story.getUserid());
        }
        if(holder.getAdapterPosition()==0){
            FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            String Uid =firebaseUser.getUid();
DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(Uid);
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        User user=snapshot.getValue(User.class);

        if (user.getImageURL().equals("Default")){

            holder.story_image.setImageResource(R.mipmap.ic_launcher);
        } else {

            Glide.with(mContext).load(user.getImageURL()).into(holder.story_image);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});
            myStory(holder.add_text,holder.story_plus,false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.getAdapterPosition()==0){
                    myStory(holder.add_text,holder.story_plus,true);
                }else{
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    intent.putExtra("userid", story.getUserid());
                    mContext.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mStory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView story_image,story_image_seen,story_plus;
        public TextView story_username,add_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            story_image=itemView.findViewById(R.id.story_image);

            story_image_seen=itemView.findViewById(R.id.story_image_seen);
            story_plus=itemView.findViewById(R.id.story_plus);
            story_username=itemView.findViewById(R.id.story_username);
            add_text=itemView.findViewById(R.id.addstory_text);
        }
    }

    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }
        return 1;
    }
    private void userInfo(final ViewHolder viewHolder, String userid, final int pos){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageURL()).into(viewHolder.story_image);
                if (pos != 0) {
                    Glide.with(mContext).load(user.getImageURL()).into(viewHolder.story_image_seen);
                    viewHolder.story_username.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void myStory(final TextView textView, final ImageView imageView, final Boolean click){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userid=firebaseUser.getUid();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Story").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                int count=0;
                long timeCurrent=System.currentTimeMillis();
                for(DataSnapshot snapshot:datasnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);
                    Story story=snapshot.getValue(Story.class);
                    if(timeCurrent>story.getStoryStart()&&timeCurrent<story.getStoryEnd()){
                        count++;

                    }

                }
                if(click){
                    if (count > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "View Story",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, StoryActivity.class);
                                        intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        mContext.startActivity(intent);
                                        dialog.dismiss();

                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Add Story",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(mContext, AddStoryActivity.class);
                                        mContext.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();



                    } else {
                        Intent intent = new Intent(mContext, AddStoryActivity.class);
                        mContext.startActivity(intent);
                    }
                } else {
                    if(count>0){
                        textView.setText("My Story");

                        imageView.setVisibility(View.GONE);
                    }
                    else{
                        textView.setText("My Story");

                        imageView.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private  void  seenStory(final ViewHolder viewHolder, String userid){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Story").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(!snapshot.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()
                            &&System.currentTimeMillis()<snapshot.getValue(Story.class).getStoryEnd()){
                        i++;
                    }
                }
                if(i>0){
                    viewHolder.story_image.setVisibility(View.VISIBLE);
                    viewHolder.story_image_seen.setVisibility(View.GONE);
                }
                else{
                    viewHolder.story_image.setVisibility(View.GONE);
                    viewHolder.story_image_seen.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
