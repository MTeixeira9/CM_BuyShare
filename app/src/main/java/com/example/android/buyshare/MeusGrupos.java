package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MeusGrupos extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> mAdapter;
    private ListView mListGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_grupos);

        getSupportActionBar().setTitle("Meus Grupos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button novoGrupo = (Button) findViewById(R.id.novoGrupo);

        novoGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MeusGrupos.this, CriarGrupo.class);
                startActivityForResult(i, 1);
            }
        });

        mListGrupos = (ListView) findViewById(R.id.listGrupos);
        mListGrupos.setOnItemClickListener(this);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mListGrupos.setAdapter(mAdapter);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String nome = data.getStringExtra("nomeG");
                String novoGrupo = nome;
                mAdapter.add(novoGrupo);
                mAdapter.notifyDataSetChanged();

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
}
