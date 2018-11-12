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

import java.util.ArrayList;

public class Amigos extends AppCompatActivity {

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
                //Intent i = new Intent(Amigos.this, NovoAmigo.class);
                //startActivityForResult(i, 1);

                //POPUP
                Intent i = new Intent(Amigos.this, PopUpAddAmigo.class);
                startActivityForResult(i, 1);
            }
        });

        mListAmigos = (ListView) findViewById(R.id.listAmigos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListAmigos.setAdapter(mAdapter);

        //Amigos iniciais
        mAdapter.add("João 967596499");
        mAdapter.add("Tomé 917733456");

        mAdapter.notifyDataSetChanged();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String nome = data.getStringExtra("nomeA");
                String tlmv = data.getStringExtra("nTlm");
                String novoAmigo = nome + " " + tlmv;
                mAdapter.add(novoAmigo);
                mAdapter.notifyDataSetChanged();

            }
        }
    }

}