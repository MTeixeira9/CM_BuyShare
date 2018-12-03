package com.example.android.buyshare;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //BASE DE DADOS
        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        entrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent j = new Intent(LoginActivity.this, MinhasListas.class);
                startActivity(j);
            }
            }
        );

    }
}
