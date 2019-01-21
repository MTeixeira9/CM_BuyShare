package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerMembros extends AppCompatActivity {

    private String userTlm, position, nameL, idL, tipoLista;
    private ListView listVerMembros;
    private ArrayAdapter<String> mAdapter;
    private DatabaseReference mDatabase;
    private ArrayList<String> membros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_membros);

        userTlm = getIntent().getStringExtra("userTlm");
        position = getIntent().getStringExtra("position");
        nameL = getIntent().getStringExtra("nameL");
        idL = getIntent().getStringExtra("idL");
        tipoLista = getIntent().getStringExtra("tipoL");

        Toast.makeText(getApplicationContext(), "idLista: " + idL, Toast.LENGTH_LONG).show();

        getSupportActionBar().setTitle(nameL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tv = findViewById(R.id.nomeLista);
        tv.setText("Membros da lista " + nameL);

        listVerMembros = findViewById(R.id.membros);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listVerMembros.setAdapter(mAdapter);

        membros = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("listas");

        mDatabase.child(idL).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista l = dataSnapshot.getValue(Lista.class);
                membros = l.getMembrosLista();

                if(membros != null){
                    for(String a : membros) {
                        mAdapter.add(a);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
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
        Intent i = new Intent(VerMembros.this, MostraLista.class);
        i.putExtra("userTlm", userTlm);
        i.putExtra("nameL", nameL);
        i.putExtra("position", position);
        i.putExtra("tipoL", tipoLista);

        startActivity(i);
    }
}
