package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditarLista extends AppCompatActivity {

    private String userTlm, key, nomeLista, position;
    private DatabaseReference mDatabase;
    private ListView listView;
    private ValueEventListener mListener;
    private HashMap<String, Double> produtoCusto;
    private ArrayAdapter<String> mAdapter;
    private EditText mItemEdit;


    private static final String msgErrLista = "Tem de dar um nome à Lista!";
    private static final String msgErrAddProd = "Tem de inserir um produto!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_lista);

        getSupportActionBar().setTitle("Nova Lista");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userTlm = getIntent().getStringExtra("userTlm");
        mDatabase = FirebaseDatabase.getInstance().getReference("listas");
        key = getIntent().getStringExtra("key");
        nomeLista = getIntent().getStringExtra("nameL");
        position = getIntent().getStringExtra("position");


        listView = findViewById(R.id.listViewEditLista);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        listView.setAdapter(mAdapter);

        produtoCusto = new HashMap<>();

        final TextView nomeTV = findViewById(R.id.nomeLEditLista);



        mListener = mDatabase.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Lista l = dataSnapshot.getValue(Lista.class);

                produtoCusto = l.getProdutoCusto();

                String tv = l.getNomeLista();

                nomeTV.setText(tv);





                for(String prod : produtoCusto.keySet()){
                    mAdapter.add(prod);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button guardarLista = (Button) findViewById(R.id.guardarListaEditLista);
        guardarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nomeL = (EditText) findViewById(R.id.nomeLEditLista);
                String nomeLista = nomeL.getText().toString();

                if (!nomeLista.equals("")) {

                    mDatabase.child(key).child("nomeLista").setValue(nomeLista);

                    Intent i = new Intent(EditarLista.this, MostraLista.class);
                    i.putExtra("userTlm", userTlm);
                    i.putExtra("position",position);
                    i.putExtra("nameL",nomeLista);
                    i.putExtra("idL", key);

                    startActivity(i);

                }

            }
        });


        mItemEdit = (EditText) findViewById(R.id.produtoInserido);


        ImageButton addProduto = (ImageButton) findViewById(R.id.addProdButton);
        addProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
                if (!item.equals("")) {
                    produtoCusto.put(item, 0.0);
                    //produtos.add(item);
                    mAdapter.add(item);
                    mAdapter.notifyDataSetChanged();
                    mItemEdit.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(), msgErrAddProd, Toast.LENGTH_LONG).show();
                }





                //intent.putStringArrayListExtra("listaProdutos", produtos);
            }
        });


    }
}
