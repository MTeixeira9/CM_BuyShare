package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MinhasListas extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> mAdapter2;
    private String userTlm;
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

        mDatabase = FirebaseDatabase.getInstance().getReference("Lista");

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
                startActivityForResult(i,2);
            }
        });

        mListas.setOnItemClickListener(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String nome = data.getStringExtra("nomeC");
                //String novaCat = nome;
                mAdapter.add(nome);
                mAdapter.notifyDataSetChanged();

            }
        }
        if (requestCode == 2){
            if (resultCode == RESULT_OK){
                String nome = data.getStringExtra("nomeL");
                //String novaLista = nome;
                mAdapter2.add(nome);
                mAdapter2.notifyDataSetChanged();

            }
        }
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
        startActivity(intent);
    }

}