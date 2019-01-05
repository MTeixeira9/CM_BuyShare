package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.android.buyshare.Database.Grupo;
import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MostraGrupo extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> mAdapter;
    private String userLogado;
    private String nomeGrupo;
    private ListView mListMembrosGrupo;
    private ValueEventListener mListenerG;
    private ValueEventListener mListenerU;
    private DatabaseReference mDataBaseG;
    private DatabaseReference mDataBaseU;
    private String posGrupoString;
    private int posGrupo;
    private String numAdd, nomeAdd;
    private List<String> membros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_grupo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeGrupo = getIntent().getStringExtra("nomeG");
        userLogado = getIntent().getStringExtra("userTlm");
        posGrupoString = getIntent().getStringExtra("posGrupo");
        posGrupo = Integer.parseInt(posGrupoString);
        getSupportActionBar().setTitle(nomeGrupo);
        Button addMembros = (Button) findViewById(R.id.addMembro);

        addMembros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MostraGrupo.this, AdicionarMembrosGrupo.class);
                i.putExtra("userTlm", userLogado);
                i.putExtra("posGrupo", String.valueOf(posGrupoString));
                i.putExtra("nomeG", nomeGrupo);
                startActivityForResult(i, 1);
            }
        });

        mListMembrosGrupo = (ListView) findViewById(R.id.listMembrosGrupo);
        mListMembrosGrupo.setOnItemClickListener(this);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListMembrosGrupo.setAdapter(mAdapter);

        numAdd = "";
        nomeAdd = "";


        membros = new ArrayList<>();
        //
        mDataBaseG = FirebaseDatabase.getInstance().getReference("grupos");
        mDataBaseU = FirebaseDatabase.getInstance().getReference("users");
        mListenerG = mDataBaseG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mListMembrosGrupo.getAdapter().getCount() == 0) {
                    int count = 0;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Grupo g = singleSnapshot.getValue(Grupo.class);
                        List<String> membrosG = g.getMembrosGrupo();
                        if (membrosG.contains(userLogado)) {
                            if (count == posGrupo) {
                                membros = membrosG;
                            }
                            count++;
                        }
                    }

                    mListenerU = mDataBaseU.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                User u = singleSnapshot.getValue(User.class);

                                if (membros.contains(u.getNumeroTlm())) {
                                    mAdapter.add(u.getNome() + " " + u.getNumeroTlm());
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("TAG", "onCancelled", databaseError.toException());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }

        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, AdicionarMembrosGrupo.class);
        String grupo = (String) parent.getItemAtPosition(position);

        intent.putExtra("userTlm", userLogado);
        intent.putExtra("posGrupo", String.valueOf(position));
        intent.putExtra("nomeG", grupo);
        startActivity(intent);

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
        Intent i = new Intent(MostraGrupo.this, MeusGrupos.class);
        i.putExtra("userTlm", userLogado);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseG.removeEventListener(mListenerG);
        mDataBaseU.removeEventListener(mListenerU);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                userLogado = data.getStringExtra("userTlm");
                nomeGrupo = data.getStringExtra("nomeG");
                posGrupoString = data.getStringExtra("posGrupo");
                mAdapter.add(nomeGrupo);
                mAdapter.notifyDataSetChanged();

            } else if (resultCode == -1) {

            }
        }
    }
}