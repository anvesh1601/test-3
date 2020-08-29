package com.anveshreddy.transchat10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anveshreddy.transchat10.Model.HiringRequest;
import com.anveshreddy.transchat10.Model.User;
import com.bumptech.glide.Glide;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

import static com.google.android.material.internal.ContextUtils.*;

public class Receve extends AppCompatActivity {
MaterialTextView username,mail,ContactDetails,Amount;
CircleImageView image_profile;
Button HireAccept,HireRejected;
    FirebaseUser fuser;
    String key;
    DatabaseReference reference;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receve);
        Toolbar toolbar = findViewById(R.id.Toolbar123);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ProposalRequest");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        username=findViewById(R.id.username);
        mail=findViewById(R.id.mail);
        ContactDetails=findViewById(R.id.ContactDetails);
        Amount=findViewById(R.id.amount);
        image_profile=findViewById(R.id.image_profile);
        HireAccept=findViewById(R.id.HireAccept);
        HireRejected=findViewById(R.id.Reject);
        intent = getIntent();
        final String user = intent.getStringExtra("userid");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        final String id=fuser.getUid();
        DatabaseReference r= FirebaseDatabase.getInstance().getReference("Users").child(user);
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContentResolver() == null) {
                    return;
                }
                User user=snapshot.getValue(User.class);

                if (user.getImageURL().equals("Default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference r1=FirebaseDatabase.getInstance().getReference("Hire").child(fuser.getUid());
        r1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                HiringRequest hr=snapshot.getValue(HiringRequest.class);
if(hr.getSender().equals(user)) {
  key=  snapshot.getKey();
    username.setText(hr.getUserName());
    mail.setText(hr.getEmail());
    Amount.setText(hr.getAmount());
    ContactDetails.setText(hr.getAdditionalBenfits());
}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        HireAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

FirebaseDatabase.getInstance().getReference("Hire").child(fuser.getUid()).child(key).child("RequestStatus").setValue("Accepted");
FirebaseDatabase.getInstance().getReference("Hirestatus").child(user).child(fuser.getUid()).child("Status").setValue("Accepted");
finish();
            }
        });
        HireRejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("Hire").child(fuser.getUid()).child(key).child("RequestStatus").setValue("Rejected");
                FirebaseDatabase.getInstance().getReference("Hirestatus").child(user).child(fuser.getUid()).child("Status").setValue("Rejected");
                finish();
            }
        });

    }
    private void Accept(final String id,final String userid){

    }
    private void Reject(final String id,final String userid){

    }
}