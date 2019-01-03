package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
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

import java.util.HashMap;

public class NovaLista extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
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

        Button guardarLista = (Button) findViewById(R.id.guardarListaEditLista);
        guardarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText nomeL = (EditText) findViewById(R.id.nomeLEditLista);
                String nomeLista = nomeL.getText().toString();


                String key = "";

                if (!nomeLista.equals("")){

                    key = mDatabase.push().getKey();
                    Lista lista = new Lista(key, userTlm, nomeLista, prodQuantCusto);

                    mDatabase.child(key).setValue(lista);

                    //Bundle b = new Bundle();
                   // b.putStringArrayList("listaProdutos", produto);

                    Intent i = new Intent(NovaLista.this, MinhasListas.class);
                    i.putExtra("userTlm", userTlm);
                    //i.putExtra("produtoCusto", produtoCusto);
                    //i.putExtras(b);


                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), msgErrLista, Toast.LENGTH_LONG).show();
                }
            }
        });

        mItemEdit = (EditText) findViewById(R.id.produtoInserido);
        mShoppingList = (ListView) findViewById(R.id.listViewItems);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        mShoppingList.setAdapter(mAdapter);





        ImageButton addProduto = (ImageButton) findViewById(R.id.addProdButton);
        addProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
                if (!item.equals("")) {
                    HashMap<String, Double> quantC = new HashMap<>();
                    quantC.put("0,0", 0.0);
                    prodQuantCusto.put(item, quantC);
                    //produtos.add(item);
                    mAdapter.add(item);
                    mAdapter.notifyDataSetChanged();
                    mItemEdit.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(), msgErrAddProd, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.novalista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

}
