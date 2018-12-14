package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Arquivo extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
    private ListView listaArq;
    private String userTlm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arquivo);

        getSupportActionBar().setTitle("Listas Arquivadas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ir buscar quem estah autenticado
        userTlm = getIntent().getStringExtra("userTlm");

        listaArq = findViewById(R.id.listasArquivadas);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaArq.setAdapter(mAdapter);

        //Listas iniciais
        mAdapter.add("Jantar de Aniversário");
        mAdapter.add("Jantar da Faculdade");
        mAdapter.add("Supermercado");
        mAdapter.notifyDataSetChanged();

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
        Intent i = new Intent(Arquivo.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }

}
