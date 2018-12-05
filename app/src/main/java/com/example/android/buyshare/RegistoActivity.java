package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class RegistoActivity extends AppCompatActivity {

    private static final String MSG_SUC = "Registo efetuado com sucesso!";
    //private static final String MSG_EMAIL_ERRO = "Tem que inserir todos os parâmetros";
    //private static final String MSG_PASS_ERRO = "As palavras passe não coicidem";
    private ImageView alertName, alertPass, alertConfPass, alertEmail, alertNTlm;
    private boolean emptyName, emptyPass, emptyConfpass, emptyEmail, emptyNTlm;

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

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

                emptyName = false;
                emptyPass = false;
                emptyConfpass = false;
                emptyEmail = false;
                emptyNTlm = false;

                if(nomeR.equals("")){
                    alertName.setVisibility(View.VISIBLE);
                    emptyName = true;
                }
                else{
                    alertName.setVisibility(View.INVISIBLE);
                }

                if(passR.equals("")){
                    alertPass.setVisibility(View.VISIBLE);
                    emptyPass = true;
                }

                if(confirPassR.equals("")){
                    alertConfPass.setVisibility(View.VISIBLE);
                    emptyConfpass = true;
                }

                if (!emptyPass && !emptyConfpass){
                    if (passR.equals(confirPassR)){
                        alertPass.setVisibility(View.INVISIBLE);
                        alertConfPass.setVisibility(View.INVISIBLE);
                    }
                    else{
                        alertPass.setVisibility(View.VISIBLE);
                        alertConfPass.setVisibility(View.VISIBLE);
                        emptyConfpass = true;
                        emptyPass = true;
                    }
                }

                if(emailR.equals("")){
                    alertEmail.setVisibility(View.VISIBLE);
                    emptyEmail = true;
                }
                else{
                    if (isValidEmail(emailR)){
                        alertEmail.setVisibility(View.INVISIBLE);
                    }
                    else{
                        alertEmail.setVisibility(View.VISIBLE);
                        emptyEmail = true;
                    }
                }

                if(telemovelR.equals("")){
                    alertNTlm.setVisibility(View.VISIBLE);
                    emptyNTlm = true;
                }
                else{
                    if(telemovelR.length() == 9){
                        alertNTlm.setVisibility(View.INVISIBLE);
                    }
                    else{
                        alertNTlm.setVisibility(View.VISIBLE);
                        emptyNTlm = true;
                    }
                }

                if (!emptyName && !emptyPass && !emptyConfpass && !emptyEmail && !emptyNTlm) {

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mDatabase = database.getReference();
                    String userId = database.getReference("users").push().getKey();

                    User user = new User(nomeR, passR, telemovelR, emailR);
                    mDatabase.child("users").child(userId).setValue(user);

                    //User.writeNewUser(userId, nomeR, passR, telemovelR, emailR);
                    Toast.makeText(getApplicationContext(), MSG_SUC, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RegistoActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });

    }

    public final static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();

    }

}
