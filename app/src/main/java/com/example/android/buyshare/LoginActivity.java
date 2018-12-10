package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private static final String MSG_USER_N_EXIST_ERRO = "O user não se encontra registado";
    private static final String MSG_EMPTY_NTLM = "Tem de preencher o número de telemóvel";
    private static final String MSG_INV_NUM_ERRO = "O número deve conter 9 dígitos";
    private static final String MSG_EMPTY_PASS = "Tem de inserir uma password";
    private static final String MSG_LOGIN_SUCES = "Sessão iniciada com sucesso!";
    private static final String MSG_WRONG_PASS = "A password inserida está errada!";
    private boolean res, emptyNTlm, emptyPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        TextView registar = findViewById(R.id.tvRegistar);
        Button entrar = findViewById(R.id.entrar);

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistoActivity.class);
                startActivity(i);
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText nTel = findViewById(R.id.numTel);
                final EditText pass = findViewById(R.id.passRegisto);

                final String numTelS = nTel.getText().toString();
                final String passS = pass.getText().toString();

                emptyNTlm = false;
                emptyPass = false;

                if (numTelS.equals("")) {
                    emptyNTlm = true;
                    nTel.setError(MSG_EMPTY_NTLM);
                }
                else {
                    if (numTelS.length() != 9) {
                        nTel.setError(MSG_INV_NUM_ERRO);
                        emptyNTlm = true;
                    }
                }
                if (passS.equals("")){
                    emptyPass = true;
                    pass.setError(MSG_EMPTY_PASS);
                }
                if(!emptyNTlm && !emptyPass){

                    res = false;
                    Query q = mDatabase.orderByChild("numeroTlm").equalTo(numTelS);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                String p = String.valueOf(singleSnapshot.child("password").getValue());

                                if (p.equals(passS)) {
                                    res = true;
                                    Toast.makeText(getApplicationContext(), MSG_LOGIN_SUCES, Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), MinhasListas.class);
                                    i.putExtra("userTlm", numTelS);
                                    startActivity(i);
                                } else {
                                    res = true;
                                    pass.setError(MSG_WRONG_PASS);
                                }
                            }

                            if (!res)
                                nTel.setError(MSG_USER_N_EXIST_ERRO);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("TAG", "onCancelled", databaseError.toException());
                        }
                    });
                }
            }
        });
    }
}
