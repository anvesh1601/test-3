package com.anveshreddy.transchat10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText send_email;
    Button btn_reset;
ImageView close;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        send_email = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.btn_reset);
close=findViewById(R.id.close1);
        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = send_email.getText().toString();

                if (email.equals("")){
                    Toast.makeText(ForgetPasswordActivity.this, "All fileds are required!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgetPasswordActivity.this, "Please check you Email", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ForgetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}