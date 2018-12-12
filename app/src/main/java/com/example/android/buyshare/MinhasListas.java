package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MinhasListas extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> mAdapter2;
    private String userTlm, nomeLista, key;
    private ListView mListasPartilhadas;
    private ListView mListas;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_listas);

        getSupportActionBar().setTitle("Minhas Listas");

        Button novaLista = (Button) findViewById(R.id.novaLista);

        //ir buscar quem estah autenticado
        userTlm = getIntent().getStringExtra("userTlm");

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");

        mListasPartilhadas = (ListView) findViewById(R.id.listCategorias);
        mListas = (ListView) findViewById(R.id.listListasSemCat);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        mListasPartilhadas.setAdapter(mAdapter);
        mListas.setAdapter(mAdapter2);



        novaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MinhasListas.this, NovaLista.class);
                i.putExtra("userTlm", userTlm);
                startActivity(i);
            }
        });



        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mListas.getAdapter().getCount() == 0) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        Lista l = singleSnapshot.getValue(Lista.class);
                        ArrayList<String > meusGrupos = l.getMembrosGrupo();

                        if(meusGrupos.contains(userTlm)) {
                            List<String> listas = new ArrayList<>();
                            listas.add(l.getNomeLista());


                            mAdapter2.add(l.getNomeLista());
                            mAdapter2.notifyDataSetChanged();
                        }
                        // ELSE:
                        //Adicionar as listas partilhas
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mListas.setOnItemClickListener(this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.listas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.meusGrupos){
            Intent grupos = new Intent(MinhasListas.this, MeusGrupos.class);
            grupos.putExtra("userTlm", userTlm);
            startActivity(grupos);
        }else if(id == R.id.amigos){
            Intent amigos = new Intent(MinhasListas.this, Amigos.class);
            amigos.putExtra("userTlm", userTlm);
            startActivity(amigos);
        }else if(id == R.id.terminarS){
            Intent terminarS = new Intent(MinhasListas.this, LoginActivity.class);
            startActivity(terminarS);
            Toast.makeText(getApplicationContext(), "Sessão terminada com sucesso.", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.meuPerfil) {
            Intent meuPerfil = new Intent(MinhasListas.this, Perfil.class);
            meuPerfil.putExtra("userTlm", userTlm);
            startActivity(meuPerfil);
        }else if(id == R.id.arquivo) {
            Intent arquivo = new Intent(MinhasListas.this, Arquivo.class);
            startActivity(arquivo);
    }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, MostraLista.class);
        String name = (String) parent.getItemAtPosition(position);

        intent.putExtra("nameL", name);
        intent.putExtra("userTlm", userTlm);
        intent.putExtra("position", Integer.toString(position));

        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            if(resultCode == 11) {
                userTlm = data.getStringExtra("userTlm");
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MinhasListas.this, LoginActivity.class);
        startActivity(i);
    }
}