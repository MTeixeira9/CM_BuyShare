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

import com.example.android.buyshare.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class MeusGrupos extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> mAdapter;
    private ListView mListGrupos;
    private String userLogado;

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

        //adicionar logo os amigos da base de dados
        final DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("users");

        Query q = mDataBase.orderByChild("numeroTlm").equalTo(userLogado);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {

                    User u = singleSnapShot.getValue(User.class);
                    List<String> grupos = u.getGrupos();

                    if(grupos != null) {
                        for (String grupo : grupos) {
                            mAdapter.add(grupo);
                            mAdapter.notifyDataSetChanged();
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
        if (requestCode == 1){
            if (resultCode == 1){
                String nomeGrupo = data.getStringExtra("nomeGrupo");
                mAdapter.add(nomeGrupo);
                mAdapter.notifyDataSetChanged();
            }else if(resultCode == -1){
                Toast.makeText(getApplicationContext(), "DEU ERRO A ADD GRUPO", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, MostraGrupo.class);
        String grupo = (String) parent.getItemAtPosition(position);


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
        Intent i = new Intent(MeusGrupos.this, MinhasListas.class);
        i.putExtra("userTlm", userLogado);
        startActivity(i);
    }
}
