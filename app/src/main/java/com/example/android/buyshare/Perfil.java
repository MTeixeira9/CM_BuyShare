package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.ValueEventListener;

public class Perfil extends AppCompatActivity {


    private ArrayAdapter<String> mAdapter;
    private TextView nomeTV, pwdTV, nTlm_TV, email_TV;
    private String userTlm;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().setTitle("Meu Perfil");

        Button alteraDados = (Button) findViewById(R.id.alterarDados);

        userTlm = getIntent().getStringExtra("userTlm");

        nomeTV = findViewById(R.id.nome_perfil);
        pwdTV = findViewById(R.id.pwd_perfil);
        nTlm_TV = findViewById(R.id.nTlm_perfil);
        email_TV = findViewById(R.id.email_perfil);

        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    User u = userSnapshot.getValue(User.class);
                    if(u.getNumeroTlm().equals(userTlm)){
                        nomeTV.setText(u.getNome());
                        pwdTV.setText(u.getPassword());
                        nTlm_TV.setText(u.getNumeroTlm());
                        email_TV.setText(u.getEmail());
                        Toast.makeText(getApplicationContext(),u.getEmail(),Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        alteraDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Perfil.this, EditPerfil.class);
                i.putExtra("userTlm", userTlm);
                startActivity(i);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String nome = data.getStringExtra("nome");
                String password = data.getStringExtra("pwd");
                //String conf_pwd = data.getStringExtra("conf_Passw");
                String nTlm = data.getStringExtra("nTlm");
                String email = data.getStringExtra("email");

                TextView nomeTV = (TextView) findViewById(R.id.nome_perfil);
                TextView pwdTV = (TextView) findViewById(R.id.pwd_perfil);
                TextView nTlm_TV = (TextView) findViewById(R.id.nTlm_perfil);
                TextView email_TV = (TextView) findViewById(R.id.email_perfil);


               if(!nome.equals("") ){
                   nomeTV.setText(nome);
               }else if(!password.equals("")){
                   pwdTV.setText(password);
               }else if(!nTlm.equals("")){
                   nTlm_TV.setText(nTlm);
               }else if(!email.equals("")){
                   email_TV.setText(email);
               }





            }
        }
    }
}
