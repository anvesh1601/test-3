package com.anveshreddy.transchat10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    EditText e1,e2,e3,e4;
    Button b1;
    TextView tv1;
    Spinner sp1;
    FirebaseAuth auth;
    int Translanguagecode;
    String s1,s2,s3,s4,s5;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        e1 = findViewById(R.id.username);
        e2 = findViewById(R.id.Email);
        e3 = findViewById(R.id.Password1);
        e4 = findViewById(R.id.Password2);
        b1 = findViewById(R.id.sibutton12);
        tv1=findViewById(R.id.textView5);

        auth = FirebaseAuth.getInstance();
        sp1 = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp1.setAdapter(arrayAdapter);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s4 = adapterView.getItemAtPosition(i).toString();

                switch (s4) {
                    case "English":
                        Translanguagecode = FirebaseTranslateLanguage.EN;

                        break;
                    case "Arabic":
                        Translanguagecode = FirebaseTranslateLanguage.AR;

                        break;
                    case "Urdu":
                        Translanguagecode = FirebaseTranslateLanguage.UR;

                        break;
                    case "Hindi":
                        Translanguagecode = FirebaseTranslateLanguage.HI;

                        break;
                    case "Telugu":
                        Translanguagecode = FirebaseTranslateLanguage.TE;

                        break;
                    case "Tamil":
                        Translanguagecode = FirebaseTranslateLanguage.TA;

                        break;
                    case "Bengali":
                        Translanguagecode = FirebaseTranslateLanguage.BN;

                        break;
                    case "Kannada":
                        Translanguagecode = FirebaseTranslateLanguage.KN;

                        break;
                    case "Marathi":
                        Translanguagecode = FirebaseTranslateLanguage.MR;

                        break;

                    default:
                        Translanguagecode = FirebaseTranslateLanguage.EN;
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Translanguagecode = 0;
                Toast.makeText(getApplicationContext(), "Please Select a language", Toast.LENGTH_LONG).show();

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(SignupActivity.this);
                pd.setMessage("Loading");
                pd.show();
                final String preferedlanguage=String.valueOf(Translanguagecode);
                final String s1=e1.getText().toString();
                final String s2=e2.getText().toString();
                String s3=e3.getText().toString();
                String s4=e4.getText().toString();
                translatecode(FirebaseTranslateLanguage.EN,Translanguagecode);
                if (TextUtils.isEmpty(s1)||TextUtils.isEmpty(s2)||TextUtils.isEmpty(s3)||TextUtils.isEmpty(s4)||Translanguagecode==0){
                    Toast.makeText(getApplicationContext(), "Every field is nessesary", Toast.LENGTH_LONG).show();

                }else if(s3.length()<8){
                    Toast.makeText(getApplicationContext(), "password should contain atleast 8 characters", Toast.LENGTH_LONG).show();

                }else {
                    auth.createUserWithEmailAndPassword(s2,s3).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser=auth.getCurrentUser();
                                String Userid=firebaseUser.getUid();
                                reference=FirebaseDatabase.getInstance().getReference("Users").child(Userid);
                                HashMap<String,String>hashMap= new HashMap<>();
                                hashMap.put("id",Userid);
                                hashMap.put("preferedlanguage",preferedlanguage);
                                hashMap.put("username",s1);
                                hashMap.put("Email",s2);
                                hashMap.put("ImageURL","Default");
                                hashMap.put("status", "offline");
                                hashMap.put("search", s1.toLowerCase());
                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if(task1.isSuccessful()){

                                            Intent i= new Intent(SignupActivity.this,Dashboard.class);
                                            startActivity(i);

                                        }else{
                                            task1.getException().printStackTrace();
                                            Toast.makeText(getApplicationContext(), "Can't register with this email", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                            }else{
                                task.getException().printStackTrace();
                                Toast.makeText(getApplicationContext(), "error occured", Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }


            }



        });

    }

    private void translatecode(int languagecode,int tralngcode) {
        tv1.setText("Downloading required language models...");
       final String ss="Download Successful..";
        FirebaseTranslatorOptions op =new FirebaseTranslatorOptions.Builder().setSourceLanguage(languagecode)
                .setTargetLanguage(tralngcode).build();
        final FirebaseTranslator translator= FirebaseNaturalLanguage.getInstance().getTranslator(op);
        FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder().requireWifi().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(ss).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        tv1.setText("Download Successful..");
                    }
                });
            }
        });
    }

}