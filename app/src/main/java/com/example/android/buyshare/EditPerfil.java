package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditPerfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);

        getSupportActionBar().setTitle("Editar Dados");

        Button guardarDados = (Button) findViewById(R.id.guardarDados);
        guardarDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nomeET = (EditText) findViewById(R.id.nome_perfil);
                EditText passwordET = (EditText) findViewById(R.id.pass_edit);
                EditText conf_PasswET = (EditText) findViewById(R.id.conf_pwd_edit);
                EditText nTlmET = (EditText) findViewById(R.id.nTlm_perfil);
                EditText emailET = (EditText) findViewById(R.id.email_edit);

                Intent i = new Intent();

                String nome = nomeET.getText().toString();
                String password = passwordET.getText().toString();
                String conf_Passw = conf_PasswET.getText().toString();
                String nTlm = nTlmET.getText().toString();
                String email = emailET.getText().toString();

                // && password.equals(conf_Passw) && !nTlm.equals("") && !email.equals("")

                if (!nome.equals("")) {
                    i.putExtra("nome", nome);
                    i.putExtra("password", password);
                    i.putExtra("conf_Passw", conf_Passw);
                    i.putExtra("nTlm", nTlm);
                    i.putExtra("email", email);

                    setResult(RESULT_OK, i);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Falta inserir dados", Toast.LENGTH_LONG).show();
                }


            }

        });
    }
}
