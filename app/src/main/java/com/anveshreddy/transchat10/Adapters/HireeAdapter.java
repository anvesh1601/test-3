package com.anveshreddy.transchat10.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anveshreddy.transchat10.MessageActivity;
import com.anveshreddy.transchat10.Model.HireStatus;
import com.anveshreddy.transchat10.Model.HiringRequest;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class HireeAdapter  extends RecyclerView.Adapter<HireeAdapter.ViewHolder> {

    private Context mcontext;
    private List<HireStatus> mUsers;
    private boolean isFragment;
    private FirebaseUser firebaseUser;
    String Sender;
    public HireeAdapter(Context mcontext,List<HireStatus> mUsers, boolean isFragment) {
        this.mcontext=mcontext;
        this.mUsers = mUsers;
        this.isFragment=isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.hiree_item, parent, false);
        return new HireeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final HireStatus hr=mUsers.get(position);
        holder.HireStatus.setText(hr.getStatus());
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users").child(hr.getReceiver());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
holder.UserName.setText(user.getUsername());
                if(user.getImageURL()!=null) {
                    if (user.getImageURL().equals("Default")) {
                        holder.CIV.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(mcontext).load(user.getImageURL()).into(holder.CIV);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(mcontext, MessageActivity.class);
                i.putExtra("userid",hr.getReceiver());
                mcontext.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView UserName,HireStatus;
        CircleImageView CIV;
        Button btn_follow;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            UserName=itemView.findViewById(R.id.hirer);
            CIV=itemView.findViewById(R.id.hirer_profile);
            btn_follow=itemView.findViewById(R.id.btn_HireAccepted);
            HireStatus=itemView.findViewById(R.id.hireStatus);
        }
    }
}
