package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class NovaLista extends AppCompatActivity {

   private ArrayAdapter<String> mAdapter;
    private CustomeAdapter customeAdapter;
    private ListView mShoppingList;
    private EditText mItemEdit;
    private String userTlm;
    private DatabaseReference mDatabase;
    private HashMap<String,HashMap<String, Double>> prodQuantCusto;


    private static final String msgErrLista = "Tem de dar um nome à Lista!";
    private static final String msgErrAddProd = "Tem de inserir um produto!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_lista);

        getSupportActionBar().setTitle("Nova Lista");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ir buscar quem estah autenticado
        userTlm = getIntent().getStringExtra("userTlm");

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");
        prodQuantCusto = new HashMap<>();

        Button guardarLista = findViewById(R.id.guardarListaEditLista);
        guardarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nomeL = findViewById(R.id.nomeLEditLista);
                String nomeLista = nomeL.getText().toString();

                if (!nomeLista.equals("")){

                    String key = mDatabase.push().getKey();
                    Lista lista = new Lista(key, userTlm, nomeLista, prodQuantCusto);

                    mDatabase.child(key).setValue(lista);

                    Intent i = new Intent(NovaLista.this, MinhasListas.class);
                    i.putExtra("userTlm", userTlm);
                    startActivity(i);
                    finish();
                }
                else{
                    nomeL.setError(msgErrLista);
                }
            }
        });

        mItemEdit = findViewById(R.id.produtoInserido);
        mShoppingList = findViewById(R.id.listViewItems);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        mShoppingList.setAdapter(mAdapter);

        ImageButton addProduto = findViewById(R.id.addProdButton);
        addProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();

                if (!item.equals("")) {
                    HashMap<String, Double> quantC = new HashMap<>();
                    quantC.put("0,0", 0.0);
                    prodQuantCusto.put(item, quantC);

                    mAdapter.add(item);
                    mAdapter.notifyDataSetChanged();
                    mItemEdit.setText("");
                }
                else{
                    mItemEdit.setError(msgErrAddProd);
                }
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
        Intent i = new Intent(NovaLista.this, MinhasListas.class);
        i.putExtra("userTlm", userTlm);
        startActivity(i);
    }

}
