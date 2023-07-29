package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    EditText Emai,Passwor,UserName,Name;
    Button Breg;
    TextView logi;
    FirebaseAuth auth;
    DatabaseReference ref;
    ProgressDialog pd;
    AlertDialog dialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        pd= new ProgressDialog(this);
        Emai=findViewById(R.id.r_email);
        Passwor=findViewById(R.id.r_password);
        Breg=findViewById(R.id.r_breg);
        logi=findViewById(R.id.r_login);
        auth= FirebaseAuth.getInstance();
        UserName=findViewById(R.id.r_username);
        Name=findViewById(R.id.r_name);
        ref=FirebaseDatabase.getInstance().getReference();
        logi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,LoginActivity.class));

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);

        Breg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Email=Emai.getText().toString();
                String Password=Passwor.getText().toString();
                String userName=UserName.getText().toString();
                String name=Name.getText().toString();
                dialog = builder.create();
                dialog.show();
                if(Email.isEmpty() || Password.isEmpty() || userName.isEmpty() || name.isEmpty()){
                    Toast.makeText(Register.this,"Empty Credentials ",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Password.length()<=5){
                        Toast.makeText(Register.this,"Short Password ",Toast.LENGTH_SHORT).show();
                    }else{
                    register(Email, Password,userName,name);}
                }
            }
        });
    }

    public void register(String a,String b ,String x,String y){
        Task<AuthResult> authResultTask = auth.createUserWithEmailAndPassword(a, b).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Map<String,String> m=new HashMap<>();
                m.put("name",y);
                m.put("username",x);
                m.put("email",a);
                m.put("id",auth.getCurrentUser().getUid());
                m.put("bio","");
                m.put("imageurl","default");
                ref.child("Users").child(auth.getCurrentUser().getUid()).setValue(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Register.this,"SuccessFull",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Register.this,MainActivity.class));

                        }
                        else{
                            Toast.makeText(Register.this,""+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}