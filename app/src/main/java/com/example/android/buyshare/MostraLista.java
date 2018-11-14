package com.example.android.buyshare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MostraLista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_lista);

        String nomeLista = getIntent().getStringExtra("nameL");

        getSupportActionBar().setTitle("Lista: " + nomeLista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button guardar = (Button) findViewById(R.id.finComprar);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostraLista.this, MinhasListas.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mostralista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.addMembros){
            Intent addMembros = new Intent(MostraLista.this, AdicionarMembrosMostraLista.class);
            startActivity(addMembros);

        }else if(id == R.id.verMembros){
            Intent amigos = new Intent(MostraLista.this, VerMembros.class);
            startActivity(amigos);

        }else if(id == R.id.estimarCusto){

            Intent amigos = new Intent(MostraLista.this, EstimarCustoLista.class);
            startActivity(amigos);

        }else if(id == R.id.finalizar) {
            Intent intent = new Intent(MostraLista.this, AdicionarCustoL.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
