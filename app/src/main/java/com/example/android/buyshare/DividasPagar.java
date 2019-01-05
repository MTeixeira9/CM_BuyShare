package com.example.android.buyshare;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DividasPagar extends AppCompatActivity {

    private TextView despesaTextV, numPessoasTextV, valorEmprestado;
    private DatabaseReference mDatabaseL, mDatabaseU;
    private String idL, userTlm, e;
    private Double custoFinal;
    private ArrayList<String> membrosL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividas_pagar);

        getSupportActionBar().setTitle("Dívidas ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idL = getIntent().getStringExtra("idL");
        userTlm = getIntent().getStringExtra("userTlm");

        despesaTextV = findViewById(R.id.despesaTotalPagar);
        numPessoasTextV = findViewById(R.id.numPessoasDPagar);
        valorEmprestado = findViewById(R.id.devesTV);

        mDatabaseL = FirebaseDatabase.getInstance().getReference("listas");
        mDatabaseU = FirebaseDatabase.getInstance().getReference("users");

        custoFinal = 0.0;
        membrosL = new ArrayList<>();
        e = "";

        Query q = mDatabaseL.child(idL);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista l = dataSnapshot.getValue(Lista.class);
                custoFinal = l.getCustoFinal();
                membrosL = l.getMembrosLista();
                Double emprestado = custoFinal - (custoFinal / membrosL.size());
                e = String.valueOf(emprestado);

                despesaTextV.setText("Despesa Total: " + custoFinal + "€");
                numPessoasTextV.setText("Nº de pessoas envolvidas: " + membrosL.size() + "");


                Query qU = mDatabaseU.child(l.getCriadorLista());
                qU.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nome = String.valueOf(dataSnapshot.child("nome").getValue());
                        valorEmprestado.setText("Deves: " + e + "€ a " + nome + "!");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
