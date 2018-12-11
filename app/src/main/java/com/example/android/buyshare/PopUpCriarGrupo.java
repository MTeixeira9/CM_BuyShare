package com.example.android.buyshare;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.buyshare.Database.Grupo;
import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PopUpCriarGrupo extends Activity {

    private static final String msg = "Tem de preencher ambos os campos!";
    private String userLogado;
    private DatabaseReference mDataBaseU;
    private Intent i;
    private  ValueEventListener mListener;

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

        Button addGrupo = findViewById(R.id.criarGrupo);
        addGrupo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText nomeGrupo = findViewById(R.id.nomeGrupo);

                i = new Intent();
                final String nomeG = nomeGrupo.getText().toString();

                if (!nomeG.equals("")) {
                    final DatabaseReference mDataBaseG = FirebaseDatabase.getInstance().getReference("grupos");

                    mDataBaseU = FirebaseDatabase.getInstance().getReference("users");
                    Query q = mDataBaseU.orderByChild("numeroTlm").equalTo(userLogado);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                User u = singleSnapshot.getValue(User.class);
                                boolean grupoJahExiste = false;

                                if (u.getGrupos() != null) {

                                    for (String g : u.getGrupos()) {
                                        //Se o grupo jah existe
                                        if (g.equals(nomeG)) {
                                            setResult(-1, i);
                                            grupoJahExiste = true;
                                        }
                                    }
                                }
                                if (!grupoJahExiste) {
                                    Grupo gAdd = new Grupo(nomeG, u.getNumeroTlm());
                                    List<String> grupos = u.getGrupos();

                                    if(grupos == null){
                                        grupos = new ArrayList<>();
                                    }

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    String key = database.getReference("grupos").push().getKey();
                                    grupos.add(nomeG);
                                    mDataBaseU.child(userLogado).child("grupos").setValue(grupos);
                                    mDataBaseG.child(key).setValue(gAdd);
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
