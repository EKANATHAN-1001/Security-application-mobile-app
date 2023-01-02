package com.example.newhcss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button lo,re;
    FirebaseAuth mauth;
    EditText ema,pass;
    CheckBox show;
    TextView forgot;
    ProgressDialog pd;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lo=findViewById(R.id.logi);
        re=findViewById(R.id.reg);
        ema=findViewById(R.id.edtxt1);
        pass=findViewById(R.id.edtxt2);
        mauth=FirebaseAuth.getInstance();
        email=ema.getText().toString();
        show=findViewById(R.id.cb1);
        forgot=findViewById(R.id.forg);
        show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(show.isChecked()){
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Forgotpass.class));
            }
        });
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Register.class));
            }
        });
        lo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser();
            }
        });
    }
    private void loginuser()
    {
        email=ema.getText().toString().trim();
        SharedPreferences pre=getSharedPreferences("CMS",MODE_PRIVATE);
        SharedPreferences.Editor edi=pre.edit();
        edi.putString("na",email);
        edi.commit();
        String passw=pass.getText().toString().trim();

        if(email.isEmpty() && passw.isEmpty())
        {
            ema.setError("Enter your email");
            ema.requestFocus();
            pass.setError("Enter your password");
            pass.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            ema.setError("Enter your email");
            ema.requestFocus();
            return;
        }
        if(passw.isEmpty())
        {
            pass.setError("Enter your password");
            pass.requestFocus();
            return;
        }
        pd=new ProgressDialog(MainActivity.this);
        //Intent i = new Intent(MainActivity.this, fileclass.class);
        //i.putExtra("val",email);
            pd.setTitle("Logging in");
            pd.show();
            pd.setMessage("Please Wait...");
            mauth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, Options.class));
                        finish();
                    } else {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(MainActivity.this, "Error in login", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }