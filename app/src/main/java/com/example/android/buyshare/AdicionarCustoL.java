package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdicionarCustoL extends AppCompatActivity {

    //private Spinner spinner;
    private String userTlm, nomeLista, key, position;
    private DatabaseReference mDatabaseL, mDatabaseU;
    private ValueEventListener mListener;
    private TextView tv;
    private ArrayList<String> membrosL, nomesMembrosLista, numerosTlmMembros;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private EditText custoFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_custo_l);

        getSupportActionBar().setTitle("Adicionar Custo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userTlm = getIntent().getStringExtra("userTlm");
        nomeLista = getIntent().getStringExtra("nameL");
        key = getIntent().getStringExtra("key");
        position = getIntent().getStringExtra("position");

        mDatabaseL = FirebaseDatabase.getInstance().getReference("listas");
        mDatabaseU = FirebaseDatabase.getInstance().getReference("users");

        tv = findViewById(R.id.editTextCusto);
        membrosL = new ArrayList<>();
        nomesMembrosLista = new ArrayList<>();
        numerosTlmMembros = new ArrayList<>();
        spinner = findViewById(R.id.spinnerPagoPor);
        custoFinal = findViewById(R.id.editTextCusto);

        mListener = mDatabaseL.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista l = dataSnapshot.getValue(Lista.class);

                membrosL = l.getMembrosLista();

                if (membrosL != null) {
                    for (String a : membrosL) {
                        Query q = mDatabaseU.child(a);
                        q.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String nome = String.valueOf(dataSnapshot.child("nome").getValue());
                                String numeroTlm = String.valueOf(dataSnapshot.child("numeroTlm").getValue());
                                numerosTlmMembros.add(numeroTlm);
                                nomesMembrosLista.add(nome);

                                //preencher o spinner com os nomesMembrosLista
                                if (nomesMembrosLista != null) {
                                    spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                            android.R.layout.simple_spinner_item, nomesMembrosLista);
                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinner = (Spinner) findViewById(R.id.spinnerPagoPor);
                                    spinner.setAdapter(spinnerArrayAdapter);

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


        Button finalizar = (Button) findViewById(R.id.finalizar);
        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Query q = mDatabaseL.orderByChild("idL").equalTo(key);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            Lista l = singleSnapshot.getValue(Lista.class);


                            //ver que nome estah selecionado no spinner
                            int posSel = spinner.getSelectedItemPosition();
                            //String selected = spinner.getSelectedItem().toString();
                            String numArray = numerosTlmMembros.get(posSel);
                            String numCriadorLista = l.getCriadorLista();
                            String c = custoFinal.getText().toString();
                            Double custD = Double.parseDouble(c);



                            mDatabaseL.child(key).child("custoFinal").setValue(custD);
                            mDatabaseL.child(key).child("quemPagou").setValue(numArray);
                            mDatabaseL.child(key).child("finalizada").setValue(true);


                            //se o utilizador que finalizar for
                            if (numArray.equals(numCriadorLista) && userTlm.equals(numArray)) {
                                //VAI PARA A PAGINA DividasReceber
                                Intent i = new Intent(AdicionarCustoL.this, DividasReceber.class);
                                i.putExtra("idL", key);
                                i.putExtra("userTlm", userTlm);
                                startActivity(i);


                            }else{
                                //VAI PARA AS MINHAS LISTAS

                            }


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }


                });
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
        Intent i = new Intent(AdicionarCustoL.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }

    public static class EditModel {

        private String editTextValue;

        public String getEditTextValue() {
            return editTextValue;
        }

        public void setEditTextValue(String editTextValue) {
            this.editTextValue = editTextValue;
        }
    }
}