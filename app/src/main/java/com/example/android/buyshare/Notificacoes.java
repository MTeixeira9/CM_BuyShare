package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Notificacao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Notificacoes extends AppCompatActivity {

    private String userTlm;
    private DatabaseReference mDatabaseN, mDatabaseU, mDatabaseL;
    private TableLayout tableLayout;
    private ArrayAdapter<String> mAdapter;
    private ValueEventListener mListener;
    private String quemDeve, quemPagou, nomePessoa, nomeLista;
    private double quantia;


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
        quantia = 0.0;

        mListener = mDatabaseN.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Notificacao n = singleSnapshot.getValue(Notificacao.class);


                    quemDeve = n.getQuemDeve();
                    quemPagou = n.getQuemPagou();
                    quantia = n.getQuantia();

                    Query q = mDatabaseU.child(quemPagou);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            nomePessoa = String.valueOf(dataSnapshot.child("nome").getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
/*
                    Query q2 = mDatabaseL.child(n.getIdL());
                    q2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            nomeLista = String.valueOf(dataSnapshot.child("nomeLista").getValue());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
*/

                    if (quemDeve.equals(userTlm)) {

                        TableRow tr = new TableRow(getApplicationContext());


                        TextView tv = new TextView(getApplicationContext());
                        tv.setText("Deves " + (double) Math.round(quantia * 100) / 100 + "€ à " + nomePessoa + "\n"
                                + " referente à lista: " );
                        tv.setTextSize(15);

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
