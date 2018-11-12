package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EstimarCustoLista extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimar_custo_lista);

        Button custo = (Button) findViewById(R.id.custo);

        custo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //POPUP
                Intent i = new Intent(EstimarCustoLista.this, PopUpEstimarCusto.class);
                startActivityForResult(i, 1);
            }
        });
    }
}
