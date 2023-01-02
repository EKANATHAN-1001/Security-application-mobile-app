package com.example.newhcss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Retrieve extends AppCompatActivity {
    ListView lv;
    StorageReference sto;
    DatabaseReference dr;
    ProgressDialog pd;
    List<Filename> filter;
    ImageView ig;
    int posi;
    String key;
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getItemId()==R.id.del)
        {
            int po=info.position;
            pd=new ProgressDialog(Retrieve.this);
            pd.setTitle("Deleting");
            pd.setMessage("Please Wait...");
            pd.show();
            String fult=filter.get(po).getFilename();
            System.out.println("Ret "+fult);
            dr=FirebaseDatabase.getInstance("https://newhcss-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
            sto=FirebaseStorage.getInstance().getReference(fult);
            sto.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    System.out.println("key "+key);
                    //dr.child(fult).removeValue();
                    dr.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot post: snapshot.getChildren())
                            {
                                Filename fn=(Filename) post.getChildren();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if(pd.isShowing()) {
                        pd.dismiss();
                    }
                    Toast.makeText(Retrieve.this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(pd.isShowing())
                    {
                        pd.dismiss();
                    }
                    Toast.makeText(Retrieve.this, "Failed to Delete", Toast.LENGTH_LONG).show();
                }
            });
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId()==R.id.listv){
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.option_menu,menu);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        lv=findViewById(R.id.listv);
        filter=new ArrayList<>();
        ig=findViewById(R.id.ig);
        ig.setVisibility(View.INVISIBLE);
        viewAll();
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Filename ful=filter.get(i);
                    Intent intent=new Intent();
                    intent.setType(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(ful.getUrl()));
                    startActivity(intent);
            }
        });

        }

    private void viewAll() {
        dr=FirebaseDatabase.getInstance("https://newhcss-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Filename");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userc = "";
                String user;
                SharedPreferences pre = getSharedPreferences("CMS", MODE_PRIVATE);
                SharedPreferences.Editor edi = pre.edit();
                user = pre.getString("na", "");
                for (DataSnapshot post : snapshot.getChildren()) {
                    userc="";
                    Filename fn = (Filename) post.getValue(Filename.class);
                    String str = fn.getFilename();
                    for (int j = 0; j < str.length(); j++) {
                        userc += str.charAt(j);
                        if (Character.compare(str.charAt(j), ' ') == 0) {
                            break;
                        }
                    }
                    System.out.println("userc" + userc + "\n");
                    int check = user.compareTo(userc);
                    System.out.println("Condition" + check);
                    if (check == -1) {
                        filter.add(fn);

                    }
                }
                System.out.println("user" + user + "\n");
                posi=filter.size();
                if(filter.size()==0)
                {
                    ig.setVisibility(View.VISIBLE);
                }
                String[] filen = new String[filter.size()];
                for (int i = 0; i < filter.size(); i++) {
                    System.out.println(filter.get(i).getFilename());
                }
                for (int i = 0; i < filen.length; i++) {
                    filen[i] = "";
                }
                for (int i = 0; i < filter.size(); i++) {
                    filen[i] = filter.get(i).getFilename();
                }
                    /*String str=filter.get(i).getFilename();
                    System.out.println("value "+i);
                    //System.out.println(str+"\n");
                    for(int j=0;j<str.length();j++)
                    {
                        userc+=str.charAt(j);
                        if(Character.compare(str.charAt(j),' ')==0)
                        {
                            break;
                        }
                    }
                    System.out.println("userc"+userc+"\n");
                    int check=user.compareTo(userc);
                    System.out.println("Condition"+check);
                    if (check==-1)
                    {
                        filen[k]=filter.get(i).getFilename();
                        System.out.println("array"+filen[k]);
                        k++;
                        userc="";
                    }
                }*/
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, filen) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getView(position, convertView, parent);
                            TextView mytext = (TextView) view.findViewById(android.R.id.text1);
                            mytext.setTextColor(Color.BLACK);
                            mytext.setTextSize(20);
                            return view;
                        }
                    };
                    lv.setAdapter(adapter);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}