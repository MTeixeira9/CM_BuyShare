package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DividasPagar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dividas_pagar);

        getSupportActionBar().setTitle("Dívidas [Lista 1]");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
