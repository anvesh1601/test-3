package com.anveshreddy.transchat10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anveshreddy.transchat10.Model.HiringRequest;
import com.anveshreddy.transchat10.Model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class sendHiringRequestActivity extends AppCompatActivity {
    MaterialEditText Email, Username,AdditionalBenifits;
    TextView Amount;
    Button Hire;
    CircleImageView civ;
    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_hiring_request);
        Username=findViewById(R.id.username);
        Email=findViewById(R.id.mail);
        AdditionalBenifits=findViewById(R.id.ContactDetails);
        Amount=findViewById(R.id.Amount);
        civ=findViewById(R.id.image_profile);
        Hire=findViewById(R.id.HireRequest);
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        String id=fuser.getUid();
        DatabaseReference r= FirebaseDatabase.getInstance().getReference("Users").child(id);
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                Username.setText(user.getUsername());
                Email.setText(user.getEmail());
                if (user.getImageURL().equals("Default")){
                    civ.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(sendHiringRequestActivity.this).load(user.getImageURL()).into(civ);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
      final  String Myid=fuser.getUid();
            Hire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   String hire =Hire.getText().toString().toLowerCase();

                        String s1=    Username.getText().toString();
                        String s2=     Email.getText().toString();
                        String s3=   AdditionalBenifits.getText().toString();
                        String s4=     Amount.getText().toString();
                        if(TextUtils.isEmpty(s1)||TextUtils.isEmpty(s2)||TextUtils.isEmpty(s4)){
                            Toast.makeText(getApplicationContext(), "Every field is nessesary", Toast.LENGTH_LONG).show();
                        }else{
                            SendHireRequest(Myid,userid,s1,s2,s3,s4);
                        }



                }
            });

    }

    private  void SendHireRequest(String Sender,final String Reciver,String uname,String Email,String Addben,String Amount){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hire").child(Reciver);

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", Sender);
        hashMap.put("receiver", Reciver);
        hashMap.put("UserName", uname);
        hashMap.put("Email", Email);
        hashMap.put("Amount", Amount);
        hashMap.put("AdditionalBenfits", Addben);
        hashMap.put("RequestStatus","Not Yet Seen");


        reference.push().setValue(hashMap);
        RequestStatus(Sender,Reciver);

    }
private void RequestStatus(String sender,String receiver){
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Hirestatus").child(sender);
    HashMap<String,Object> hashmap=new HashMap<>();
    hashmap.put("receiver", receiver);
    hashmap.put("Status","Not Yet Seen");
    reference1.child(receiver).setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            finish();
        }
    });

}

}