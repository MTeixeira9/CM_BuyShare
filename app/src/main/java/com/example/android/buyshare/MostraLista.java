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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MostraLista extends AppCompatActivity {

    private String userTlm, nomeLista;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_lista);

        nomeLista = getIntent().getStringExtra("nameL");
        userTlm = getIntent().getStringExtra("userTlm");

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        Query q = mDatabase.orderByChild("numeroTlm").equalTo(userTlm);

       // String nome = String.valueOf(q.g);


        TextView listaCriadaPor = findViewById(R.id.pessoaCriaLista);
        listaCriadaPor.setText("Lista criada por: " + userTlm);

        getSupportActionBar().setTitle(nomeLista);
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
