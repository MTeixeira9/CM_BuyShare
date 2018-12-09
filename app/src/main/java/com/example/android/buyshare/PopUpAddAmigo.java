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

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopUpAddAmigo extends Activity {

    private static final String MSG_ERRO = "Tem de preencher ambos os campos!";

    private User aAdicionar;
    private User logado;
    private String tlmUserLogado;
    private Intent i;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_popupaddamigo);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .30), (int) (height * .30));

        //telemovel user logado
        tlmUserLogado = getIntent().getStringExtra("userTlm");

        Button addAmigo = findViewById(R.id.enviarConvite);
        addAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nTelemovel = findViewById(R.id.nTelemovel);

                i = new Intent();
                final String nTele = nTelemovel.getText().toString();

                if (!nTele.equals("")) {
                    //Adicionar amigos

                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                    mListener = mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                User u = singleSnapshot.getValue(User.class);
                                //ir buscar user logado
                                if (u.getNumeroTlm().equals(tlmUserLogado)) {
                                    logado = u;
                                }
                                //ir buscar user a add
                                if (u.getNumeroTlm().equals(nTele)) {
                                    aAdicionar = u;
                                }
                            }

                            //add um amigo que nao estah registado
                            if (aAdicionar == null) {
                                setResult(0, i);
                                i.putExtra("userTlm", tlmUserLogado);
                                finish();
                            }

                            //add um amigo que jah estah na lista dos amigos do logado
                            else if (logado.getAmigos().get(nTele) != null) {
                                setResult(-1, i);
                                i.putExtra("userTlm", tlmUserLogado);
                                finish();
                            }
                            //nao pode ser amigo de si proprio
                            else if (logado.getNome().equals(aAdicionar.getNome())) {
                                setResult(-2, i);
                                i.putExtra("userTlm", tlmUserLogado);
                                finish();
                            }
                            //add novo amigo
                            else {
                                mDatabase.child(tlmUserLogado).child("amigos").child(aAdicionar.getNumeroTlm()).setValue(aAdicionar.getNome());
                                i.putExtra("nTlm", aAdicionar.getNumeroTlm());
                                i.putExtra("nomeA", aAdicionar.getNome());
                                i.putExtra("userTlm", tlmUserLogado);
                                setResult(1, i);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("TAG", "onCancelled", databaseError.toException());
                        }


                    });
                } else {
                    nTelemovel.setError(MSG_ERRO);
                }
            }
        });
    }


}
