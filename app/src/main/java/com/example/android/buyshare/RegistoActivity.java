package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.FirebaseDatabase;



public class RegistoActivity extends AppCompatActivity {

    private static final String msg = "Registo efetuado com sucesso!";
    private static final String msgErro = "Tem que inserir todos os parâmetros";
    private ImageView alertName, alertPass, alertConfPass, alertEmail, alertNTlm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);

        getSupportActionBar().setTitle("Registo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //INIT
        alertName = (ImageView) findViewById(R.id.alertName);
        alertPass = (ImageView) findViewById(R.id.alertPass);
        alertConfPass = (ImageView) findViewById(R.id.alertConfPass);
        alertEmail = (ImageView) findViewById(R.id.alertEmail);
        alertNTlm = (ImageView) findViewById(R.id.alertNTlm);

        //GONE
        alertName.setVisibility(View.GONE);
        alertPass.setVisibility(View.GONE);
        alertConfPass.setVisibility(View.GONE);
        alertEmail.setVisibility(View.GONE);
        alertNTlm.setVisibility(View.GONE);

        Button registo = (Button) findViewById(R.id.registar);

        registo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView nomeBox = (TextView) findViewById(R.id.nomeRegisto);
                TextView passBox = (TextView) findViewById(R.id.passRegisto);
                TextView confirPassBox = (TextView) findViewById(R.id.confPassRegisto);
                TextView emailBox = (TextView) findViewById(R.id.emailRegisto);
                TextView telemovelBox= (TextView) findViewById(R.id.tlmRegisto);

                String nomeR = nomeBox.getText().toString();
                String passR = passBox.getText().toString();
                String confirPassR = confirPassBox.getText().toString();
                String emailR = emailBox.getText().toString();
                String telemovelR = telemovelBox.getText().toString();

                if(nomeR.equals("")){
                    alertName.setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(), msgErro, Toast.LENGTH_LONG).show();
                }
                else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String userId = database.getReference("users").push().getKey();

                    //User.writeNewUser(userId, nomeR, passR, telemovelR, emailR);
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }


            }
        });

    }



}
