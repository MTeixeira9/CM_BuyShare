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

    private static final String MSG_ERRO = "Tem de preencher este campo!";
    private static final String MSG_ERRO1 = "O utilizador já se encontra na sua lista de amigos!";
    private static final String MSG_ERRO2 = "Não pode ser amigo de si próprio!";
    private static final String MSG_ERRO3 = "O utilizador que quer adicionar não está registado!";
    private static final String MSG_INV_NUM_ERRO = "O número deve conter 9 dígitos";

    private User aAdicionar;
    private User logado;
    private String tlmUserLogado;
    private Intent i;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    public boolean emptyAdd, proprio, alreadyFriend;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_pop_up_add_amigo);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .75), (int) (height * .25));

        //telemovel user logado
        tlmUserLogado = getIntent().getStringExtra("userTlm");
        count = 0;

        Button addAmigo = findViewById(R.id.enviarConvite);
        addAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText nTelemovel = findViewById(R.id.nTelemovel);

                i = new Intent();
                final String nTele = nTelemovel.getText().toString();

                if (!nTele.equals("")) {
                    if (nTele.length() == 9) {
                        mDatabase = FirebaseDatabase.getInstance().getReference("users");
                        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                aAdicionar = null;
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    User u = singleSnapshot.getValue(User.class);
                                    //ir buscar user logado (apenas 1 vez)
                                    if (logado == null && u.getNumeroTlm().equals(tlmUserLogado)) {
                                        logado = u;
                                    }
                                    //ir buscar user a add
                                    if (u.getNumeroTlm().equals(nTele)) {
                                        aAdicionar = u;
                                    }
                                }
                                count++;

                                if(aAdicionar != null)
                                    Log.d(count + " -> LOGADO: " + logado.getNome(), " --- ADD: " + aAdicionar.getNome());

                                emptyAdd = false;
                                proprio = false;
                                alreadyFriend = false;

                                //add um amigo que nao estah registado
                                if (aAdicionar == null) {
                                    nTelemovel.setError(MSG_ERRO3);
                                    emptyAdd = true;
                                }
                                //add a si proprio
                                if (!emptyAdd) {
                                    if (tlmUserLogado.equals(aAdicionar.getNumeroTlm())) {
                                        nTelemovel.setError(MSG_ERRO2);
                                        proprio = true;
                                    }
                                }
                                //user tem amigos
                                if (!emptyAdd && !proprio) {
                                    if (logado.getAmigos() != null) {
                                        //add um amigo que jah estah na lista dos amigos do logado
                                        if (logado.getAmigos().get(nTele) != null) {
                                            nTelemovel.setError(MSG_ERRO1);
                                            alreadyFriend = true;
                                        } else {
                                            mDatabase.child(tlmUserLogado).child("amigos").child(aAdicionar.getNumeroTlm()).setValue(aAdicionar.getNome());
                                            mDatabase.child(aAdicionar.getNumeroTlm()).child("amigos").child(tlmUserLogado).setValue(logado.getNome());

                                            i.putExtra("nTlm", aAdicionar.getNumeroTlm());
                                            i.putExtra("nomeA", aAdicionar.getNome());
                                            i.putExtra("userTlm", tlmUserLogado);
                                            setResult(1, i);
                                            finish();
                                        }
                                    }
                                    //user nao tem amigos
                                    else {
                                        mDatabase.child(tlmUserLogado).child("amigos").child(aAdicionar.getNumeroTlm()).setValue(aAdicionar.getNome());
                                        mDatabase.child(aAdicionar.getNumeroTlm()).child("amigos").child(tlmUserLogado).setValue(logado.getNome());
                                        i.putExtra("nTlm", aAdicionar.getNumeroTlm());
                                        i.putExtra("nomeA", aAdicionar.getNome());
                                        i.putExtra("userTlm", tlmUserLogado);
                                        setResult(1, i);
                                        finish();
                                    }
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("TAG", "onCancelled", databaseError.toException());
                            }


                        });
                    } else {
                        nTelemovel.setError(MSG_INV_NUM_ERRO);
                    }
                } else {
                    nTelemovel.setError(MSG_ERRO);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mListener != null)
            mDatabase.removeEventListener(mListener);
    }

}
