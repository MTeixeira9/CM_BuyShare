package com.example.android.buyshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class PopUpEstimarCusto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_estimar_custo);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getSupportActionBar().hide();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.32));


        Bundle extras = getIntent().getExtras();

        Double p = extras.getDouble("custo");
        String info = "O preço da lista é " + p.toString() + "€";
        TextView t = (TextView) findViewById(R.id.custo);
        t.setText(info);
    }
}
