package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Perfil extends AppCompatActivity {


    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().setTitle("Meu Perfil");

        Button alteraDados = (Button) findViewById(R.id.alterarDados);

        alteraDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Perfil.this, EditPerfil.class);
                startActivityForResult(i, 1);
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
