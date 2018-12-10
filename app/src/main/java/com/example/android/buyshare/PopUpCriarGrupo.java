package com.example.android.buyshare;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Grupo;
import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PopUpCriarGrupo extends Activity {

    private static final String msg = "Tem de preencher ambos os campos!";
    private String userLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_grupo);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .75), (int) (height * .25));

        userLogado = getIntent().getStringExtra("userTlm");

        final Button addGrupo = (Button) findViewById(R.id.criarGrupo);
        addGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView nomeGrupo = (TextView) findViewById(R.id.nomeGrupo);

                final Intent i = new Intent();
                final String nomeG = nomeGrupo.getText().toString();

                if (!nomeG.equals("")) {

                    DatabaseReference mDataBaseG = FirebaseDatabase.getInstance().getReference("grupos");
                    final DatabaseReference mDataBaseU = FirebaseDatabase.getInstance().getReference("users");
                    mDataBaseU.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Grupo gAdd = new Grupo(nomeG);
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                User u = singleSnapshot.getValue(User.class);
                                boolean grupoJahExiste = false;
                                for (Grupo g: u.getGrupos()){

                                    //Se o grupo jah existe
                                    if(g.getNome().equals(nomeG)){
                                        setResult(-1, i);
                                        grupoJahExiste = true;
                                    }
                                }
                                if(!grupoJahExiste){
                                    mDataBaseU.child("grupos").setValue(gAdd);
                                    i.putExtra("nomeGrupo", nomeG);
                                    setResult(1, i);
                                    finish();
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("TAG", "onCancelled", databaseError.toException());
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
