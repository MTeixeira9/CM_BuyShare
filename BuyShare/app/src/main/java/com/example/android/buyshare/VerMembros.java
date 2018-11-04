package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VerMembros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_membros);

        getSupportActionBar().setTitle("Membros de [nome lista]");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
