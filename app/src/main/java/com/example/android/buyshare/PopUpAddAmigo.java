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

public class PopUpAddAmigo extends Activity {

    private static final String msgErro = "Tem de preencher ambos os campos!";
    private static final String msgAddAmigo = "Amigo adicionado com sucesso!";
    private User logado ;
    private User aAdicionar ;
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


                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                User u = userSnapshot.getValue(User.class);

                                //amigo a adicionar
                                if (u.getNumeroTlm().equals(nTele)) {
                                    aAdicionar = userSnapshot.getValue(User.class);
                                    Toast.makeText(getApplicationContext(), "Amigo p/ add " + aAdicionar.getEmail(), Toast.LENGTH_LONG).show();
                                }
                                if(u.getNumeroTlm().equals(tlmUserLogado)){

                                    logado = userSnapshot.getValue(User.class);

                                    //DatabaseReference amigosRef = database.push();
                                    //amigosRef.child(logado.getNumeroTlm()).child("amigos").setValue("0123");



                                    Toast.makeText(getApplicationContext(), "User logado " + logado.getEmail(), Toast.LENGTH_LONG).show();
                                }

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
}
