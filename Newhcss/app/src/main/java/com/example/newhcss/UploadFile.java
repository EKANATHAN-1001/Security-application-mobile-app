package com.example.newhcss;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadFile extends AppCompatActivity {
    Button uf, uc,ucf;
    StorageReference sto;
    ProgressDialog pd;
    Uri img;
    DatabaseReference dr;
    ImageButton backb;
    String path;
    @Deprecated
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(UploadFile.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        setContentView(R.layout.activity_upload_file);
        uf = findViewById(R.id.upf);
        ucf=findViewById(R.id.choof);
        ucf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent f=new Intent(Intent.ACTION_GET_CONTENT);
                f.setType("*/*");
                startActivityForResult(f,100);
            }
        });
        backb=findViewById(R.id.bac);
        backb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadFile.this,Options.class));
            }
        });
        uf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(img==null)
                {
                    Toast.makeText(UploadFile.this, "Upload any image", Toast.LENGTH_LONG).show();
                }
                pd=new ProgressDialog(UploadFile.this);
                pd.setTitle("Uploading");
                pd.show();
                SimpleDateFormat formatter= new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.CANADA);
                Date now=new Date();
                //String fn=formatter.format(now);
                String fn=getName(img,getApplicationContext());
                String user;
                SharedPreferences pre=getSharedPreferences("CMS",MODE_PRIVATE);
                SharedPreferences.Editor edi=pre.edit();
                user =pre.getString("na","");
                path="/"+user+" "+fn;
                System.out.println("uri:"+img);
                sto = FirebaseStorage.getInstance().getReference(path);
                sto.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        System.out.println(user);
                        String pat=user+" "+fn;
                        Task<Uri> uri=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        Uri url=uri.getResult();
                        Filename fil=new Filename(pat,url.toString());
                        fileclass fi=new fileclass();
                        fi.add(fil);
                        Toast.makeText(UploadFile.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(pd.isShowing())
                        {
                            pd.dismiss();
                        }
                        Toast.makeText(UploadFile.this, "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double pg=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                        pd.setMessage("Uploaded:"+(int)pg+"% completed");
                    }
                });
            }
        });

    }
    @SuppressLint("Range")
    String getName(Uri uri, Context context)
    {
        String res=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = context.getContentResolver().query(uri,null,null,null,null);
            try {
                    if(cursor!=null && cursor.moveToFirst())
                    {
                        res=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
            }
            finally {
                cursor.close();
            }
            if(res==null)
            {
                res=uri.getPath();
                int cutt=res.lastIndexOf('/');
                if(cutt!=-1)
                {
                    res=res.substring(cutt+1);
                }
            }
        }
        return res;
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==100)
            {
                try{
                    img=data.getData();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}