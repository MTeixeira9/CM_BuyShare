package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NovoAmigoAddM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_amigo);

        getSupportActionBar().setTitle("Novo Amigo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
