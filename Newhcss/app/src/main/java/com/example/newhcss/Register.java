package com.example.newhcss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    Button sub;
    EditText email,pass,conpass;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.et1);
        pass=findViewById(R.id.et3);
        conpass=findViewById(R.id.et4);
        sub=findViewById(R.id.but);
        mauth= FirebaseAuth.getInstance();
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createuser();
            }
        });
    }
    private void createuser()
    {
        String ema=email.getText().toString();
        String pas=pass.getText().toString();
        mauth.createUserWithEmailAndPassword(ema,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser u=mauth.getCurrentUser();
                    u.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Register.this, "Verifying link is sent to your mail", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, ""+e, Toast.LENGTH_LONG).show();
                        }
                    });
                    startActivity(new Intent(Register.this,MainActivity.class));
                }
                else
                {
                    Toast.makeText(Register.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}