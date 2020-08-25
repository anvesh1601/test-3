package com.anveshreddy.transchat10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText e1,e2;
    Button b1;
    FirebaseAuth auth;
    TextView tv,tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);e1=findViewById(R.id.EmailAddress);
        e2=findViewById(R.id.Password3);
        b1=findViewById(R.id.button2);
        tv = findViewById(R.id.textViee);
        tv1=findViewById(R.id.textView9);
        auth= FirebaseAuth.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=e1.getText().toString();
                String password=e2.getText().toString();
                if(TextUtils.isEmpty(email)||TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(), "Every field is required", Toast.LENGTH_LONG).show();

                }else{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent i= new Intent(LoginActivity.this,Dashboard.class);
                                startActivity(i);

                            }else{
                                Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
Intent in = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
startActivity(in);
            }
        });


    }

    public void sigh(View view) {
        Intent i=new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(i);
    }
}