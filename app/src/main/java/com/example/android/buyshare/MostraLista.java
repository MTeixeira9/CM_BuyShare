package com.example.android.buyshare;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
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
    private TextView listaCriadaPor, custoTotal;
    private LinearLayout linearLayout;
    private ValueEventListener mListener;
    private HashMap<String, HashMap<String, Double>> prodQuantCusto;
    private int pos;
    private String idL, finalizada;
    private ArrayList<String> membros;
    private boolean partilhada;
    private Button edit_button, contas_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_lista);

        nomeLista = getIntent().getStringExtra("nameL");
        userTlm = getIntent().getStringExtra("userTlm");
        position = getIntent().getStringExtra("position");
        tipoLista = getIntent().getStringExtra("tipoL");

        pos = Integer.parseInt(position);

        partilhada = false;
        if (tipoLista.equals("partilhada"))
            partilhada = true;

        listaCriadaPor = findViewById(R.id.pessoaCriaLista);
        linearLayout = findViewById(R.id.linearLayoutID);

        custoTotal = findViewById(R.id.custoTotal);

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");
        mDatabase2 = FirebaseDatabase.getInstance().getReference("users");

        prodQuantCusto = new HashMap<>();


        idL = "";
        finalizada = "";
        membros = new ArrayList<>();

        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                int count = 0;

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Lista l = singleSnapshot.getValue(Lista.class);

                    String numTelemovel = l.getCriadorLista();

                    membros = l.getMembrosLista();

                    //LISTA SER PUBLICA
                    if (membros.contains(userTlm) && partilhada && l.isPartilhada()) {
                        prodQuantCusto = l.getProdutoCusto();
                        if (prodQuantCusto != null) {
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
                                finalizada = String.valueOf(l.isFinalizada());
                                for (Map.Entry<String, HashMap<String, Double>> a : prodQuantCusto.entrySet()) {
                                    CheckBox cb = new CheckBox(getApplicationContext());
                                    cb.setTextSize(18);
                                    cb.setText(a.getKey());
                                    linearLayout.addView(cb);
                                }

                                Double custoF = l.getCustoFinal();
                                if (l.isFinalizada()) {
                                    custoTotal.setText("Total: " + String.valueOf(custoF) + "€");
                                }


                                break;
                            }
                        }
                        count++;
                    }
                    //LISTA SER PRIVADA
                    if (membros.contains(userTlm) && !partilhada && !l.isPartilhada()) {
                        prodQuantCusto = l.getProdutoCusto();
                        if (prodQuantCusto != null) {
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
                                finalizada = String.valueOf(l.isFinalizada());

                                for (Map.Entry<String, HashMap<String, Double>> a : prodQuantCusto.entrySet()) {
                                    CheckBox cb = new CheckBox(getApplicationContext());
                                    cb.setTextSize(18);
                                    cb.setText(a.getKey());
                                    linearLayout.addView(cb);

                                    cb.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            CheckBox cb = (CheckBox) view;
                                            if (cb.isChecked()) {
                                                cb.setPaintFlags(cb.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                cb.setTextColor(Color.GRAY);
                                            }
                                            else {
                                                if ((cb.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
                                                    cb.setPaintFlags(cb.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                                    cb.setTextColor(Color.BLACK);
                                                }
                                            }
                                        }
                                    });

                                }
                                break;
                            }
                        }
                        count++;
                    }
                }

                edit_button = findViewById(R.id.edit_button);
                contas_button = findViewById(R.id.dividas);

                if (finalizada.equals("true")) {

                    Query q = mDatabase.orderByChild("idL").equalTo(idL);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                Lista l = singleSnapshot.getValue(Lista.class);

                                String quemPagou = l.getQuemPagou();

                                if (!userTlm.equals(quemPagou)) {
                                    contas_button.setVisibility(View.INVISIBLE);
                                } else {
                                    contas_button.setVisibility(View.VISIBLE);
                                }

                                edit_button.setVisibility(View.INVISIBLE);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    edit_button.setVisibility(View.VISIBLE);
                    contas_button.setVisibility(View.INVISIBLE);
                }

                edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MostraLista.this, EditarLista.class);
                        intent.putExtra("key", idL);
                        intent.putExtra("nameL", nomeLista);
                        intent.putExtra("userTlm", userTlm);
                        intent.putExtra("position", position);
                        intent.putExtra("tipoL", tipoLista);

                        startActivity(intent);
                    }
                });

                contas_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MostraLista.this, DividasReceber.class);
                        intent.putExtra("idL", idL);
                        intent.putExtra("userTlm", userTlm);
                        intent.putExtra("nameL", nomeLista);
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

        getSupportActionBar().setTitle(nomeLista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.removeEventListener(mListener);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        Query q = mDatabase.orderByChild("idL").equalTo(idL);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    Lista l = singleSnapshot.getValue(Lista.class);
                    if (!l.isFinalizada()) {
                        getMenuInflater().inflate(R.menu.mostralista, menu);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

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
            addMembros.putExtra("tipoL", tipoLista);

            startActivity(addMembros);

        } else if (id == R.id.verMembros) {
            Intent membros = new Intent(MostraLista.this, VerMembros.class);
            membros.putExtra("userTlm", userTlm);
            membros.putExtra("nameL", nomeLista);
            membros.putExtra("position", position);
            membros.putExtra("idL", idL);
            membros.putExtra("tipoL", tipoLista);

            startActivity(membros);

        } else if (id == R.id.estimarCusto) {

            Intent estCusto = new Intent(MostraLista.this, EstimarCustoLista.class);
            estCusto.putExtra("userTlm", userTlm);
            estCusto.putExtra("nameL", nomeLista);
            estCusto.putExtra("key", idL);
            estCusto.putExtra("position", position);
            estCusto.putExtra("tipoL", tipoLista);
            startActivity(estCusto);

        } else if (id == R.id.finalizar) {

            Query q = mDatabase.orderByChild("idL").equalTo(idL);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Lista l = singleSnapshot.getValue(Lista.class);
                        if (l.isPartilhada()) {
                            Intent intent = new Intent(MostraLista.this, AdicionarCustoL.class);
                            intent.putExtra("userTlm", userTlm);
                            intent.putExtra("nameL", nomeLista);
                            intent.putExtra("key", idL);
                            intent.putExtra("position", position);
                            intent.putExtra("tipoL", tipoLista);
                            startActivity(intent);
                        } else {
                            mDatabase.child(idL).child("finalizada").setValue(true);
                            Intent intent = new Intent(MostraLista.this, MinhasListas.class);
                            intent.putExtra("userTlm", userTlm);
                            startActivity(intent);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
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
