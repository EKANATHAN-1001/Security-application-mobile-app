package com.example.newhcss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgotpass extends AppCompatActivity {
    EditText et1;
    Button submit;
    String email;
    ProgressDialog pd;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        et1=findViewById(R.id.emal);
        submit=findViewById(R.id.submi);
        auth=FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=et1.getText().toString();
                pd=new ProgressDialog(Forgotpass.this);
                pd.setTitle("Sending");
                pd.show();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(pd.isShowing()) {
                                pd.dismiss();
                            }
                            Toast.makeText(Forgotpass.this, "Reset Link is sent", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Forgotpass.this,MainActivity.class));
                        }
                        else
                        {
                            if(pd.isShowing())
                            {
                                pd.dismiss();
                            }
                            Toast.makeText(Forgotpass.this, "Reset Link not sent due to error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}