package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.IntentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button BLogin;
    EditText LEmail;
    EditText LPass;
    TextView Reg;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Reg=findViewById(R.id.l_register);
        LEmail=findViewById(R.id.l_email);
        LPass=findViewById(R.id.l_password);
        BLogin=findViewById(R.id.l_breg);
        auth=FirebaseAuth.getInstance();
        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Register.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        BLogin.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email,Password;
                Email=LEmail.getText().toString();
                Password=LPass.getText().toString();
                if(Email.length()!=0&& Password.length()!=0){
                    login(Email, Password);
                }
                else{
                    if(Password.length()<=5){
                        Toast.makeText(LoginActivity.this,"Short Password or Email Format",Toast.LENGTH_SHORT).show();
                    }

            }
        }});
    }
        public void login(String a,String b){
             auth.signInWithEmailAndPassword(a,b).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         Toast.makeText(LoginActivity.this,"SuccesFull",Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(LoginActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                 Intent.FLAG_ACTIVITY_CLEAR_TASK));
                         finish();
                     }
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(LoginActivity.this,"Failed"+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                 }
             });
        }
}