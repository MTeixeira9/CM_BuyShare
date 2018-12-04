package com.example.android.buyshare;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("users");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        //dataBase = Room.databaseBuilder(getApplicationContext(), BuyShareDatabaase.class, "userinfo").allowMainThreadQueries().build();
        TextView registar = (TextView) findViewById(R.id.tvRegistar);
        Button entrar = (Button) findViewById(R.id.entrar);

        //TextView logo = (TextView) findViewById(R.id.logo);
        //Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/GILLUBCD.TTF");
        //logo.setTypeface(customFont);

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
                EditText nTel =(EditText) findViewById(R.id.numTel);
                EditText pass = (EditText) findViewById(R.id.passRegisto);
                String numTelS = nTel.getText().toString();
               // Toast.makeText(getApplicationContext(),numTelS,Toast.LENGTH_LONG).show();

                final String passS = pass.getText().toString();
                Intent i = new Intent(LoginActivity.this, MinhasListas.class);


                Query q = mDatabase.orderByChild("numeroTlm").equalTo(numTelS);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("Pass","lol");
                        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                            Log.d("Pass------", "bosta");
                            String p = String.valueOf(singleSnapshot.child("password").getValue());
                            Toast.makeText(getApplicationContext(),p,Toast.LENGTH_LONG).show();
                            if (p.equals(passS)){
                               Intent i = new Intent(getApplicationContext(),MinhasListas.class);
                                startActivity(i);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("TAG", "onCancelled", databaseError.toException());
                    }

                });
               /* if (numTelS != null && passS != null && loginUser(numTelS, passS)) {

                    startActivity(i);
                }*/
            }
        });

    }

    private void loginUser(String numTel, String pass) {



        //String u = User.readUser(numTel);


        /*boolean passou = false;

        if (u != null) {
            Toast.makeText(getApplicationContext(), "NAO Fodeu", Toast.LENGTH_LONG).show();
            String passVerdadeira = u;
            if (passVerdadeira.equals(pass))
                passou = true;

        }else{
            Toast.makeText(getApplicationContext(), "Fodeu", Toast.LENGTH_LONG).show();
        }*/



    }
}
