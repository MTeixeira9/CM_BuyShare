package com.example.android.buyshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NovaLista extends AppCompatActivity {

    private ArrayAdapter<String> mAdapter;
    private ListView mShoppingList;
    private EditText mItemEdit;

    private static final String msgErrLista = "Tem de dar um nome à Lista!";
    private static final String msgErrAddProd = "Tem de inserir um produto!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_lista);

        getSupportActionBar().setTitle("Nova Lista");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button guardarLista = (Button) findViewById(R.id.guardarLista);
        guardarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nomeL = (EditText) findViewById(R.id.nomeL);
                Intent i = new Intent();
                String nome = nomeL.getText().toString();

                if (!nome.equals("")){
                    i.putExtra("nomeL", nome);
                    setResult(RESULT_OK, i);
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
        if(id == R.id.addMembros){
            Intent addMembros = new Intent(NovaLista.this, AdicionarMembrosNovaLista.class);
            startActivity(addMembros);

        }else if(id == R.id.verMembros){
            Intent amigos = new Intent(NovaLista.this, VerMembros.class);
            startActivity(amigos);

        }else if(id == R.id.finLista){
            Intent finLista = new Intent(NovaLista.this, AdicionarCustoL.class);
            startActivity(finLista);

        }
        return super.onOptionsItemSelected(item);
    }

}
