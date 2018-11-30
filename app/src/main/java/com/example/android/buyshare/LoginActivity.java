package com.example.android.buyshare;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.buyshare.BD.BuyShareDatabaase;

public class LoginActivity extends AppCompatActivity {

    public static BuyShareDatabaase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        dataBase = Room.databaseBuilder(getApplicationContext(), BuyShareDatabaase.class, "userinfo").allowMainThreadQueries().build();
        TextView registar = (TextView) findViewById(R.id.tvRegistar);
        Button entrar = (Button) findViewById(R.id.entrar);

        TextView logo = (TextView) findViewById(R.id.logo);
        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/GILLUBCD.TTF");
        logo.setTypeface(customFont);

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
