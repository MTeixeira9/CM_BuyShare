package com.example.android.buyshare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private static final String MSG_USER_EXIST_ERRO = "O número deve conter 9 dígitos";

    private static int RESULT_LOAD_IMAGE = 1;

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

        Button insereFoto = findViewById(R.id.escolherFoto);
        insereFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);

            }
        });




        Button registo = (Button) findViewById(R.id.registar);
        registo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView nomeBox = (TextView) findViewById(R.id.nomeRegisto);
                TextView passBox = (TextView) findViewById(R.id.passRegisto);
                TextView confirPassBox = (TextView) findViewById(R.id.confPassRegisto);
                TextView emailBox = (TextView) findViewById(R.id.emailRegisto);
                TextView telemovelBox = (TextView) findViewById(R.id.tlmRegisto);

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



                if (nomeR.equals("")) {
                    nomeBox.setError(MSG_NOME_ERRO);
                    emptyName = true;
                }

                if (passR.equals("")) {
                    passBox.setError(MSG_PASS_ERRO);
                    emptyPass = true;
                }

                if (confirPassR.equals("")) {
                    confirPassBox.setError(MSG_PASS_ERRO);
                    emptyConfpass = true;
                }

                if (!emptyPass && !emptyConfpass) {
                    if (!passR.equals(confirPassR)) {
                        passBox.setError(MSG_PASSES_ERRO);
                        confirPassBox.setError(MSG_PASSES_ERRO);
                        emptyConfpass = true;
                        emptyPass = true;
                    }
                }

                if (emailR.equals("")) {
                    emailBox.setError(MSG_EMAIL_ERRO);
                    emptyEmail = true;
                } else {
                    if (!isValidEmail(emailR)) {
                        emailBox.setError(MSG_INV_EMAIL_ERRO);
                        emptyEmail = true;
                    }
                }

                if (telemovelR.equals("")) {
                    telemovelBox.setError(MSG_NUM_ERRO);
                    emptyNTlm = true;
                } else {
                    if (telemovelR.length() != 9) {
                        telemovelBox.setError(MSG_INV_NUM_ERRO);
                        emptyNTlm = true;
                    }
                }

                if (!emptyName && !emptyPass && !emptyConfpass && !emptyEmail && !emptyNTlm) {

                    FirebaseDatabase fd = FirebaseDatabase.getInstance();
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference mDatabase = fd.getReference("users");

                    if (userIsRegisted(mDatabase, telemovelR)) {
                        Toast.makeText(getApplicationContext(), MSG_USER_EXIST_ERRO, Toast.LENGTH_LONG).show();
                    } else {

                        //String userId = fd.getReference("users").push().getKey();
                        User user = new User(nomeR, passR, telemovelR, emailR);
                        database.child("users").child(telemovelR).setValue(user);

                        Toast.makeText(getApplicationContext(), MSG_SUC, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(RegistoActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

    }

    private boolean userIsRegisted(DatabaseReference mDatabase, String numTelS) {
        final boolean[] exists = {false};
        Toast.makeText(getApplicationContext(), "ANTES", Toast.LENGTH_LONG).show();
        Query q = mDatabase.orderByChild("numeroTlm").equalTo(numTelS);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    String p = String.valueOf(singleSnapshot.child("numeroTlm").getValue());

                    if (!p.equals(""))
                        exists[0] = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

        return exists[0];
    }

    public final static boolean isValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);



        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

}
