package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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
    private DatabaseReference mDatabaseL;
    private Double custoFinal;
    private ArrayList<String> membrosL;

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
        custoFinal = 0.0;
        membrosL = new ArrayList<>();

        Query q = mDatabaseL.child(idL);
        q.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista l = dataSnapshot.getValue(Lista.class);
                custoFinal = l.getCustoFinal();
                membrosL = l.getMembrosLista();
                Double emprestado = custoFinal-(custoFinal/membrosL.size());
                String e = String.valueOf(emprestado);

                despesaTextV.setText("Despesa Total: " + custoFinal);
                numPessoasTextV.setText("Nº de pessoas envolvidas: " + membrosL.size()+"");
                valorEmprestado.setText("Emprestaste: " + e + "€");

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
