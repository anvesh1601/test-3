package com.anveshreddy.transchat10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    Animation am;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv=findViewById(R.id.backimg);
        am= AnimationUtils.loadAnimation(this,R.anim.bottom);
        iv.setAnimation(am);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(getApplicationContext(), ChooseActivity.class);
                startActivity(i);
            }
        },5000);
    }

}