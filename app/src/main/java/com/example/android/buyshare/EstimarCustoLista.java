package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class EstimarCustoLista extends AppCompatActivity {

    private String key, userTlm, position;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    private TableLayout tableL;
    private int pos;
    private ArrayList<EditText> idEditText;
    private ArrayList<Double> arrayCusto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimar_custo_lista);

        getSupportActionBar().setTitle("Estimar Custo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        key = getIntent().getStringExtra("key");
        userTlm = getIntent().getStringExtra("userTlm");

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");

        position = getIntent().getStringExtra("position");


        tableL = findViewById(R.id.table);
        tableL.setStretchAllColumns(true);
        tableL.bringToFront();

        arrayCusto = new ArrayList<>();


        mListener = mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Lista l = dataSnapshot.getValue(Lista.class);
                ArrayList<String> prod = l.getProdutos();

                idEditText = new ArrayList<>();

                for(int i = 0; i < prod.size(); i++ ){
                    TableRow tr = new TableRow(getApplicationContext());
                    TextView c1 = new TextView(getApplicationContext());
                    c1.setTextSize(18);
                    EditText c2 = new EditText(getApplicationContext());
                    c2.setId(i);

                    idEditText.add(c2);

                    c1.setText(prod.get(i));
                    c2.setText("");

                    tr.addView(c1);
                    tr.addView(c2);

                    tableL.addView(tr);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        final Button custo = (Button) findViewById(R.id.custo);

        custo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double totalCusto=0.0;
                Double valor = 0.0;
                String text ="";
                for(int i = 0; i < idEditText.size(); i++){

                    EditText et = idEditText.get(i);
                    String p = et.getText().toString();
                    valor = Double.parseDouble(p);
                    arrayCusto.add(valor);

                    if(p != null && p.length()>0){
                        try{
                            totalCusto+= valor;
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "ERROOOO", Toast.LENGTH_LONG).show();
                        }
                    }


                }

                //POPUP
                Intent intent = new Intent(EstimarCustoLista.this, PopUpEstimarCusto.class);
                intent.putExtra("custo", totalCusto);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Size: " + arrayCusto.size(), Toast.LENGTH_LONG).show();
            }
        });

        mDatabase.child(key).child("arrayCusto").setValue(arrayCusto);






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
        startActivity(i);
    }

}