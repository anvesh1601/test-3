package com.anveshreddy.transchat10.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anveshreddy.transchat10.MessageActivity;
import com.anveshreddy.transchat10.Model.HiringRequest;
import com.anveshreddy.transchat10.Model.User;
import com.anveshreddy.transchat10.R;
import com.anveshreddy.transchat10.Receve;
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

public class HireAdapter extends RecyclerView.Adapter<HireAdapter.ViewHolder> {
    private Context mcontext;
    private List<HiringRequest> mUsers;
    private boolean isFragment;
    private FirebaseUser firebaseUser;
String Sender;
    public HireAdapter(Context mcontext,List<HiringRequest> mUsers, boolean isFragment) {
this.mcontext=mcontext;
        this.mUsers = mUsers;
        this.isFragment=isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mcontext).inflate(R.layout.hire_item, parent, false);
        return new HireAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final HiringRequest hr=mUsers.get(position);
        final String[] key = new String[1];
holder.UserName.setText(hr.getUserName());

    holder.btn_follow.setVisibility(View.VISIBLE);
    holder.btn_follow.setText(hr.getRequestStatus());

DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Hire").child(firebaseUser.getUid());
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            HiringRequest hra=snapshot.getValue(HiringRequest.class);
            if(hra.getSender().equals(hr.getSender())) {
                key[0] =  snapshot.getKey();

            }
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users").child(hr.getSender());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);

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

                Intent i=new Intent(mcontext, Receve.class);
                i.putExtra("userid", hr.getSender());
                mcontext.startActivity(i);
            }
        });
        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Hire").child(firebaseUser.getUid()).child(key[0]).child("RequestStatus").setValue("Rejected");
if(holder.btn_follow.getText().equals("Rejected")){
    FirebaseDatabase.getInstance().getReference("Hire").child(firebaseUser.getUid()).child(key[0]).child("RequestStatus").setValue("Accepted");
    FirebaseDatabase.getInstance().getReference("Hirestatus").child(hr.getSender()).child(firebaseUser.getUid()).child("Status").setValue("Accepted");
}else if(holder.btn_follow.getText().equals("Not Yet Seen")){
    Intent i=new Intent(mcontext, Receve.class);
    i.putExtra("userid", hr.getSender());
    mcontext.startActivity(i);
}
else{
    FirebaseDatabase.getInstance().getReference("Hire").child(firebaseUser.getUid()).child(key[0]).child("RequestStatus").setValue("Rejected");
    FirebaseDatabase.getInstance().getReference("Hirestatus").child(hr.getSender()).child(firebaseUser.getUid()).child("Status").setValue("Rejected");

}
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView UserName;
        CircleImageView CIV;
        Button btn_follow;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            UserName=itemView.findViewById(R.id.hirer);
            CIV=itemView.findViewById(R.id.hirer_profile);
           btn_follow=itemView.findViewById(R.id.btn_HireAccepted);
        }
    }
}
