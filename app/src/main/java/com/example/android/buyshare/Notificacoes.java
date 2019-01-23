package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

public class    Notificacoes extends AppCompatActivity {

    private String userTlm, nomeLista;
    private DatabaseReference mDatabaseN, mDatabaseU, mDatabaseU2;
    private TableLayout tableLayout;
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
        mDatabaseU2 = FirebaseDatabase.getInstance().getReference("users");

        tableLayout = findViewById(R.id.tableLNotificacoes);

        nomeLista = "";
        quantia = 0.0;
        u = null;

        Query q = mDatabaseU.child(userTlm);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                u = dataSnapshot.getValue(User.class);
                List<String> notificacoes = u.getNotificacoes();

                if (notificacoes != null) {
                    for (final String idNot : notificacoes) {
                        Query qN = mDatabaseN.child(idNot);
                        qN.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Notificacao n = dataSnapshot.getValue(Notificacao.class);
                                if (!n.isPago()) {
                                    quantia = n.getQuantia();
                                    nomeLista = n.getNomeL();

                                    String texto = "Deves " + (double) Math.round(quantia * 100) / 100 +
                                            "€ a referente à lista: " + nomeLista;

                                    TableRow tr = new TableRow(getApplicationContext());
                                    TextView tv = new TextView(getApplicationContext());
                                    tv.setText(texto);
                                    tv.setTextSize(16);

                                    Button pagar = new Button(getApplicationContext());
                                    pagar.setText("Pagar");
                                    tr.addView(tv);
                                    tr.addView(pagar);
                                    tableLayout.addView(tr);

                                    pagar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mDatabaseN.child(idNot).child("pago").setValue(true);
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