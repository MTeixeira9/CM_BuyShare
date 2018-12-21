package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CriarGrupoAddM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_criar_grupo);

        getSupportActionBar().setTitle("Criar Grupo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
