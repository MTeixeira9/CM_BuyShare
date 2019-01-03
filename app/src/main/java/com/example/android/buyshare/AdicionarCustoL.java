package com.example.android.buyshare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdicionarCustoL extends AppCompatActivity {

    //private Spinner spinner;
    private String userTlm, nomeLista, key, position;
    private DatabaseReference mDatabase, mDatabase2;
    private ValueEventListener mListener;
    private HashMap<String,HashMap<String, Double>> prodQuantCusto;
    private double custoTotal;
    private TextView tv;
    private ArrayList<String> membrosL, nomesLista;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_custo_l);

        getSupportActionBar().setTitle("Adicionar Custo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userTlm = getIntent().getStringExtra("userTlm");
        nomeLista = getIntent().getStringExtra("nameL");
        key = getIntent().getStringExtra("key");
        position = getIntent().getStringExtra("position");

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");
        mDatabase2 = FirebaseDatabase.getInstance().getReference("users");

        tv = findViewById(R.id.editTextCusto);
        prodQuantCusto = new HashMap<>();
        membrosL = new ArrayList<>();
        nomesLista = new ArrayList<>();
        custoTotal = 0.0;
        spinner = findViewById(R.id.spinnerPagoPor);

        mListener = mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista l = dataSnapshot.getValue(Lista.class);

                prodQuantCusto = l.getProdutoCusto();
                membrosL = l.getMembrosLista();

                if(prodQuantCusto != null) {
                    for (Map.Entry<String, HashMap<String, Double>> a : prodQuantCusto.entrySet()) {
                        for (Map.Entry<String, Double> e : a.getValue().entrySet())
                        custoTotal += (Double.parseDouble(e.getKey()) * e.getValue());
                    }
                }

                if(membrosL != null) {
                    for (String a : membrosL) {
                        Query q = mDatabase2.child(a);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String nome = String.valueOf(dataSnapshot.child("nome").getValue());
                                nomesLista.add(nome);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }


                if(nomesLista != null) {
                    spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, nomesLista);
                    spinner.setAdapter(spinnerArrayAdapter);
                }





                tv.setText(String.valueOf(custoTotal));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        spinner = (Spinner)findViewById(R.id.spinnerPagoPor);

        ArrayList<String> list = new ArrayList<>();
        list.add("Nome1");
        list.add("Nome2");
        list.add("Nome3");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
    }
}
