package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PopUpCategoria extends AppCompatActivity {

    private static final String msgErro = "Tem de preencher ambos os campos!";
    private static final String msgAddCat = "Categoria adicionada com sucesso!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pop_up_categoria);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getSupportActionBar().hide();

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.32));

        Button addCategoria = (Button) findViewById(R.id.criarCat);
        addCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nomeCat = (TextView) findViewById(R.id.nomeCategoria);
                Intent i = new Intent();
                String nomeCategoria = nomeCat.getText().toString();

                if (!nomeCategoria.equals("")){
                    i.putExtra("nCat", nomeCategoria);
                    setResult(RESULT_OK, i);
                    Toast.makeText(getApplicationContext(), msgAddCat, Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), msgErro, Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
