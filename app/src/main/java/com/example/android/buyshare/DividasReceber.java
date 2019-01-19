package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.example.android.buyshare.Database.Notificacao;
import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DividasReceber extends AppCompatActivity {

    private TextView despesaTextV, numPessoasTextV, valorEmprestado;
    private String idL, userTlm, nomeLista;
    private DatabaseReference mDatabaseL, mDatabaseU, mDatabaseN;
    private Double custoFinal;
    private ArrayList<String> membrosL;
    private TableLayout tableL;
    private String e;
    private ArrayList<Button> botoesNotifica;
    private HashMap<Integer, String> posNumTlNotifica;
    private HashMap<String, Boolean> notificados;
    private int posNotifica;
    private String nome;
    private TableRow tr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividas_receber);

        getSupportActionBar().setTitle("Dívidas ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idL = getIntent().getStringExtra("idL");
        userTlm = getIntent().getStringExtra("userTlm");

        despesaTextV = findViewById(R.id.despesaTotalPagar);
        numPessoasTextV = findViewById(R.id.numPessoasDPagar);
        valorEmprestado = findViewById(R.id.emprestaste);
        mDatabaseL = FirebaseDatabase.getInstance().getReference("listas");
        mDatabaseU = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseN = FirebaseDatabase.getInstance().getReference("notificacoes");
        custoFinal = 0.0;
        membrosL = new ArrayList<>();
        e = "";
        nome = "";

        tableL = findViewById(R.id.tableLDividasR);
        tableL.setStretchAllColumns(true);
        tableL.bringToFront();
        posNumTlNotifica = new HashMap<>();
        botoesNotifica = new ArrayList<>();
        notificados = new HashMap<>();

        Query q = mDatabaseL.child(idL);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista l = dataSnapshot.getValue(Lista.class);
                custoFinal = l.getCustoFinal();
                membrosL = l.getMembrosLista();
                notificados = l.getNotificados();
                Double emprestado = custoFinal - (custoFinal / membrosL.size());
                e = String.valueOf(Math.round(emprestado*100.0) / 100.0);
                Double d = custoFinal/membrosL.size();

                final String deveTE = String.valueOf(Math.round(d*100.0) / 100.0);
                final Double quantiaADever = custoFinal/membrosL.size();

                nomeLista = l.getNomeLista();

                despesaTextV.setText("Despesa Total: " + custoFinal +"€");
                numPessoasTextV.setText("Nº de pessoas envolvidas: " + membrosL.size() + "");
                valorEmprestado.setText("Emprestaste: " + e + "€");

                int i = 0;
                posNotifica = 1;
                for (final String a : membrosL) {
                    if(i !=0){
                        Query qU = mDatabaseU.child(a);
                        qU.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                tr = new TableRow(getApplicationContext());

                                TextView quemDeve = new TextView(getApplicationContext());
                                nome = String.valueOf(dataSnapshot.child("nome").getValue());
                                String numTlm = String.valueOf(dataSnapshot.child("numeroTlm").getValue());
                                quemDeve.setTextSize(18);
                                quemDeve.setText(nome + " deve-te " + deveTE + "€!");

                                Boolean notificado = notificados.get(a);

                                final Button notifica = new Button(getApplicationContext());
                                TextView noti = new TextView(getApplicationContext());

                                tr.addView(quemDeve);

                                if (notificado){
                                    noti.setTextSize(18);
                                    noti.setText("Notificado!");
                                    tr.addView(noti);
                                }
                                else {
                                    notifica.setText("Notifica");
                                    notifica.setId(posNotifica);

                                    posNumTlNotifica.put(posNotifica,numTlm );
                                    botoesNotifica.add(notifica);
                                    tr.addView(notifica);
                                    //Toast.makeText(getApplicationContext(),posNotifica + " <- POSnOTIFICA" ,Toast.LENGTH_LONG).show();
                                    notifica.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //Toast.makeText(getApplicationContext(),v.getId()+" <- ID" ,Toast.LENGTH_LONG).show();
                                            notificados.put(a, true);
                                            String idN = mDatabaseN.push().getKey();
                                            ArrayList<String> notif = new ArrayList<>();
                                            notif.add(idN);
                                            Notificacao n = new Notificacao(idN, idL,
                                                    membrosL.get(0), posNumTlNotifica.get(v.getId()), quantiaADever, nomeLista, "");
                                            mDatabaseN.child(idN).setValue(n);
                                            mDatabaseU.child(posNumTlNotifica.get(v.getId())).child("notificacoes").setValue(notif);
                                            mDatabaseL.child(idL).child("notificados").setValue(notificados);
                                            //notifica.setVisibility(View.INVISIBLE);
                                            tr.removeView(notifica);
                                            TextView tv = new TextView(getApplicationContext());
                                            tv.setTextSize(18);
                                            tv.setText("Notificado!");
                                            tr.addView(tv);
                                        }
                                    });
                                }

                                tableL.addView(tr);

                                posNotifica++;

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

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
