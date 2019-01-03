package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MostraLista extends AppCompatActivity {

    private String userTlm, nomeLista, position, tipoLista;
    private DatabaseReference mDatabase, mDatabase2;
    private TextView listaCriadaPor;
    private LinearLayout linearLayout;
    private ValueEventListener mListener;
    private HashMap<String, Double> produtoCusto;
    private int pos;
    private String idL;
    private ArrayList<String> membros ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_lista);

        nomeLista = getIntent().getStringExtra("nameL");
        userTlm = getIntent().getStringExtra("userTlm");
        position = getIntent().getStringExtra("position");
        //tipoLista = getIntent().getStringExtra("tipoL");

        pos = Integer.parseInt(position);

        listaCriadaPor = findViewById(R.id.pessoaCriaLista);
        linearLayout = findViewById(R.id.linearLayoutID);

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");
        mDatabase2 = FirebaseDatabase.getInstance().getReference("users");

        produtoCusto = new HashMap<>();

        idL = "";
        membros = new ArrayList<>();

        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Lista l = singleSnapshot.getValue(Lista.class);

                    String numTelemovel = l.getCriadorLista();
                    membros = l.getMembrosLista();

                    if (membros.contains(userTlm)) {

                        produtoCusto = l.getProdutoCusto();

                        if (produtoCusto != null) {
                            if (count == pos) {

                                /**
                                 * IR BUSCAR O NOME DO CRIADOR DA LISTA
                                 */
                                Query q = mDatabase2.orderByChild("numeroTlm").equalTo(numTelemovel);
                                q.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                            String nome = String.valueOf(singleSnapshot.child("nome").getValue());
                                            listaCriadaPor.setText("Lista criada por: " + nome);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("TAG", "onCancelled", databaseError.toException());
                                    }
                                });

                                idL = l.getIdL();
                                for (Map.Entry<String, Double> a : produtoCusto.entrySet()) {
                                    CheckBox cb = new CheckBox(getApplicationContext());
                                    cb.setTextSize(18);
                                    cb.setText(a.getKey());
                                    linearLayout.addView(cb);
                                }
                                break;
                            }
                        }
                        count++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

        getSupportActionBar().setTitle(nomeLista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button edit_button = findViewById(R.id.edit_button);

        edit_button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MostraLista.this, EditarLista.class);
                intent.putExtra("key", idL);
                intent.putExtra("nameL", nomeLista);
                intent.putExtra("userTlm", userTlm);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.removeEventListener(mListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mostralista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.addMembros) {
            Intent addMembros = new Intent(MostraLista.this, AdicionarMembrosMostraLista.class);
            addMembros.putExtra("userTlm", userTlm);
            addMembros.putExtra("nameL", nomeLista);
            addMembros.putExtra("position", position);
            addMembros.putExtra("idL", idL);

            startActivity(addMembros);

        } else if (id == R.id.verMembros) {
            Intent membros = new Intent(MostraLista.this, VerMembros.class);
            membros.putExtra("userTlm", userTlm);
            membros.putExtra("nameL", nomeLista);
            membros.putExtra("position", position);
            membros.putExtra("idL", idL);

            startActivity(membros);

        } else if (id == R.id.estimarCusto) {

            Intent estCusto = new Intent(MostraLista.this, EstimarCustoLista.class);
            estCusto.putExtra("userTlm", userTlm);
            estCusto.putExtra("nameL", nomeLista);
            estCusto.putExtra("key", idL);
            estCusto.putExtra("position", position);
            startActivity(estCusto);

        } else if (id == R.id.finalizar) {
            Intent intent = new Intent(MostraLista.this, AdicionarCustoL.class);
            intent.putExtra("userTlm", userTlm);
            intent.putExtra("nameL", nomeLista);
            intent.putExtra("key", idL);
            intent.putExtra("position", position);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MostraLista.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }
}
