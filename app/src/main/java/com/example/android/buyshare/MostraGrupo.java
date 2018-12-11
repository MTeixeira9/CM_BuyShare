package com.example.android.buyshare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MostraGrupo extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_grupo);

        String nomeGrupo = getIntent().getStringExtra("nomeG");
        String userLogado = getIntent().getStringExtra("userLog");

        getSupportActionBar().setTitle(nomeGrupo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ListView mListAmigos = findViewById(R.id.listAmigos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListAmigos.setAdapter(mAdapter);


        //mostrar os amigos para o logado escolher quem quer no grupo
        final DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("users");
        Query q = mDataBase.orderByChild("numeroTlm").equalTo(userLogado);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    User u = singleSnapShot.getValue(User.class);
                    Map<String, String> amigos = (Map<String, String>) u.getAmigos();

                    if (amigos != null) {
                        for (Map.Entry<String, String> amigo : amigos.entrySet()) {
                            mAdapter.add(amigo.getValue() + " " + amigo.getKey());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    public void onListItemClick(ListView parent, View v, int position, long id){
        CheckedTextView item = (CheckedTextView) v;

    }
}