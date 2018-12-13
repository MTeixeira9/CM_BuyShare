package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import java.util.Map;

public class Amigos extends AppCompatActivity {

    private static final String MSG_SUCESSO = "Amigo adicionado com sucesso!";

    private ArrayAdapter<String> mAdapter;
    private String userLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        getSupportActionBar().setTitle("Meus Amigos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button addAmigos = findViewById(R.id.addAmigos);
        userLogado = getIntent().getStringExtra("userTlm");

        addAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //POPUP
                Intent i = new Intent(Amigos.this, PopUpAddAmigo.class);
                i.putExtra("userTlm", userLogado);
                startActivityForResult(i, 1);
            }
        });

        ListView mListAmigos = findViewById(R.id.listAmigos);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListAmigos.setAdapter(mAdapter);

        //adicionar logo os amigos da base de dados
        final DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference("users");

        Query q = mDataBase.orderByChild("numeroTlm").equalTo(userLogado);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapShot : dataSnapshot.getChildren()) {
                    User u = singleSnapShot.getValue(User.class);
                    Map<String, String> amigos = (Map<String, String>)  u.getAmigos();

                    if(amigos != null) {
                        for (Map.Entry<String, String> amigo : amigos.entrySet()) {
                            mAdapter.add(amigo.getValue() + " " + amigo.getKey());
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
        if (requestCode == 1) {
            if (resultCode == 1) {
                String nome = data.getStringExtra("nomeA");
                String tlmv = data.getStringExtra("nTlm");
                String novoAmigo = nome + " " + tlmv;
                mAdapter.add(novoAmigo);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), MSG_SUCESSO, Toast.LENGTH_LONG).show();
            }
        }
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
        Intent i = new Intent(Amigos.this, MinhasListas.class);
        i.putExtra("userTlm", userLogado);
        startActivity(i);
    }
}