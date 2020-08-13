package com.anveshreddy.transchat10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anveshreddy.transchat10.Adapters.MessageAdapter;
import com.anveshreddy.transchat10.Fragments.MessageFragment;
import com.anveshreddy.transchat10.Model.Chat;
import com.anveshreddy.transchat10.Model.User;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image;
    String s1,s3;
    TextView username,tv1;
    FirebaseUser fuser;
    DatabaseReference reference;
    int languagecode;
    int Translanguagecode;
    ImageButton btn_send;
    EditText text_send;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    String Translated_Text,msg,Rmsg;
    List<Chat> mchat;
    Fragment f= null;

    Chat chat;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = findViewById(R.id.Toolbar123);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f= new MessageFragment();

            }
        });
        tv1=findViewById(R.id.textView8);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        username=findViewById(R.id.username222);
        profile_image=findViewById(R.id.imagecircleview222);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.editText);

        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        final int i= FirebaseTranslateLanguage.EN;



        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                username.setText(user.getUsername());
                s3=user.getPreferedlanguage();
                int i=Integer.parseInt(s3);
                Translanguagecode=i;
                if (user.getImageURL().equals("Default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_image);
                }
                readMesagges(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {


            @Override

            public void onClick(View view) {
                msg = text_send.getText().toString();
                if (!msg.equals("")){

                   

                    sendMessage(fuser.getUid(), userid, msg, Translated_Text);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });


    }
    private void sendMessage(String sender, final String receiver, String message,String rmessage) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("smessage", message);



        reference.child("Chats").push().setValue(hashMap);
    }

    private void readMesagges(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mchat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl,Translanguagecode);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void identifylanguage(String ss) {

        FirebaseLanguageIdentification a= FirebaseNaturalLanguage.getInstance().getLanguageIdentification();


        a.identifyLanguage(ss).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if(s.equals("und")){
                    Toast.makeText(getApplicationContext(), "Language cannot be idetified", Toast.LENGTH_LONG).show();

                }else{
                    getLanguageCode(s);

                }

            }
        });
    }
    private void getLanguageCode(String s) {
        switch(s){
            case "en":
                languagecode= FirebaseTranslateLanguage.EN;

                break;
            case "ar":
                languagecode= FirebaseTranslateLanguage.AR;

                break;
            case "ur":
                languagecode=FirebaseTranslateLanguage.UR;

                break;
            case "hi":
                languagecode= FirebaseTranslateLanguage.HI;

                break;
            case "te":
                languagecode= FirebaseTranslateLanguage.TE;

                break;
            case "ta":
                languagecode= FirebaseTranslateLanguage.TA;

                break;
            case "bn":
                languagecode= FirebaseTranslateLanguage.BN;

                break;
            case "kn":
                languagecode= FirebaseTranslateLanguage.KN;

                break;
            case "mr":
                languagecode= FirebaseTranslateLanguage.MR;

                break;

            default:
                languagecode=0;
        }

        translatecode(languagecode,Translanguagecode);
    }
    private void translatecode(int languagecode,int tralngcode) {
        tv1.setText("translating...");
        FirebaseTranslatorOptions op =new FirebaseTranslatorOptions.Builder().setSourceLanguage(languagecode)
                .setTargetLanguage(tralngcode).build();
        final FirebaseTranslator translator=FirebaseNaturalLanguage.getInstance().getTranslator(op);
        FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder().requireWifi().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(s1).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        tv1.setText(s);
                        Translated_Text=s;
                    }
                });
            }
        });
    }

}