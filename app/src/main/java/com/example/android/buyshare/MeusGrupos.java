package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.EventListener;
import java.util.List;
import java.util.Map;

public class MeusGrupos extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> mAdapter;
    private ListView mListGrupos;
    private String userLogado;
    private ValueEventListener mListener;
    private DatabaseReference mDataBaseG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_grupos);

        getSupportActionBar().setTitle("Meus Grupos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button novoGrupo = (Button) findViewById(R.id.novoGrupo);

        userLogado = getIntent().getStringExtra("userTlm");

        novoGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MeusGrupos.this, PopUpCriarGrupo.class);
                i.putExtra("userTlm", userLogado);
                startActivityForResult(i, 1);
            }
        });

        mListGrupos = (ListView) findViewById(R.id.listGrupos);
        mListGrupos.setOnItemClickListener(this);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListGrupos.setAdapter(mAdapter);

        //adicionar logo os grupos em que a pessoa estah adicionada
        mDataBaseG = FirebaseDatabase.getInstance().getReference("grupos");
        mListener = mDataBaseG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mListGrupos.getAdapter().getCount() == 0) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        Grupo g = singleSnapshot.getValue(Grupo.class);
                        List<String> membrosG = g.getMembrosGrupo();

                        if (membrosG != null) {
                            for (String membro : membrosG) {
                                if (membro.equals(userLogado)) {
                                    mAdapter.add(g.getNome());
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }


        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                String nomeGrupo = data.getStringExtra("nomeGrupo");
                mAdapter.add(nomeGrupo);
                mAdapter.notifyDataSetChanged();
            } else if (resultCode == -1) {
                Toast.makeText(getApplicationContext(), "DEU ERRO A ADD GRUPO", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, MostraGrupo.class);
        String grupo = (String) parent.getItemAtPosition(position);

        intent.putExtra("userTlm", userLogado);
        intent.putExtra("posGrupo", String.valueOf(position));
        intent.putExtra("nomeG", grupo);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBaseG.removeEventListener(mListener);
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
        Intent i = new Intent(MeusGrupos.this, MinhasListas.class);
        i.putExtra("userTlm", userLogado);
        startActivity(i);
    }
}
