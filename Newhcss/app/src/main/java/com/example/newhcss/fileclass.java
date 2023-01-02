package com.example.newhcss;

import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fileclass {
        private final DatabaseReference dr;
        String filename;
        public  fileclass()
        {
            FirebaseDatabase db=FirebaseDatabase.getInstance("https://newhcss-default-rtdb.asia-southeast1.firebasedatabase.app");
            dr=db.getReference(Filename.class.getSimpleName());
            Intent i=new Intent();
            //Bundle extras=i.getExtras();
            //filename=extras.getString("val");
        }
        public void add(Filename fn){
            dr.push().setValue(fn);
        }
}
