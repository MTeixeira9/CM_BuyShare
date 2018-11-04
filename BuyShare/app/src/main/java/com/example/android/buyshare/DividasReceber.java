package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DividasReceber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividas_receber);

        getSupportActionBar().setTitle("Dívidas [lista 2]");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
