package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MostraGrupo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_grupo);

        String nomeGrupo = getIntent().getStringExtra("nomeG");

        getSupportActionBar().setTitle(nomeGrupo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
