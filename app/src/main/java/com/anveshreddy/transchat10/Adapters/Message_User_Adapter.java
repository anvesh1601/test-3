package com.anveshreddy.transchat10.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anveshreddy.transchat10.MessageActivity;
import com.anveshreddy.transchat10.Model.Chat;
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

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Message_User_Adapter extends RecyclerView.Adapter<Message_User_Adapter.viewHolder>{
    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;
private  boolean isChat;
String theLastMessage;
    private FirebaseUser firebaseUser;
    public Message_User_Adapter(Context Context, List<User> users, boolean isFragment,boolean isChat){
        mContext = Context;
        mUsers = users;
        this.isFragment = isFragment;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_list, parent, false);
        return new Message_User_Adapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("Default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }
        if (isChat){
            lastMessage(user.getId(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }
        if (isChat){
            if (user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mContext, MessageActivity.class);
                i.putExtra("userid",user.getId());
                mContext.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return  mUsers.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username1111);
            profile_image=itemView.findViewById(R.id.profile_image1111);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }


    }
    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        final String myid=fuser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                            theLastMessage = chat.getSmessage();
                        }
                    }
                }
                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);
                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
