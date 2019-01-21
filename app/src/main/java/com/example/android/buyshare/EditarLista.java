package com.example.android.buyshare;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditarLista extends AppCompatActivity {

    private String userTlm, key, nomeLista, position, nomeLista2, tipoLista;
    private DatabaseReference mDatabase;
    private ListView listView;
    private HashMap<String, HashMap<String, Double>> prodQuantCusto;
    private ArrayAdapter<String> mAdapter;
    private EditText mItemEdit;
    private List<String> lProdutos;
    private static final String MSG_EMPTY_LIST_NAME = "Tem de dar um nome à Lista!";
    private static final String MSG_NO_EMPTY_LIST = "A lista deve conter pelo menos 1 produto!";
    private static final String MSG_ERR_ADD_PROD = "Tem de inserir um produto!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_lista);

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");

        userTlm = getIntent().getStringExtra("userTlm");
        key = getIntent().getStringExtra("key");
        nomeLista = getIntent().getStringExtra("nameL");
        position = getIntent().getStringExtra("position");
        tipoLista = getIntent().getStringExtra("tipoL");
        nomeLista2 = "";
        lProdutos = new ArrayList<>();

        getSupportActionBar().setTitle(nomeLista);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.listViewEditLista);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        listView.setAdapter(mAdapter);
        /**
         * opcoes listas
         * */
        registerForContextMenu(listView);

        prodQuantCusto = new HashMap<>();

        final TextView nomeTV = findViewById(R.id.nomeLEditLista);

        mDatabase.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listView.getAdapter().getCount() == 0) {
                    Lista l = dataSnapshot.getValue(Lista.class);
                    prodQuantCusto = l.getProdutoCusto();
                    nomeTV.setText(l.getNomeLista());

                    if (prodQuantCusto != null) {
                        for (String prod : prodQuantCusto.keySet()) {
                            lProdutos.add(prod);
                            mAdapter.add(prod);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button guardarLista = findViewById(R.id.guardarListaEditLista);
        guardarLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nomeL = findViewById(R.id.nomeLEditLista);
                nomeLista2 = nomeL.getText().toString();

                if (!nomeLista2.equals("")) {

                    mDatabase.child(key).child("nomeLista").setValue(nomeLista2);
                    mDatabase.child(key).child("produtoCusto").setValue(prodQuantCusto);

                    Intent i = new Intent(EditarLista.this, MostraLista.class);
                    i.putExtra("userTlm", userTlm);
                    i.putExtra("position", position);
                    i.putExtra("nameL", nomeLista2);
                    i.putExtra("tipoL", tipoLista);

                    startActivity(i);

                } else {
                    nomeL.setError(MSG_EMPTY_LIST_NAME);
                }
            }
        });


        mItemEdit = findViewById(R.id.produtoInserido);

        ImageButton addProduto = findViewById(R.id.addProdButton);
        addProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mItemEdit.getText().toString();
                if (!item.equals("")) {
                    HashMap<String, Double> quantC = new HashMap<>();
                    quantC.put("0,0", 0.0);

                    if (prodQuantCusto == null)
                        prodQuantCusto = new HashMap<>();
                    prodQuantCusto.put(item, quantC);
                    lProdutos.add(item);

                    mAdapter.add(item);
                    mAdapter.notifyDataSetChanged();
                    mItemEdit.setText("");
                } else {
                    mItemEdit.setError(MSG_ERR_ADD_PROD);
                }
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Eliminar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int p = info.position;
        String pSelected = lProdutos.get(p);
        if (item.getTitle().equals("Eliminar")) {
            if (lProdutos.size() == 1) {
                Toast.makeText(getApplicationContext(), MSG_NO_EMPTY_LIST, Toast.LENGTH_LONG).show();
            } else {
                prodQuantCusto.remove(pSelected);
                mAdapter.remove(pSelected);
                mAdapter.notifyDataSetChanged();
            }
        }

        return super.onContextItemSelected(item);

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
        Intent i = new Intent(EditarLista.this, MostraLista.class);
        i.putExtra("userTlm", userTlm);
        i.putExtra("position", position);
        i.putExtra("tipoL", tipoLista);

        if (nomeLista2.equals(""))
            i.putExtra("nameL", nomeLista);
        else
            i.putExtra("nameL", nomeLista2);
        startActivity(i);
    }
}
