package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Notificacao;
import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notificacoes extends AppCompatActivity {

    private String userTlm;
    private DatabaseReference mDatabaseN, mDatabaseU, mDatabaseL;
    private TableLayout tableLayout;
    private String quemDeve, quemPagou, nomePessoa, nomeLista, all;
    private double quantia;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);

        getSupportActionBar().setTitle("Notificações ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userTlm = getIntent().getStringExtra("userTlm");
        mDatabaseN = FirebaseDatabase.getInstance().getReference("notificacoes");
        mDatabaseU = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseL = FirebaseDatabase.getInstance().getReference("listas");

        tableLayout = findViewById(R.id.tableLNotificacoes);

        quemDeve = "";
        quemPagou = "";
        nomePessoa = "";
        nomeLista = "";
        all = "";
        quantia = 0.0;

        Query q = mDatabaseU.orderByChild("numeroTlm").equalTo(userTlm);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    u = singleSnapshot.getValue(User.class);
                    List<String> notificacoes = u.getNotificacoes();
                    Log.d("ANTES", u.getNome());
                    if (notificacoes != null) {
                        for (String idNot : notificacoes) {
                            Log.d("id--", idNot);
                            Query qN = mDatabaseN.orderByChild("idN").equalTo(idNot);
                            qN.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        Notificacao n = singleSnapshot.getValue(Notificacao.class);
                                        quemDeve = n.getQuemDeve();
                                        quemPagou = n.getQuemPagou();
                                        quantia = n.getQuantia();
                                        nomePessoa = u.getNome();
                                        nomeLista = n.getNomeL();


                                        Query q = mDatabaseU.orderByChild("numeroTlm").equalTo(quemPagou);
                                        q.addListenerForSingleValueEvent(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                    User deve = singleSnapshot.getValue(User.class);
                                                    String nomeDeve = deve.getNome();

                                                    if (quemDeve.equals(userTlm)) {
                                                        TableRow tr = new TableRow(getApplicationContext());
                                                        TextView tv = new TextView(getApplicationContext());
                                                        tv.setText("Deves " + (double) Math.round(quantia * 100) / 100 + "€ a " + nomeDeve + "\n"
                                                                + " referente à lista: " + nomeLista);
                                                        tv.setTextSize(16);

                                                        Button pagar = new Button(getApplicationContext());
                                                        pagar.setText("Pagar");
                                                        tr.addView(tv);
                                                        tr.addView(pagar);
                                                        tableLayout.addView(tr);
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }


                                        });




                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
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
        Intent i = new Intent(Notificacoes.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }
}
