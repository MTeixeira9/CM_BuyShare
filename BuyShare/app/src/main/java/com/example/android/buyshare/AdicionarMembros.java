package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdicionarMembros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_membros);

        getSupportActionBar().setTitle("Adicionar Membros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Button newGrupo = (Button) findViewById(R.id.newGrupo);
        Button addAmigo = (Button) findViewById(R.id.addAm);

        newGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdicionarMembros.this, CriarGrupoAddM.class);
                startActivity(i);
            }
        });

        addAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdicionarMembros.this, NovoAmigoAddM.class);
                startActivity(i);
            }
        });
    }
}
