package com.example.android.buyshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PopUpAddAmigo extends Activity {

    private static final String msgErro = "Tem de preencher ambos os campos!";
    private static final String msgAddAmigo = "Amigo adicionado com sucesso!";
    private User logado;
    private User aAdicionar;
    private String tlmUserLogado;


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
                TextView nTelemovel = (TextView) findViewById(R.id.nTelemovel);
                TextView nomeAmigo = (TextView) findViewById(R.id.nomeAmigo);
                Intent i = new Intent();
                final String nTele = nTelemovel.getText().toString();
                String nome = nomeAmigo.getText().toString();


                if (!nome.equals("") && !nTele.equals("")) {
                    i.putExtra("nTlm", nTele);
                    i.putExtra("nomeA", nome);
                    setResult(RESULT_OK, i);

                    //Adicionar amigos
                    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");


                    //User logado
                    Query qLogado = database.orderByChild("numeroTlm").equalTo(tlmUserLogado);
                    qLogado.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                logado = singleSnapshot.getValue(User.class);
                                //DatabaseReference amigosRef = database.push();
                                //amigosRef.child(logado.getNumeroTlm()).child("amigos").setValue("0123");
                                Log.d("ORDEM LOGADO----","1");

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
                                Log.d("ORDEM ADD----","2");
                                addAmigo(aAdicionar.getNumeroTlm(), logado.getNumeroTlm(), database);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("TAG", "onCancelled", databaseError.toException());
                        }
                    });

                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), msgErro, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void addAmigo(final String numTAdd , final String numTLog, final DatabaseReference mDatabase) {
        Log.d("ORDEM ----","3");

        /*
        Query q = mDatabase.orderByChild("numeroTlm").equalTo(numTLog);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            */
                DatabaseReference amigosRef = mDatabase.child(numTLog).child("amigos").push();
                Log.d("ORDEM ----","4");
                Map<String, Object> map = new HashMap<>();
                Log.d("ORDEM ----","5");
                map.put(numTAdd, "NOME");

                amigosRef.updateChildren(map);


                /*
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    Log.d("ORDEM ----","4");
                    User u = singleSnapshot.child("amigos").getValue(User.class);
                    Log.d("ORDEM ----","5");
                    ArrayList<String> lAmigos = u.getAmigos();
                    lAmigos.add(numTAdd);
                    mDatabase.child(numTLog).child("amigos").setValue(lAmigos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
            */

    }
}
