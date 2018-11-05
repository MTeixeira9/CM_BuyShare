package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Arquivo extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
    private ListView listaArq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arquivo);

        getSupportActionBar().setTitle("Listas Arquivadas");

        listaArq = (ListView) findViewById(R.id.listasArquivadas);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaArq.setAdapter(mAdapter);

        //Listas iniciais
        mAdapter.add("Jantar de Aniversário");
        mAdapter.add("Jantar da Faculdade");
        mAdapter.add("Supermercado");
        mAdapter.notifyDataSetChanged();

    }
}
