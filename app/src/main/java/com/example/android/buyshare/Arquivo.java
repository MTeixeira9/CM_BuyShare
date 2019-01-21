package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Arquivo extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
    private ListView listaArq;
    private String userTlm;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    private static final String MSG_EMPTY_LISTS = "Ainda não tem nenhuma lista";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arquivo);

        getSupportActionBar().setTitle("Listas Arquivadas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ir buscar quem estah autenticado
        userTlm = getIntent().getStringExtra("userTlm");
        mDatabase = FirebaseDatabase.getInstance().getReference("listas");

        listaArq = findViewById(R.id.listasArquivadas);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaArq.setAdapter(mAdapter);

        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Lista l = singleSnapshot.getValue(Lista.class);
                    ArrayList<String> membrosLista = l.getMembrosLista();

                    if (membrosLista.contains(userTlm) && l.getQuemArquivou().contains(userTlm)) {

                        mAdapter.add(l.getNomeLista());
                        mAdapter.notifyDataSetChanged();
                    }
                }

                if (listaArq.getAdapter().getCount() == 0) {
                    mAdapter.add(MSG_EMPTY_LISTS);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mListener != null)
            mDatabase.removeEventListener(mListener);

        Intent i = new Intent(Arquivo.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }
}
