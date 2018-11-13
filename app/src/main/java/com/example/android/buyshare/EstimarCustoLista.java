package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.ArrayList;

public class EstimarCustoLista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimar_custo_lista);

        getSupportActionBar().setTitle("Estimar Custo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button custo = (Button) findViewById(R.id.custo);



        custo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableLayout table = findViewById(R.id.table);
                Double totalCusto=0.0;

                for (int i = 0; i<table.getChildCount(); i++) {
                    View viewL = table.getChildAt(i);
                    TableRow r = (TableRow) viewL;
                    View viewC = r.getChildAt(1);
                    EditText preco = (EditText) viewC;
                    String p = preco.getText().toString();

                    if(p != null && p.length()>0){
                        try{
                            totalCusto+= Double.parseDouble(p);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "ERROOOO", Toast.LENGTH_LONG).show();
                        }
                    }

                }

                //POPUP
                Intent intent = new Intent(EstimarCustoLista.this, PopUpEstimarCusto.class);
                intent.putExtra("custo", totalCusto);
                startActivity(intent);
            }
        });
    }
}

