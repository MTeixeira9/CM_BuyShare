package com.example.android.buyshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.buyshare.Database.Lista;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinhasListas extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> mAdapter2;
    private String userTlm;
    private ListView mListasPartilhadas;
    private ListView mListasPrivadas;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    private ArrayList<Lista> lPrivadas, lPartilhadas;
    private ListView selectedListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_listas);

        getSupportActionBar().setTitle("Minhas Listas");

        Button novaLista = findViewById(R.id.novaLista);

        //ir buscar quem estah autenticado
        userTlm = getIntent().getStringExtra("userTlm");

        // produtoCusto = (HashMap<String, Double>)getIntent().getSerializableExtra("produtoCusto");

        mDatabase = FirebaseDatabase.getInstance().getReference("listas");

        mListasPartilhadas = findViewById(R.id.listaPartilhadas);
        mListasPrivadas = findViewById(R.id.listasPrivadas);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        mListasPartilhadas.setAdapter(mAdapter);
        mListasPrivadas.setAdapter(mAdapter2);

        lPartilhadas = new ArrayList<>();
        lPrivadas = new ArrayList<>();

        /**
         * opcoes listas
         * */
        registerForContextMenu(mListasPrivadas);
        registerForContextMenu(mListasPartilhadas);

        novaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MinhasListas.this, NovaLista.class);
                i.putExtra("userTlm", userTlm);
                startActivity(i);
            }
        });

        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mListasPrivadas.getAdapter().getCount() == 0) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                        Lista l = singleSnapshot.getValue(Lista.class);


                        //listas privadas (e nao arquivadas)
                        if (l.getCriadorLista().equals(userTlm) && !l.isPartilhada() && !l.getQuemArquivou().contains(userTlm)) {
                            lPrivadas.add(l);

                            mAdapter2.add(l.getNomeLista());
                            mAdapter2.notifyDataSetChanged();
                        }
                        //listas partilhadas (e nao arquivadas nem eliminadas por mim)
                        else if (l.getMembrosLista().contains(userTlm) && l.isPartilhada()
                                && !l.getQuemArquivou().contains(userTlm) && !l.getQuemEliminou().contains(userTlm)) {
                            lPartilhadas.add(l);

                            mAdapter.add(l.getNomeLista());
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

        mListasPrivadas.setOnItemClickListener(this);
        mListasPartilhadas.setOnItemClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Arquivar");
        menu.add(0, v.getId(), 0, "Eliminar");

        selectedListView = (ListView) v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        mDatabase = FirebaseDatabase.getInstance().getReference("listas");
        final int p = info.position;
        int idListView = selectedListView.getId();
        Lista lSelected = null;

        switch (idListView) {
            case R.id.listasPrivadas:

                //IR BUSCAR A LISTA SELECIONADA
                lSelected = lPrivadas.get(p);

                if (item.getTitle().equals("Arquivar")) {
                    List<String> quemArq = lSelected.getQuemArquivou();
                    quemArq.add(userTlm);
                    mDatabase.child(lSelected.getIdL()).child("quemArquivou").setValue(quemArq);
                    mAdapter2.remove(lSelected.getNomeLista());
                    mAdapter2.notifyDataSetChanged();

                } else if (item.getTitle().equals("Eliminar")) {
                    mDatabase.child(lSelected.getIdL()).removeValue();
                    mAdapter2.remove(lSelected.getNomeLista());
                    mAdapter2.notifyDataSetChanged();
                }

                break;

            case R.id.listaPartilhadas:

                //IR BUSCAR A LISTA SELECIONADA
                lSelected = lPartilhadas.get(p);

                if (item.getTitle().equals("Arquivar")) {
                    List<String> quemArq = lSelected.getQuemArquivou();
                    quemArq.add(userTlm);
                    mDatabase.child(lSelected.getIdL()).child("quemArquivou").setValue(quemArq);
                    mAdapter.remove(lSelected.getNomeLista());
                    mAdapter.notifyDataSetChanged();

                } else if (item.getTitle().equals("Eliminar")) {
                    List<String> quemEli = lSelected.getQuemEliminou();
                    quemEli.add(userTlm);
                    mDatabase.child(lSelected.getIdL()).child("quemEliminou").setValue(quemEli);
                    mAdapter.remove(lSelected.getNomeLista());
                    mAdapter.notifyDataSetChanged();
                }

                break;

            default:
                Toast.makeText(getApplicationContext(), "Algo correu mal", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onContextItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.meusGrupos) {
            Intent grupos = new Intent(MinhasListas.this, MeusGrupos.class);
            grupos.putExtra("userTlm", userTlm);
            startActivity(grupos);
        } else if (id == R.id.amigos) {
            Intent amigos = new Intent(MinhasListas.this, Amigos.class);
            amigos.putExtra("userTlm", userTlm);
            startActivity(amigos);
        } else if (id == R.id.terminarS) {
            Toast.makeText(getApplicationContext(), "Sessão terminada com sucesso.", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        } else if (id == R.id.meuPerfil) {
            Intent meuPerfil = new Intent(MinhasListas.this, Perfil.class);
            meuPerfil.putExtra("userTlm", userTlm);
            startActivity(meuPerfil);
        } else if (id == R.id.arquivo) {
            Intent arquivo = new Intent(MinhasListas.this, Arquivo.class);
            arquivo.putExtra("userTlm", userTlm);
            startActivity(arquivo);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, MostraLista.class);
        String name = (String) parent.getItemAtPosition(position);

        intent.putExtra("nameL", name);
        intent.putExtra("userTlm", userTlm);
        intent.putExtra("position", Integer.toString(position));
        //intent.putExtra("nomeClasse","0");

        startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 11) {
            if (resultCode == 11) {
                userTlm = data.getStringExtra("userTlm");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabase.removeEventListener(mListener);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MinhasListas.this, LoginActivity.class);
        startActivity(i);
    }
}