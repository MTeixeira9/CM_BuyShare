package com.example.android.buyshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PopUpAddAmigo extends Activity {

    private static final String msgErro = "Tem de preencher ambos os campos!";
    private static final String msgAddAmigo = "Amigo adicionado com sucesso!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivity_popupaddamigo);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8),(int) (height*.32));

        Button addAmigo = (Button) findViewById(R.id.enviarConvite);
        addAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nTelemovel = (TextView) findViewById(R.id.nTelemovel);
                TextView nomeAmigo = (TextView) findViewById(R.id.nomeAmigo);
                Intent i = new Intent();
                String nTele = nTelemovel.getText().toString();
                String nome = nomeAmigo.getText().toString();

                if (!nome.equals("") && !nTele.equals("")){
                    i.putExtra("nTlm", nTele);
                    i.putExtra("nomeA", nome);
                    setResult(RESULT_OK, i);
                    Toast.makeText(getApplicationContext(), msgAddAmigo, Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), msgErro, Toast.LENGTH_LONG).show();
                }

            }
        });
        
    }
}
