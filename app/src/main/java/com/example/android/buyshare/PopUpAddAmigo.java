package com.example.android.buyshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PopUpAddAmigo extends Activity {

    private static final String MSG_ERRO = "Tem de preencher ambos os campos!";
    private static final String MSG_SUCESSO = "Amigo adicionado com sucesso!";

    private User aAdicionar;
    private User logado;
    private String tlmUserLogado;
    private String nomeLogado;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_popupaddamigo);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .32));

        //telemovel user logado
        tlmUserLogado = getIntent().getStringExtra("userTlm");

        Button addAmigo = (Button) findViewById(R.id.enviarConvite);
        addAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nTelemovel = (EditText) findViewById(R.id.nTelemovel);
                EditText nomeAmigo = (EditText) findViewById(R.id.nomeAmigo);
                i = new Intent();
                final String nTele = nTelemovel.getText().toString();
                final String nome = nomeAmigo.getText().toString();

                if (!nome.equals("") && !nTele.equals("")) {

                    //Adicionar amigos
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");

                    //User logado
                    Query qlogado = database.orderByChild("numeroTlm").equalTo(tlmUserLogado);
                    qlogado.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                logado = singleSnapshot.getValue(User.class);
                                nomeLogado = logado.getNome();

                                //add um amigo que jah estah na lista dos amigos do logado
                                if (logado.getAmigos().get(nTele) != null) {
                                    setResult(-1, i);
                                    i.putExtra("userTlm", tlmUserLogado);
                                    finish();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("TAG", "onCancelled", databaseError.toException());
                        }
                    });

                    //Amigo a adicionar
                    Query qAAdicionar = database.orderByChild("numeroTlm").equalTo(nTele);
                    qAAdicionar.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                aAdicionar = singleSnapshot.getValue(User.class);

                                //nao pode ser amigo de si proprio
                                if (nomeLogado.equals(aAdicionar.getNome())) {
                                    setResult(-2, i);
                                    i.putExtra("userTlm", tlmUserLogado);
                                    finish();
                                }

                                database.child(tlmUserLogado).child("amigos").child(aAdicionar.getNumeroTlm()).setValue(aAdicionar.getNome());
                                Toast.makeText(getApplicationContext(), MSG_SUCESSO, Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("TAG", "onCancelled", databaseError.toException());
                        }
                    });

                    i.putExtra("nTlm", nTele);
                    i.putExtra("nomeA", nome);
                    i.putExtra("userTlm", tlmUserLogado);
                    setResult(1, i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), MSG_ERRO, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
