package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Amigos extends AppCompatActivity {

    private static final String MSG_ERRO1 = "O utilizador já se encontra na sua lista de amigos!";
    private static final String MSG_ERRO2 = "Não pode ser amigo de si próprio!";

    private ArrayAdapter<String> mAdapter;
    private ListView mListAmigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        getSupportActionBar().setTitle("Meus Amigos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addAmigos = (Button) findViewById(R.id.addAmigos);

        addAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //POPUP
                Intent i = new Intent(Amigos.this, PopUpAddAmigo.class);
                String userLogado = getIntent().getStringExtra("userTlm");
                i.putExtra("userTlm", userLogado);
                startActivityForResult(i, 1);
            }
        });

        mListAmigos = (ListView) findViewById(R.id.listAmigos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListAmigos.setAdapter(mAdapter);

        //Amigos da pessoa
        //TODO

        mAdapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                Toast.makeText(getApplicationContext(), MSG_ERRO1, Toast.LENGTH_LONG).show();
            }
            else if (resultCode == -2) {
                Toast.makeText(getApplicationContext(), MSG_ERRO2, Toast.LENGTH_LONG).show();
            }
            else if (requestCode == 1) {
                String nome = data.getStringExtra("nomeA");
                String tlmv = data.getStringExtra("nTlm");
                String novoAmigo = nome + " " + tlmv;
                mAdapter.add(novoAmigo);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}