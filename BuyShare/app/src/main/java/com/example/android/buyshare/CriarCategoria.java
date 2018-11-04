package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CriarCategoria extends AppCompatActivity {

    private static final String msg = "Tem de dar um nome à categoria!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_categoria);

        getSupportActionBar().setTitle("Nova Categoria");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addCategoria = (Button) findViewById(R.id.criarCat);
        addCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nomeC = (TextView) findViewById(R.id.nomeCat);
                Intent i = new Intent();
                String nome = nomeC.getText().toString();

                if (!nome.equals("")){
                    i.putExtra("nomeC", nome);
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
