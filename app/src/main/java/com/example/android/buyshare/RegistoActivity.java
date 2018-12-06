package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class RegistoActivity extends AppCompatActivity {

    private static final String MSG_SUC = "Registo efetuado com sucesso!";
    private static final String MSG_EMAIL_ERRO = "Tem que inserir um email";
    private static final String MSG_INV_EMAIL_ERRO = "Tem que inserir um email válido";
    private static final String MSG_NOME_ERRO = "Tem que inserir um nome";
    private static final String MSG_PASS_ERRO = "Tem que inserir uma password";
    private static final String MSG_PASSES_ERRO = "As palavras passe não coicidem";
    private static final String MSG_NUM_ERRO = "Tem que inserir um número de telemóvel";
    private static final String MSG_INV_NUM_ERRO = "O número deve conter 9 dígitos";

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
                    nomeBox.setError(MSG_NOME_ERRO);
                    emptyName = true;
                }

                if(passR.equals("")){
                    passBox.setError(MSG_PASS_ERRO);
                    emptyPass = true;
                }

                if(confirPassR.equals("")){
                    confirPassBox.setError(MSG_PASS_ERRO);
                    emptyConfpass = true;
                }

                if (!emptyPass && !emptyConfpass){
                    if (!passR.equals(confirPassR)){
                        passBox.setError(MSG_PASSES_ERRO);
                        confirPassBox.setError(MSG_PASSES_ERRO);
                        emptyConfpass = true;
                        emptyPass = true;
                    }
                }

                if(emailR.equals("")){
                    emailBox.setError(MSG_EMAIL_ERRO);
                    emptyEmail = true;
                }
                else{
                    if (!isValidEmail(emailR)){
                        emailBox.setError(MSG_INV_EMAIL_ERRO);
                        emptyEmail = true;
                    }
                }

                if(telemovelR.equals("")){
                    telemovelBox.setError(MSG_NUM_ERRO);
                    emptyNTlm = true;
                }
                else{
                    if(telemovelR.length() != 9){
                        telemovelBox.setError(MSG_INV_NUM_ERRO);
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
