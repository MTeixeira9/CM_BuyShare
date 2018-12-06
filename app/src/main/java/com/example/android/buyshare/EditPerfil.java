package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditPerfil extends AppCompatActivity {

    private String userTlm;
    private DatabaseReference mDatabase;
    private Query q;
    private EditText nomeET, passwordET, conf_PasswET, nTlmET, emailET;
    private TextView nomeTV, pwdTV, nTlm_TV, email_TV;
    private String nome, password, conf_Passw, nTlm, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        getSupportActionBar().setTitle("Editar Dados");

        userTlm = getIntent().getStringExtra("userTlm");


        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        nomeET = (EditText) findViewById(R.id.nome_perfil);
        passwordET = (EditText) findViewById(R.id.pass_edit);
        conf_PasswET = (EditText) findViewById(R.id.conf_pwd_edit);
        nTlmET = (EditText) findViewById(R.id.nTlm_perfil);
        emailET = (EditText) findViewById(R.id.email_edit);


        q = mDatabase.orderByChild("numeroTlm").equalTo(userTlm);


        Button guardarDados = (Button) findViewById(R.id.guardarDados);
        guardarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = nomeET.getText().toString();
                password = passwordET.getText().toString();
                conf_Passw = conf_PasswET.getText().toString();
                nTlm = nTlmET.getText().toString();
                email = emailET.getText().toString();

                if (!password.equals(conf_Passw)){

                    Toast.makeText(getApplicationContext(), "Palavra passe não coincide", Toast.LENGTH_SHORT).show();

                }else if (!nome.equals("") || !password.equals("") || !conf_Passw.equals("")|| !nTlm.equals("") || !email.equals("")) {


                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){

                                if(!nome.equals("")) {
                                    mDatabase.child(userTlm).child("nome").setValue(nome);
                                }

                                if(!email.equals("")) {
                                    mDatabase.child(userTlm).child("email").setValue(email);
                                }

                                if(!nTlm.equals("")) {
                                    mDatabase.child(userTlm).child("numeroTlm").setValue(nTlm);
                                }

                                if(!password.equals("")) {
                                    mDatabase.child(userTlm).child("password").setValue(password);
                                }
                            }

                            Intent i = new Intent(EditPerfil.this, Perfil.class);
                            i.putExtra("userTlm", userTlm);
                            startActivity(i);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    //finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Falta inserir dados", Toast.LENGTH_LONG).show();
                }

                Intent i = new Intent(EditPerfil.this, Perfil.class);
                i.putExtra("userTlm", userTlm);
                startActivity(i);
            }

        });
    }
}
