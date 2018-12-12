package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class AdicionarMembros extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
    private String userLogado;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_membros);

        getSupportActionBar().setTitle("Adicionar Membros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userLogado = getIntent().getStringExtra("userTlm");
        linearLayout = findViewById(R.id.linearL);




        ListView mListAmigos = findViewById(R.id.listAmigos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        //mListAmigos.setAdapter(mAdapter);


        //adicionar logo os amigos da base de dados
        final DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("users");

        Query q = mDataBase.orderByChild("numeroTlm").equalTo(userLogado);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    User u = singleSnapShot.getValue(User.class);
                    Map<String, String> amigos = (Map<String, String>)  u.getAmigos();

                    if(amigos != null) {
                        for (Map.Entry<String, String> amigo : amigos.entrySet()) {
                            CheckBox cb = new CheckBox(getApplicationContext());
                            cb.setText(amigo.getValue());
                            linearLayout.addView(cb);
                            //mAdapter.add(amigo.getValue() + " " + amigo.getKey());
                            //mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });


    }
}
