package com.example.instaclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {
    Button Register,Login;
    ImageView icon;
    LinearLayout linear;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        icon=findViewById(R.id.insta_logo);
        linear=findViewById(R.id.linear_layout);
        Register=findViewById(R.id.register_button);
        Register.setBackgroundResource(R.drawable.button_background);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartActivity.this,Register.class);
                startActivity(i);
            }
        });
        Animation a=new TranslateAnimation(0.0f,0.0f,0.0f,-1000);
        a.setDuration(1000);
        a.setFillAfter(false);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            icon.clearAnimation();
            icon.setVisibility(View.INVISIBLE);
            linear.animate().alpha(1.0f).setDuration(1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        icon.setAnimation(a);
        Login=findViewById(R.id.login_button);
        Login.setBackgroundResource(R.drawable.button_background);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(i);

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        auth= FirebaseAuth.getInstance();
        FirebaseUser u=auth.getCurrentUser();
        if(u!=null)
        {
            startActivity(new Intent(StartActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }

    }
}