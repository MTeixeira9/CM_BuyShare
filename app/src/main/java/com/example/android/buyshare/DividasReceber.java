package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DividasReceber extends AppCompatActivity {

    private TextView despesaTextV, numPessoasTextV, valorEmprestado;
    private String idL, userTlm;
    private DatabaseReference mDatabaseL, mDatabaseU;
    private Double custoFinal;
    private ArrayList<String> membrosL;
    private TableLayout tableL;
    private String e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividas_receber);

        getSupportActionBar().setTitle("Dívidas ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idL = getIntent().getStringExtra("idL");
        userTlm = getIntent().getStringExtra("userTlm");
        despesaTextV = findViewById(R.id.despesaTotal);
        numPessoasTextV = findViewById(R.id.numPessoas);
        valorEmprestado = findViewById(R.id.emprestaste);
        mDatabaseL = FirebaseDatabase.getInstance().getReference("listas");
        mDatabaseU = FirebaseDatabase.getInstance().getReference("users");
        custoFinal = 0.0;
        membrosL = new ArrayList<>();
        e = "";

        tableL = findViewById(R.id.tableLDividasR);
        tableL.setStretchAllColumns(true);
        tableL.bringToFront();

        Query q = mDatabaseL.child(idL);
        q.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista l = dataSnapshot.getValue(Lista.class);
                custoFinal = l.getCustoFinal();
                membrosL = l.getMembrosLista();
                Double emprestado = custoFinal - (custoFinal / membrosL.size());
                e = String.valueOf(emprestado);

                despesaTextV.setText("Despesa Total: " + custoFinal);
                numPessoasTextV.setText("Nº de pessoas envolvidas: " + membrosL.size() + "");
                valorEmprestado.setText("Emprestaste: " + e + "€");

                int i = 0;
                for (String a : membrosL) {

                    Query qU = mDatabaseU.child(a);
                    qU.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            TableRow tr = new TableRow(getApplicationContext());

                            TextView quemDeve = new TextView(getApplicationContext());
                            String nome = String.valueOf(dataSnapshot.child("nome").getValue());

                            quemDeve.setTextSize(18);
                            //Falta acrescentar o valor
                            quemDeve.setText(nome + " deve-te " + e + "€!");

                            Button notifica = new Button(getApplicationContext());
                            notifica.setText("Notifa");

                            Button pago = new Button(getApplicationContext());
                            pago.setText("Pago!");

                            tr.addView(quemDeve);
                            tr.addView(notifica);
                            tr.addView(pago);

                            tableL.addView(tr);





                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    i++;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        Intent i = new Intent(DividasReceber.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }
}
