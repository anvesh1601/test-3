package com.anveshreddy.transchat10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseusert;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseusert= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseusert!=null){
            Intent i=new Intent(this,Dashboard.class);
            startActivity(i);
            finish();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);

    }

    public void signup(View view) {
        Intent i=new Intent(this,SignupActivity.class);
        startActivity(i);
    }
}