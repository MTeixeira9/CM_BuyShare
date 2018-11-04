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

public class CriarGrupo extends AppCompatActivity {

    private static final String msg = "Tem de preencher ambos os campos!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_grupo);

        getSupportActionBar().setTitle("Criar Grupo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addGrupo = (Button) findViewById(R.id.criarGrupo);
        addGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nomeGrupoo = (TextView) findViewById(R.id.nomeGrupo);
                Intent i = new Intent();
                String nome = nomeGrupoo.getText().toString();

                if (!nome.equals("")){
                    i.putExtra("nomeG", nome);
                    setResult(RESULT_OK, i);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
