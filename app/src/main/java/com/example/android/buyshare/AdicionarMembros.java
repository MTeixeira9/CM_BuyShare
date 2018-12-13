package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.Map;

public class AdicionarMembros extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
    private String userLogado;
    private LinearLayout linearLayout;
    private String nomeGrupo;
    private String posGrupoString;
    private CheckBox cb;
    private ValueEventListener mListener;
    private DatabaseReference mDataBaseG;
    private List<String> paraAdicionar;
    private Button adicionar;
    private Map<String, String> amigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_membros);

        getSupportActionBar().setTitle("Adicionar Membros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.linearL);

        userLogado = getIntent().getStringExtra("userTlm");
        nomeGrupo = getIntent().getStringExtra("nomeG");
        posGrupoString = getIntent().getStringExtra("posGrupo");

        getSupportActionBar().setTitle(nomeGrupo);

        adicionar = (Button) findViewById(R.id.edit_button);

        ListView mListAmigos = findViewById(R.id.listAmigos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        //mListAmigos.setAdapter(mAdapter);


        //adicionar logo os amigos da base de dados
        final DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("users");

        Query q = mDataBase.orderByChild("numeroTlm").equalTo(userLogado);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                amigos = null;

                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    User u = singleSnapShot.getValue(User.class);
                    amigos = (Map<String, String>) u.getAmigos();

                    if (amigos != null) {
                        for (Map.Entry<String, String> amigo : amigos.entrySet()) {
                            cb = new CheckBox(getApplicationContext());
                            cb.setText(amigo.getValue() + "|" + amigo.getKey());
                            linearLayout.addView(cb);
                            //mAdapter.add(amigo.getValue() + " " + amigo.getKey());
                            //mAdapter.notifyDataSetChanged();
                        }
                    }
                }

                //botao

                adicionar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(AdicionarMembros.this, MostraGrupo.class);

                        paraAdicionar = new ArrayList<>();
                        for (int a = 0; a <= linearLayout.getChildCount(); a++) {

                            View view = linearLayout.getChildAt(a);
                            if (view instanceof CheckBox) {
                                CheckBox c = (CheckBox) view;
                                //cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                //  @Override
                                //public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                if (c.isChecked()) {
                                    Toast.makeText(getApplicationContext(), "DEPOISSS", Toast.LENGTH_LONG).show();
                                    String[] add = c.getText().toString().split("\\|");
                                    Toast.makeText(getApplicationContext(), add[1], Toast.LENGTH_LONG).show();
                                    paraAdicionar.add(add[1]);
                                }

                            }
                        }

                        mDataBaseG = FirebaseDatabase.getInstance().getReference("grupos");
                        mListener = mDataBaseG.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int count = 0;
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    Grupo g = singleSnapshot.getValue(Grupo.class);

                                    if (g.getAdmin().equals(userLogado) && g.getNome().equals(nomeGrupo)) {
                                        List<String> membrosG = g.getMembrosGrupo();
                                        for (String numAdd : paraAdicionar) {
                                            if(!g.getMembrosGrupo().contains(numAdd)) {
                                                membrosG.add(numAdd);
                                            }
                                        }
                                        mDataBaseG.child(g.getIdG()).child("membrosGrupo").setValue(membrosG);
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("TAG", "onCancelled", databaseError.toException());
                            }

                        });
                        i.putExtra("userTlm", userLogado);
                        i.putExtra("nomeG", nomeGrupo);
                        i.putExtra("posGrupo", posGrupoString);
                        setResult(1, i);
                        startActivity(i);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AdicionarMembros.this, MostraGrupo.class);
        i.putExtra("userTlm", userLogado);
        i.putExtra("nomeG", nomeGrupo);
        i.putExtra("posGrupo", posGrupoString);

        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseG.removeEventListener(mListener);
    }
}
