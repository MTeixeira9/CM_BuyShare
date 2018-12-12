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
import android.widget.CheckedTextView;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MostraGrupo extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> mAdapter;
    private String userLogado;
    private String nomeGrupo;
    private ListView mListMembrosGrupo;
    private ValueEventListener mListener;
    private DatabaseReference mDataBaseG;
    private String posGrupoString;
    private int posGrupo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_grupo);

        getSupportActionBar().setTitle(nomeGrupo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomeGrupo = getIntent().getStringExtra("nomeG");
        userLogado = getIntent().getStringExtra("userTlm");
        posGrupoString = getIntent().getStringExtra("posGrupo");
        posGrupo = Integer.parseInt(posGrupoString);

        Button addMembros = (Button) findViewById(R.id.addMembro);

        addMembros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MostraGrupo.this, AdicionarMembros.class);
                i.putExtra("userTlm", userLogado);
                i.putExtra("posGrupo", String.valueOf(posGrupoString));
                i.putExtra("nomeG", nomeGrupo);
                startActivityForResult(i, 1);
            }
        });



        mListMembrosGrupo = (ListView) findViewById(R.id.listMembrosGrupo);
        //mListMembrosGrupo.setOnItemClickListener(this);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListMembrosGrupo.setAdapter(mAdapter);

        //
        mDataBaseG = FirebaseDatabase.getInstance().getReference("grupos");
        mListener = mDataBaseG.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mListMembrosGrupo.getAdapter().getCount() == 0) {
                    int count = 0;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Grupo g = singleSnapshot.getValue(Grupo.class);
                        List<String> membrosG = g.getMembrosGrupo();
                        for(String membro : membrosG){
                            if(membro.equals(userLogado)){
                                if(count == posGrupo){
                                    for(String membroAdd : membrosG){
                                        mAdapter.add(membroAdd);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                                count++;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, AdicionarMembros.class);
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

        }
    }
}