package com.anveshreddy.transchat10.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anveshreddy.transchat10.MessageActivity;
import com.anveshreddy.transchat10.Model.User;
import com.anveshreddy.transchat10.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Message_User_Adapter extends RecyclerView.Adapter<Message_User_Adapter.viewHolder>{
    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;

    private FirebaseUser firebaseUser;
    public Message_User_Adapter(Context Context, List<User> users, boolean isFragment){
        mContext = Context;
        mUsers = users;
        this.isFragment = isFragment;
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
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.username1111);
            profile_image=itemView.findViewById(R.id.profile_image1111);
        }


    }

}
