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
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
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
    private ListView mListCategorias;
    private ListView mListas;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_listas);

        getSupportActionBar().setTitle("Minhas Listas");

        Button novaCat = (Button) findViewById(R.id.novaCategoria);
        Button novaLista = (Button) findViewById(R.id.novaLista);

        //ir buscar quem estah autenticado
        userTlm = getIntent().getStringExtra("userTlm");

        mDatabase = FirebaseDatabase.getInstance().getReference("lista");

        mListCategorias = (ListView) findViewById(R.id.listCategorias);
        mListas = (ListView) findViewById(R.id.listListasSemCat);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        mListCategorias.setAdapter(mAdapter);
        mListas.setAdapter(mAdapter2);



        novaCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MinhasListas.this, PopUpCategoria.class);
                startActivityForResult(i, 1);
            }
        });

        novaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MinhasListas.this, NovaLista.class);
                i.putExtra("userTlm", userTlm);
                startActivity(i);
            }
        });



       // key = getIntent().getStringExtra("key");

        Toast.makeText(getApplicationContext(), "USER TLM: " + userTlm, Toast.LENGTH_LONG).show();
        Log.d(userTlm, "userTlm");
        Query q = mDatabase.orderByChild(userTlm);


        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapShot : dataSnapshot.getChildren()){


                    Toast.makeText(getApplicationContext(), "Dentro for", Toast.LENGTH_LONG).show();

                    Lista l = singleSnapShot.getValue(Lista.class);

                    List<String> listas = new ArrayList<>();
                    listas.add(l.getNomeLista());


                    mAdapter2.add(l.getNomeLista());
                    mAdapter2.notifyDataSetChanged();
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
            //amigos.set
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
        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            if(resultCode == 11) {
                String myStr=data.getStringExtra("MyData");
                //mTextView.setText(myStr);
            }
        }
    }

}