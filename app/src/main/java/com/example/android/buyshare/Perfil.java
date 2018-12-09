package com.example.android.buyshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

public class Perfil extends AppCompatActivity {


    private ArrayAdapter<String> mAdapter;
    private TextView nomeTV, pwdTV, nTlm_TV, email_TV;
    private ImageView image;
    private String userTlm;
    private DatabaseReference mDatabase, mDatabase2;
    private StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().setTitle("Meu Perfil");

        Button alteraDados = (Button) findViewById(R.id.alterarDados);

        userTlm = getIntent().getStringExtra("userTlm");

        nomeTV = findViewById(R.id.nome_perfil);
        pwdTV = findViewById(R.id.pwd_perfil);
        nTlm_TV = findViewById(R.id.nTlm_perfil);
        email_TV = findViewById(R.id.email_perfil);

        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mStorage = FirebaseStorage.getInstance().getReference("uploads");

        mDatabase2 = FirebaseDatabase.getInstance().getReference("upload");


        Query q = mDatabase.orderByChild("numeroTlm").equalTo(userTlm);

        Query q2 = mDatabase2.child(userTlm).child("imageUrl");

        String url = q2.toString();
        Log.d(url,"url");

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){

                    //Valores da base de dados
                    String nTelemovel = String.valueOf(singleSnapshot.child("numeroTlm").getValue());
                    String nome = String.valueOf(singleSnapshot.child("nome").getValue());
                    String pass = String.valueOf(singleSnapshot.child("password").getValue());
                    String email = String.valueOf(singleSnapshot.child("email").getValue());

                    nomeTV.setText(nome);
                    pwdTV.setText(pass);
                    nTlm_TV.setText(nTelemovel);
                    email_TV.setText(email);










                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        alteraDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Perfil.this, EditPerfil.class);
                i.putExtra("userTlm", userTlm);
                startActivity(i);
            }
        });
    }

    public static Bitmap getBitmap(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            is.close();
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
