package com.example.android.buyshare;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MostraLista extends AppCompatActivity {

    private String userTlm, nomeLista, nomePessoa;
    private DatabaseReference mDatabase;
    private TextView listaCriadaPor;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_lista);

        nomeLista = getIntent().getStringExtra("nameL");
        userTlm = getIntent().getStringExtra("userTlm");



        listaCriadaPor = findViewById(R.id.pessoaCriaLista);
        linearLayout = findViewById(R.id.linearLayoutID);


        mDatabase = FirebaseDatabase.getInstance().getReference("lista");

        Query q = mDatabase.orderByChild("numeroTlm").equalTo(userTlm);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Lista l = singleSnapshot.getValue(Lista.class);
                    String nomePessoa = l.getCriadorLista();
                    ArrayList<String> listaProdutos = l.getProdutos();
                    listaCriadaPor.setText("Lista criada por: " + nomePessoa);

                    for (String a : listaProdutos){
                        CheckBox cb = new CheckBox(getApplicationContext());
                        cb.setText(a);
                        linearLayout.addView(cb);
                    }


                   //ArrayList<String> listaProdutos = u.get
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        //listaCriadaPor.setText("Lista criada por: " + nomePessoa);

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

    /*
    public void onCheckList (ArrayList<String> listaProdutos){
        CheckBox checkBox = findViewById(R.id.)
    }
    */
}
