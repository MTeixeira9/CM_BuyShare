package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private String userLogado;
    private String nomeGrupo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_grupo);

        nomeGrupo = getIntent().getStringExtra("nomeG");
        userLogado = getIntent().getStringExtra("userLog");

        getSupportActionBar().setTitle(nomeGrupo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addMembros = (Button) findViewById(R.id.addMembro);

        addMembros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MostraGrupo.this, AdicionarMembros.class);
                i.putExtra("userTlm", userLogado);
                startActivityForResult(i, 1);
            }
        });


/*
        ListView mListAmigos = findViewById(R.id.listAmigos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListAmigos.setAdapter(mAdapter);
*/

    }

}