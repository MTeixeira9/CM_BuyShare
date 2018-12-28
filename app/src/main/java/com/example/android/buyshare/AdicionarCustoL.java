package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdicionarCustoL extends AppCompatActivity {

    private Spinner spinner;
    private String userTlm, nomeLista, key, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_custo_l);

        getSupportActionBar().setTitle("Adicionar Custo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        key = getIntent().getStringExtra("key");
        userTlm = getIntent().getStringExtra("userTlm");
        nomeLista = getIntent().getStringExtra("nameL");

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");

        position = getIntent().getStringExtra("position");








        spinner = (Spinner)findViewById(R.id.spinnerPagoPor);

        ArrayList<String> list = new ArrayList<>();
        list.add("Nome1");
        list.add("Nome2");
        list.add("Nome3");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
    }
}
