package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

public class EstimarCustoLista extends AppCompatActivity {

    private String key, userTlm, position, nomeLista, tipoL;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    private TableLayout tableL;
    private int pos;
    private ArrayList<EditText> idQuant, idCusto;
    private ArrayList<TextView> idProd;
    //private ArrayList<Double> arrayCusto;
    private HashMap<String, HashMap<String, Double>> prodQuantCusto, novoHashMap;
    private ArrayList<String> prod;
    private ArrayList<Double> cust;
    private ArrayAdapter<String> mAdapter;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimar_custo_lista);

        getSupportActionBar().setTitle("Estimar Custo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        key = getIntent().getStringExtra("key");
        userTlm = getIntent().getStringExtra("userTlm");
        nomeLista = getIntent().getStringExtra("nameL");
        tipoL = getIntent().getStringExtra("tipoL");

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");

        position = getIntent().getStringExtra("position");


        tableL = findViewById(R.id.table);
        tableL.setStretchAllColumns(true);
        tableL.bringToFront();

        prod = new ArrayList<>();
        cust = new ArrayList<>();

        //arrayCusto = new ArrayList<>();
        prodQuantCusto = new HashMap<>();
        novoHashMap = new HashMap<>();

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        count = mAdapter.getCount();

        mListener = mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Lista l = dataSnapshot.getValue(Lista.class);
                    //ArrayList<String> prod = l.getProdutos();


                    prodQuantCusto = l.getProdutoCusto();

                    idProd = new ArrayList<>();
                    idQuant = new ArrayList<>();
                    idCusto = new ArrayList<>();
                    int i = 0;

                    if (count == 0) {
                        if (prodQuantCusto != null) {
                            for (Map.Entry<String, HashMap<String, Double>> a : prodQuantCusto.entrySet()) {
                                TableRow tr = new TableRow(getApplicationContext());

                                TextView prod = new TextView(getApplicationContext());
                                prod.setTextSize(18);
                                prod.setId(i);
                                idProd.add(prod);

                                EditText quant = new EditText(getApplicationContext());
                                quant.setId(i);
                                quant.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                idQuant.add(quant);

                                EditText custo = new EditText(getApplicationContext());
                                custo.setId(i);
                                custo.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                //c1.setText(prod.get(i));
                                prod.setText(a.getKey());
                                prod.setTextSize(18);
                                idCusto.add(custo);

                                for (Map.Entry<String, Double> e : a.getValue().entrySet()) {
                                    quant.setText(String.valueOf(e.getKey()));
                                    custo.setText(String.valueOf(e.getValue()));
                                }

                                tr.addView(prod);
                                tr.addView(quant);
                                tr.addView(custo);

                                tableL.addView(tr);

                                i++;

                            }
                        }
                    }
                    count++;
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final Button total = (Button) findViewById(R.id.custo);

        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double totalCusto = 0.0;
                String prod = "";
                Double quant = 0.0;
                Double custo = 0.0;


                for (int i = 0; i < idCusto.size(); i++) {

                    TextView pr = idProd.get(i);
                    EditText q = idQuant.get(i);
                    String quantidade = q.getText().toString();
                    EditText c = idCusto.get(i);
                    String custoP = c.getText().toString();
                    prod = pr.getText().toString();
                    String quanti = quantidade;
                    if (quantidade.indexOf(',') >= 0) {
                        quanti = quantidade.replace(",", ".");
                    }
                    quant = Double.parseDouble(quanti);
                    String custoProd = custoP;
                    if (custoP.indexOf(',') >= 0) {
                        custoProd = custoP.replace(",", ".");
                    }
                    custo = Double.parseDouble(custoProd);

                    String nQuant = quantidade;
                    if (quantidade.indexOf('.') >= 0) {
                        nQuant = quantidade.replace(".", ",");
                    }
                    HashMap<String, Double> res = new HashMap<>();
                    res.put(nQuant, custo);
                    novoHashMap.put(prod, res);

                    if (prod != null && prod.length() > 0) {
                        try {
                            totalCusto += (quant * custo);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "ERROOOO", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                mDatabase.child(key).child("produtoCusto").setValue(novoHashMap);
                mDatabase.child(key).child("custoFinal").setValue(Math.round(totalCusto*100.0) / 100.0);


                //POPUP
                Intent intent = new Intent(EstimarCustoLista.this, PopUpEstimarCusto.class);
                intent.putExtra("custo", Math.round(totalCusto*100.0) / 100.0);

                startActivity(intent);


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EstimarCustoLista.this, MostraLista.class);
        i.putExtra("userTlm", userTlm);
        i.putExtra("position", position);
        i.putExtra("nameL", nomeLista);
        i.putExtra("tipoL", tipoL);
        startActivity(i);
    }

}