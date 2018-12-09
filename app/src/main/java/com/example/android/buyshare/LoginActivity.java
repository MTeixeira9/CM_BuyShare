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
    private String emptyFields = "Tem de preencher ambos os campos!";
    private String loginSucess = "Sessão iniciada com sucesso!";
    private String wrongPass = "A password inserida está errada!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        TextView registar = (TextView) findViewById(R.id.tvRegistar);
        Button entrar = (Button) findViewById(R.id.entrar);

        registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistoActivity.class);
                finish();
                startActivity(i);
            }
        });

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nTel =(EditText) findViewById(R.id.numTel);
                EditText pass = (EditText) findViewById(R.id.passRegisto);

                final String numTelS = nTel.getText().toString();
                final String passS = pass.getText().toString();

                if (numTelS.equals("") && passS.equals(""))
                    Toast.makeText(getApplicationContext(), emptyFields, Toast.LENGTH_LONG).show();
                else {

                    Query q = mDatabase.orderByChild("numeroTlm").equalTo(numTelS);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                String p = String.valueOf(singleSnapshot.child("password").getValue());

                                if (p.equals(passS)) {
                                    Toast.makeText(getApplicationContext(), loginSucess, Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getApplicationContext(), MinhasListas.class);
                                    i.putExtra("userTlm", numTelS);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), wrongPass, Toast.LENGTH_LONG).show();
                                }
                            }
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
